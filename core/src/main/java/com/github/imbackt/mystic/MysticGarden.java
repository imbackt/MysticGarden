package com.github.imbackt.mystic;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.imbackt.mystic.audio.AudioManager;
import com.github.imbackt.mystic.ecs.ECSEngine;
import com.github.imbackt.mystic.input.InputManager;
import com.github.imbackt.mystic.map.MapManager;
import com.github.imbackt.mystic.screen.ScreenType;
import com.github.imbackt.mystic.view.GameRenderer;

import java.util.EnumMap;

public class MysticGarden extends Game {
    private static final String TAG = MysticGarden.class.getSimpleName();

    private SpriteBatch spriteBatch;
    private EnumMap<ScreenType, Screen> screenCache;
    private OrthographicCamera gameCamera;
    private FitViewport screenViewport;

    public static final BodyDef BODY_DEF = new BodyDef();
    public static final FixtureDef FIXTURE_DEF = new FixtureDef();
    public static final float UNIT_SCALE = 1 / 32f;
    public static final short BIT_PLAYER = 1 << 0;
    public static final short BIT_GROUND = 1 << 1;
    private World world;
    private WorldContactListener worldContactListener;

    private static final float FIXED_TIME_STEP = 1 / 144f;
    private float accumulator;

    private AssetManager assetManager;
    private AudioManager audioManager;
    private MapManager mapManager;
    private Stage stage;
    private Skin skin;
    private I18NBundle i18NBundle;
    private InputManager inputManager;
    private ECSEngine ecsEngine;

    private GameRenderer gameRenderer;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        spriteBatch = new SpriteBatch();

        // box2d
        accumulator = 0f;
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);

        // initialize assetManager
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
        initializeSkin();
        stage = new Stage(new FitViewport(450, 800), spriteBatch);

        // audio
        audioManager = new AudioManager(this);

        // input
        inputManager = new InputManager();
        Gdx.input.setInputProcessor(new InputMultiplexer(inputManager, stage));


        // setup game view port
        gameCamera = new OrthographicCamera();
        screenViewport = new FitViewport(9, 16, gameCamera);

        // map
        mapManager = new MapManager(this);

        // ecs engine
        ecsEngine = new ECSEngine(this);

        // game renderer
        gameRenderer = new GameRenderer(this);

        // set first screen
        screenCache = new EnumMap<>(ScreenType.class);
        setScreen(ScreenType.LOADING);
    }

    public static void resetBodiesAndFixtureDefinition() {
        BODY_DEF.position.set(0, 0);
        BODY_DEF.gravityScale = 1;
        BODY_DEF.type = BodyDef.BodyType.StaticBody;
        BODY_DEF.fixedRotation = false;

        FIXTURE_DEF.density = 0;
        FIXTURE_DEF.isSensor = false;
        FIXTURE_DEF.restitution = 0;
        FIXTURE_DEF.friction = 0.2f;
        FIXTURE_DEF.filter.categoryBits = 0x0001;
        FIXTURE_DEF.filter.maskBits = -1;
        FIXTURE_DEF.shape = null;
    }

    private void initializeSkin() {
        // Setup markup colors
        Colors.put("Red", Color.RED);
        Colors.put("Blue", Color.BLUE);


        // Generate ttf bitmaps
        final ObjectMap<String, Object> resources = new ObjectMap<>();
        final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        final int[] sizesToCreate = {16, 20, 26, 32};
        for (int size : sizesToCreate) {
            fontParameter.size = size;
            final BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
            bitmapFont.getData().markupEnabled = true;
            resources.put("font_" + size, bitmapFont);
        }
        fontGenerator.dispose();
        //load skin
        final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("ui/hud.atlas", resources);
        assetManager.load("ui/hud.json", Skin.class, skinParameter);
        assetManager.load("ui/strings", I18NBundle.class);
        assetManager.finishLoading();
        skin = assetManager.get("ui/hud.json", Skin.class);
        i18NBundle = assetManager.get("ui/strings", I18NBundle.class);
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public ECSEngine getEcsEngine() {
        return ecsEngine;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public I18NBundle getI18NBundle() {
        return i18NBundle;
    }

    public Stage getStage() {
        return stage;
    }

    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public FitViewport getScreenViewport() {
        return screenViewport;
    }

    public World getWorld() {
        return world;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }

    public void setScreen(final ScreenType screenType) {
        final Screen screen = screenCache.get(screenType);
        if (screen == null) {
            // screen not created -> create it
            try {
                Gdx.app.debug(TAG, "Creating new screen: " + screenType);
                final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), MysticGarden.class).newInstance(this);
                screenCache.put(screenType, newScreen);
                setScreen(newScreen);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Screen " + screenType + " could not be created", e);
            }
        } else {
            Gdx.app.debug(TAG, "Switching to screen: " + screenType);
            setScreen(screen);
        }
    }

    @Override
    public void render() {
        super.render();

        final float deltaTime = Math.min(0.25f, Gdx.graphics.getDeltaTime());
        ecsEngine.update(deltaTime);
        accumulator += deltaTime;
        while (accumulator >= FIXED_TIME_STEP) {
            world.step(FIXED_TIME_STEP, 6, 2);
            accumulator -= FIXED_TIME_STEP;
        }

        gameRenderer.render(accumulator / FIXED_TIME_STEP);
        stage.getViewport().apply();
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        gameRenderer.dispose();
        world.dispose();
        assetManager.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }
}