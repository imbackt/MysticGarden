package com.github.imbackt.mystic.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.github.imbackt.mystic.MysticGarden;

import java.util.EnumMap;

import static com.github.imbackt.mystic.MysticGarden.*;
import static com.github.imbackt.mystic.MysticGarden.FIXTURE_DEF;

public class MapManager {
    public static final String TAG = MapManager.class.getSimpleName();

    private final World world;
    private final Array<Body> bodies;

    private final AssetManager assetManager;

    private MapType currentMapType;
    private Map currentMap;
    private final EnumMap<MapType, Map> mapCache;
    private final Array<MapListener> listeners;

    public MapManager(final MysticGarden context) {
        currentMapType = null;
        currentMap = null;
        world = context.getWorld();
        assetManager = context.getAssetManager();
        bodies = new Array<>();
        mapCache = new EnumMap<>(MapType.class);
        listeners = new Array<>();
    }

    public void addMapListener(final MapListener listener) {
        listeners.add(listener);
    }

    public void setMap(final MapType type) {
        if (currentMapType == type) {
            return;
        }

        if (currentMap != null) {
            world.getBodies(bodies);
            destroyCollisionAreas();
        }

        Gdx.app.debug(TAG, "Changing to map " + type);
        currentMap = mapCache.get(type);
        if (currentMap == null) {
            Gdx.app.debug(TAG, "Creating mew map of type " + type);
            final TiledMap tiledMap = assetManager.get(type.getFilePath(), TiledMap.class);
            currentMap = new Map(tiledMap);
            mapCache.put(type, currentMap);
        }

        spawnCollisionAreas();

        for (final MapListener listener : listeners) {
            listener.mapChange(currentMap);
        }
    }

    private void destroyCollisionAreas() {
        for (final Body body : bodies) {
            if ("GROUND".equals(body.getUserData())) {
                world.destroyBody(body);
            }
        }
    }

    private void spawnCollisionAreas() {
        MysticGarden.resetBodiesAndFixtureDefinition();
        for (final CollisionArea collisionArea : currentMap.getCollisionAreas()) {
            resetBodiesAndFixtureDefinition();
            BODY_DEF.position.set(collisionArea.getX(), collisionArea.getY());
            BODY_DEF.fixedRotation = true;
            final Body body = world.createBody(BODY_DEF);
            body.setUserData("GROUND");

            FIXTURE_DEF.filter.categoryBits = BIT_GROUND;
            FIXTURE_DEF.filter.maskBits = -1;
            final ChainShape chainShape = new ChainShape();
            chainShape.createChain(collisionArea.getVertices());
            FIXTURE_DEF.shape = chainShape;
            body.createFixture(FIXTURE_DEF);
            chainShape.dispose();
        }
    }

    public Map getCurrentMap() {
        return currentMap;
    }
}
