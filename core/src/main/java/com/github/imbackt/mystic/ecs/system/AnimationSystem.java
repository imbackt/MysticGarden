package com.github.imbackt.mystic.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.ecs.ECSEngine;
import com.github.imbackt.mystic.ecs.component.AnimationComponent;

public class AnimationSystem extends IteratingSystem {


    public AnimationSystem(final MysticGarden context) {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
        if (animationComponent.animationType != null) {
            animationComponent.animationTime += deltaTime;
        }
    }
}