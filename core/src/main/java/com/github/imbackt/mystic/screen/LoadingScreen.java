package com.github.imbackt.mystic.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.ui.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI> {
    private final AssetManager assetManager;

    public LoadingScreen(MysticGarden context) {
        super(context);

        this.assetManager = context.getAssetManager();
        assetManager.load("map/map.tmx", TiledMap.class);

    }

    @Override
    protected LoadingUI getScreenUI(MysticGarden context) {
        return new LoadingUI(context);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (assetManager.update()) {
            if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) {
                context.setScreen(ScreenType.GAME);
            }
        }
        screenUI.setProgress(assetManager.getProgress());
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
}