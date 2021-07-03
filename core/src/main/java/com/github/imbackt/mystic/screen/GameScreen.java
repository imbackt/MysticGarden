package com.github.imbackt.mystic.screen;

import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.input.GameKeys;
import com.github.imbackt.mystic.input.InputManager;
import com.github.imbackt.mystic.map.Map;
import com.github.imbackt.mystic.map.MapListener;
import com.github.imbackt.mystic.map.MapManager;
import com.github.imbackt.mystic.map.MapType;
import com.github.imbackt.mystic.view.GameUI;

public class GameScreen extends AbstractScreen<GameUI> implements MapListener {
    private final MapManager mapManager;

    public GameScreen(MysticGarden context) {
        super(context);

        mapManager = context.getMapManager();
        mapManager.addMapListener(this);
        mapManager.setMap(MapType.MAP_1);

        context.getEcsEngine().createPlayer(mapManager.getCurrentMap().getPlayerStartLocation(), 0.75f, 0.75f);
    }

    @Override
    protected GameUI getScreenUI(MysticGarden context) {
        return new GameUI(context);
    }

    @Override
    public void render(float delta) {

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

    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    @Override
    public void mapChange(Map map) {

    }
}
