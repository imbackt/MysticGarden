package com.github.imbackt.mystic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.github.imbackt.mystic.screen.ScreenType;

import java.util.EnumMap;

public class MysticGarden extends Game {
    private static final String TAG = MysticGarden.class.getSimpleName();

    private EnumMap<ScreenType, Screen> screenCache;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        screenCache = new EnumMap<>(ScreenType.class);
        setScreen(ScreenType.LOADING);
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
}