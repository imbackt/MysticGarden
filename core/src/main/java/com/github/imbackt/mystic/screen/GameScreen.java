package com.github.imbackt.mystic.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.input.GameKeys;
import com.github.imbackt.mystic.input.InputManager;
import com.github.imbackt.mystic.map.Map;
import com.github.imbackt.mystic.map.MapListener;
import com.github.imbackt.mystic.map.MapType;
import com.github.imbackt.mystic.ui.GameUI;

import static com.github.imbackt.mystic.MysticGarden.UNIT_SCALE;

public class GameScreen extends AbstractScreen<GameUI> implements MapListener {
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera gameCamera;
    private final GLProfiler profiler;

    public GameScreen(MysticGarden context) {
        super(context);

        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getSpriteBatch());
        gameCamera = context.getGameCamera();

        profiler = new GLProfiler(Gdx.graphics);
        //profiler.enable();

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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply(false);

        if (mapRenderer.getMap() !=null ) {
            mapRenderer.setView(gameCamera);
            mapRenderer.render();
        }

        box2DDebugRenderer.render(world, viewport.getCamera().combined);

        if (profiler.isEnabled()) {
            Gdx.app.debug("RenderInfo", "Binding: " + profiler.getTextureBindings());
            Gdx.app.debug("RenderInfo", "DrawCalls: " + profiler.getDrawCalls());
            profiler.reset();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }

    @Override
    public void keyPressed(InputManager manager, GameKeys key) {

    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {

    }

    @Override
    public void mapChange(Map map) {
        mapRenderer.setMap(map.getTiledMap());
    }
}
