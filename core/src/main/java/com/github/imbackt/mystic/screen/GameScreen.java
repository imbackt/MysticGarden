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
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;

    private Body player;

    private boolean directionChane;
    private int xFactor;
    private int yFactor;

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
        profiler.enable();

        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        final TiledMap tiledMap = assetManager.get("map/map.tmx", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        map = new Map(tiledMap);

        spawnCollisionAreas();
        spawnPlayer();
    }

    @Override
    protected GameUI getScreenUI(MysticGarden context) {
        return new GameUI(context);
    }

    private void resetBodiesAndFixtureDefinition() {
        bodyDef.position.set(0, 0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = false;

        fixtureDef.density = 0;
        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.shape = null;
    }

    private void spawnPlayer() {
        resetBodiesAndFixtureDefinition();

        bodyDef.position.set(map.getPlayerStartLocation());
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        player = world.createBody(bodyDef);
        player.setUserData("PLAYER");

        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = pShape;
        player.createFixture(fixtureDef);
        pShape.dispose();
    }

    private void spawnCollisionAreas() {

        for (final CollisionArea collisionArea : map.getCollisionAreas()) {
            resetBodiesAndFixtureDefinition();

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

        if (directionChane) {
            player.applyLinearImpulse(
                    (xFactor * 3 - player.getLinearVelocity().x) * player.getMass(),
                    (yFactor * 3 - player.getLinearVelocity().y) * player.getMass(),
                    player.getWorldCenter().x, player.getWorldCenter().y, true
            );
        }

        viewport.apply(true);
        mapRenderer.setView(gameCamera);
        mapRenderer.render();
        box2DDebugRenderer.render(world, viewport.getCamera().combined);
        //Gdx.app.debug("RenderInfo", "Binding: " + profiler.getTextureBindings());
        //Gdx.app.debug("RenderInfo", "DrawCalls: " + profiler.getDrawCalls());
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
        switch (key) {
            case LEFT:
                directionChane = true;
                xFactor = -1;
                break;
            case RIGHT:
                directionChane = true;
                xFactor = 1;
                break;
            case UP:
                directionChane = true;
                yFactor = 1;
                break;
            case DOWN:
                directionChane = true;
                yFactor = -1;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys key) {
        switch (key) {
            case LEFT:
                directionChane = true;
                xFactor = manager.isKeyPressed(GameKeys.RIGHT) ? 1 : 0;
                break;
            case RIGHT:
                directionChane = true;
                xFactor = manager.isKeyPressed(GameKeys.LEFT) ? -1 : 0;
                break;
            case UP:
                directionChane = true;
                yFactor = manager.isKeyPressed(GameKeys.DOWN) ? -1 : 0;
                break;
            case DOWN:
                directionChane = true;
                yFactor = manager.isKeyPressed(GameKeys.UP) ? 1 : 0;
                break;
            default:
                break;
        }
    }
}
