package com.github.imbackt.mystic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.imbackt.mystic.screen.ScreenType;

import java.util.EnumMap;

public class MysticGarden extends Game {
    private static final String TAG = MysticGarden.class.getSimpleName();

    private EnumMap<ScreenType, Screen> screenCache;
    private FitViewport screenViewport;


    public static final short BIT_CIRCLE = 1 << 0;
    public static final short BIT_BOX = 1 << 1;
    public static final short BIT_GROUND = 1 << 2;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private static final float FIXED_TIME_STEP = 1 / 60f;
    private float accumulator;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        accumulator = 0f;

        Box2D.init();
        world = new World(new Vector2(0, -9.81f), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        screenViewport = new FitViewport(9, 16);
        screenCache = new EnumMap<>(ScreenType.class);
        setScreen(ScreenType.GAME);
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
                throw new GdxRuntimeException("Screen " + screenType + "could not be created", e);
            }
        } else {
            Gdx.app.debug(TAG, "Switching to screen: " + screenType);
            setScreen(screen);
        }
    }

    @Override
    public void render() {
        super.render();

        //Gdx.app.debug(TAG, "" + Gdx.graphics.getDeltaTime());
        accumulator += Gdx.graphics.getDeltaTime();
        while (accumulator >= FIXED_TIME_STEP) {
            world.step(FIXED_TIME_STEP, 6, 2);
            accumulator -= FIXED_TIME_STEP;
        }

        //final float alpha = accumulator / FIXED_TIME_STEP;
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
    }
}