package com.github.imbackt.mystic.screen;

import com.badlogic.gdx.Screen;

public enum ScreenType {
    GAME(GameScreen.class),
    LOADING(LoadingScreen.class);

    private final Class<? extends Screen> screenClass;

    ScreenType(Class<? extends Screen> screenClass) {
        this.screenClass = screenClass;
    }

    public Class<? extends Screen> getScreenClass() {
        return screenClass;
    }
}
