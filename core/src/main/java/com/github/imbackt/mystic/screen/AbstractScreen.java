package com.github.imbackt.mystic.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.audio.AudioManager;
import com.github.imbackt.mystic.input.InputListener;
import com.github.imbackt.mystic.input.InputManager;

public abstract class AbstractScreen<T extends Table> implements Screen, InputListener {
    protected final MysticGarden context;
    protected final FitViewport viewport;
    protected final World world;
    protected final Stage stage;
    protected final T screenUI;
    protected final InputManager inputManager;
    protected final AudioManager audioManager;

    public AbstractScreen(MysticGarden context) {
        this.context = context;
        this.viewport = context.getScreenViewport();
        this.world = context.getWorld();
        this.inputManager = context.getInputManager();
        this.audioManager = context.getAudioManager();

        stage = context.getStage();
        screenUI = getScreenUI(context);
    }

    protected abstract T getScreenUI(final MysticGarden context);

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        inputManager.addInputListener(this);
        stage.addActor(screenUI);
    }

    @Override
    public void hide() {
        inputManager.removeListener(this);
        stage.getRoot().removeActor(screenUI);
    }
}
