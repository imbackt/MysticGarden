package com.github.imbackt.mystic.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.ecs.component.AnimationComponent;
import com.github.imbackt.mystic.ecs.component.Box2DComponent;
import com.github.imbackt.mystic.ecs.component.PlayerComponent;
import com.github.imbackt.mystic.ecs.system.AnimationSystem;
import com.github.imbackt.mystic.ecs.system.PlayerAnimationSystem;
import com.github.imbackt.mystic.ecs.system.PlayerCameraSystem;
import com.github.imbackt.mystic.ecs.system.PlayerMovementSystem;
import com.github.imbackt.mystic.view.AnimationType;

import static com.github.imbackt.mystic.MysticGarden.*;

public class ECSEngine extends PooledEngine {
    public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<Box2DComponent> box2DCmpMapper = ComponentMapper.getFor(Box2DComponent.class);
    public static final ComponentMapper<AnimationComponent> aniCmpMapper = ComponentMapper.getFor(AnimationComponent.class);

    private final World world;


    public ECSEngine(final MysticGarden context) {
        super();

        world = context.getWorld();

        this.addSystem(new PlayerMovementSystem(context));
        this.addSystem(new PlayerCameraSystem(context));
        this.addSystem(new AnimationSystem(context));
        this.addSystem(new PlayerAnimationSystem(context));
    }

    public void createPlayer(final Vector2 playerSpawnLocation, final float width, final float height) {
        final Entity player = this.createEntity();
        final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed.set(3, 3);
        player.add(playerComponent);

        resetBodiesAndFixtureDefinition();
        final Box2DComponent box2DComponent = this.createComponent(Box2DComponent.class);
        BODY_DEF.position.set(playerSpawnLocation.x, playerSpawnLocation.y + height * 0.5f);
        BODY_DEF.fixedRotation = true;
        BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        box2DComponent.body = world.createBody(BODY_DEF);
        box2DComponent.body.setUserData("PLAYER");
        box2DComponent.width = width;
        box2DComponent.height = height;
        box2DComponent.renderPosition.set(box2DComponent.body.getPosition());

        FIXTURE_DEF.filter.categoryBits = BIT_PLAYER;
        FIXTURE_DEF.filter.maskBits = BIT_GROUND;
        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(width * 0.5f, height * 0.5f);
        FIXTURE_DEF.shape = pShape;
        box2DComponent.body.createFixture(FIXTURE_DEF);
        pShape.dispose();
        player.add(box2DComponent);

        final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
        animationComponent.animationType = AnimationType.HERO_MOVE_DOWN;
        animationComponent.width = 64 * UNIT_SCALE * 0.75f;
        animationComponent.height = 64 * UNIT_SCALE * 0.75f;
        player.add(animationComponent);

        this.addEntity(player);
    }
}
