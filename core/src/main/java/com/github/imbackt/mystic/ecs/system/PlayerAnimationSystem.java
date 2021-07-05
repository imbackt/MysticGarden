package com.github.imbackt.mystic.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.ecs.ECSEngine;
import com.github.imbackt.mystic.ecs.component.AnimationComponent;
import com.github.imbackt.mystic.ecs.component.Box2DComponent;
import com.github.imbackt.mystic.ecs.component.PlayerComponent;
import com.github.imbackt.mystic.view.AnimationType;

public class PlayerAnimationSystem extends IteratingSystem {


    public PlayerAnimationSystem(final MysticGarden context) {
        super(Family.all(AnimationComponent.class, PlayerComponent.class, Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final Box2DComponent box2DComponent = ECSEngine.box2DCmpMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
        if (box2DComponent.body.getLinearVelocity().equals(Vector2.Zero)) {
            // Player does not move
            animationComponent.animationTime = 0;
        } else if (box2DComponent.body.getLinearVelocity().x > 0) {
            // Player move to the right
            animationComponent.animationType = AnimationType.HERO_MOVE_RIGHT;
        } else if (box2DComponent.body.getLinearVelocity().x < 0) {
            // Player move to the left
            animationComponent.animationType = AnimationType.HERO_MOVE_LEFT;
        } else if (box2DComponent.body.getLinearVelocity().y > 0) {
            // Player moves up
            animationComponent.animationType = AnimationType.HERO_MOVE_UP;
        } else if (box2DComponent.body.getLinearVelocity().y < 0) {
            // Player moves down
            animationComponent.animationType = AnimationType.HERO_MOVE_DOWN;
        }
    }
}