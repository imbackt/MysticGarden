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
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.imbackt.mystic.audio.AudioManager;
import com.github.imbackt.mystic.input.InputManager;
import com.github.imbackt.mystic.screen.ScreenType;

import java.util.EnumMap;

public class MysticGarden extends Game {
    private static final String TAG = MysticGarden.class.getSimpleName();

    private SpriteBatch spriteBatch;
    private EnumMap<ScreenType, Screen> screenCache;
    private OrthographicCamera gameCamera;
    private FitViewport screenViewport;

    public static final float UNIT_SCALE = 1 / 32f;
    public static final short BIT_PLAYER = 1 << 0;
    public static final short BIT_GROUND = 1 << 2;
    private World world;
    private WorldContactListener worldContactListener;
    private Box2DDebugRenderer box2DDebugRenderer;

    private static final float FIXED_TIME_STEP = 1 / 60f;
    private float accumulator;

    private AssetManager assetManager;
    private AudioManager audioManager;
    private Stage stage;
    private Skin skin;
    private I18NBundle i18NBundle;

    private InputManager inputManager;

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
        box2DDebugRenderer = new Box2DDebugRenderer();

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

        // set loading screen
        gameCamera = new OrthographicCamera();
        screenViewport = new FitViewport(9, 16, gameCamera);
        screenCache = new EnumMap<>(ScreenType.class);
        setScreen(ScreenType.LOADING);

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

    public Box2DDebugRenderer getBox2DDebugRenderer() {
        return box2DDebugRenderer;
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

        accumulator += Gdx.graphics.getDeltaTime();
        while (accumulator >= FIXED_TIME_STEP) {
            world.step(FIXED_TIME_STEP, 6, 2);
            accumulator -= FIXED_TIME_STEP;
        }

        //final float alpha = accumulator / FIXED_TIME_STEP;
        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
        assetManager.dispose();
        spriteBatch.dispose();
        stage.dispose();
    }
}