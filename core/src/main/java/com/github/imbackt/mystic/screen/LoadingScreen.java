package com.github.imbackt.mystic.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.audio.AudioType;
import com.github.imbackt.mystic.input.GameKeys;
import com.github.imbackt.mystic.input.InputManager;
import com.github.imbackt.mystic.ui.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI> {
    private final AssetManager assetManager;
    private boolean isMusicLoaded;

    public LoadingScreen(MysticGarden context) {
        super(context);

        this.assetManager = context.getAssetManager();
        assetManager.load("map/map.tmx", TiledMap.class);

        isMusicLoaded = false;
        for (final AudioType audioType : AudioType.values()) {
            if (audioType.isMusic()) {
                assetManager.load(audioType.getFilePath(), Music.class);
            } else {
                assetManager.load(audioType.getFilePath(), Sound.class);
            }
        }
    }

    @Override
    protected LoadingUI getScreenUI(MysticGarden context) {
        return new LoadingUI(context);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        assetManager.update();
        if (!isMusicLoaded && assetManager.isLoaded(AudioType.INTRO.getFilePath())) {
            isMusicLoaded = true;
            audioManager.playAudio(AudioType.INTRO);
        }
        screenUI.setProgress(assetManager.getProgress());
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
        audioManager.stopCurrentMusic();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {
        if (assetManager.getProgress() >= 1) {
            audioManager.playAudio(AudioType.SELECT);
            context.setScreen(ScreenType.GAME);
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }
}