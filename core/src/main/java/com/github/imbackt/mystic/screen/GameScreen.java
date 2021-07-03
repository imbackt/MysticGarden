package com.github.imbackt.mystic.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.input.GameKeys;
import com.github.imbackt.mystic.input.InputManager;
import com.github.imbackt.mystic.map.CollisionArea;
import com.github.imbackt.mystic.map.Map;
import com.github.imbackt.mystic.ui.GameUI;

import static com.github.imbackt.mystic.MysticGarden.*;

public class GameScreen extends AbstractScreen<GameUI> {
    private final AssetManager assetManager;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera gameCamera;
    private final Map map;
    private final GLProfiler profiler;

    public GameScreen(MysticGarden context) {
        super(context);

        assetManager = context.getAssetManager();
        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getSpriteBatch());
        gameCamera = context.getGameCamera();

        profiler = new GLProfiler(Gdx.graphics);
        //profiler.enable();

        final TiledMap tiledMap = assetManager.get("map/map.tmx", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);

        spawnCollisionAreas();
        context.getEcsEngine().createPlayer(map.getPlayerStartLocation(), 1, 1);
    }

    @Override
    protected GameUI getScreenUI(MysticGarden context) {
        return new GameUI(context);
    }

    private void spawnCollisionAreas() {
        final BodyDef bodyDef = new BodyDef();
        final FixtureDef fixtureDef = new FixtureDef();
        for (final CollisionArea collisionArea : map.getCollisionAreas()) {
            bodyDef.position.set(collisionArea.getX(), collisionArea.getY());
            bodyDef.fixedRotation = true;
            final Body body = world.createBody(bodyDef);
            body.setUserData("GROUND");

            fixtureDef.filter.categoryBits = BIT_GROUND;
            fixtureDef.filter.maskBits = -1;
            final ChainShape chainShape = new ChainShape();
            chainShape.createChain(collisionArea.getVertices());
            fixtureDef.shape = chainShape;
            body.createFixture(fixtureDef);
            chainShape.dispose();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        mapRenderer.setView(gameCamera);
        mapRenderer.render();
        box2DDebugRenderer.render(world, viewport.getCamera().combined);
        /*Gdx.app.debug("RenderInfo", "Binding: " + profiler.getTextureBindings());
        Gdx.app.debug("RenderInfo", "DrawCalls: " + profiler.getDrawCalls());*/
        profiler.reset();
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
}
