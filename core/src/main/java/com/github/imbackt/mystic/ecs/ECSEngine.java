package com.github.imbackt.mystic.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.ecs.component.Box2DComponent;
import com.github.imbackt.mystic.ecs.component.PlayerComponent;
import com.github.imbackt.mystic.ecs.system.PlayerMovementSystem;

import static com.github.imbackt.mystic.MysticGarden.BIT_GROUND;
import static com.github.imbackt.mystic.MysticGarden.BIT_PLAYER;

public class ECSEngine extends PooledEngine {
    public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<Box2DComponent> box2DCmpMapper = ComponentMapper.getFor(Box2DComponent.class);

    private final World world;
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;


    public ECSEngine(final MysticGarden context) {
        super();

        world = context.getWorld();
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        this.addSystem(new PlayerMovementSystem(context));
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

    public void createPlayer(final Vector2 playerSpawnLocation, final float width, final float height) {
        final Entity player = this.createEntity();
        final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed.set(3, 3);
        player.add(playerComponent);

        resetBodiesAndFixtureDefinition();
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        bodyDef.position.set(playerSpawnLocation.x, playerSpawnLocation.y + height * 0.5f);
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        box2DComponent.body = world.createBody(bodyDef);
        box2DComponent.body.setUserData("PLAYER");
        box2DComponent.width = width;
        box2DComponent.height = height;

        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(width * 0.5f, height * 0.5f);
        fixtureDef.shape = pShape;
        box2DComponent.body.createFixture(fixtureDef);
        pShape.dispose();

        player.add(box2DComponent);
        this.addEntity(player);
    }
}
