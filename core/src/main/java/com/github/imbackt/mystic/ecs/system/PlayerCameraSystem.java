package com.github.imbackt.mystic.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.ecs.ECSEngine;
import com.github.imbackt.mystic.ecs.component.Box2DComponent;
import com.github.imbackt.mystic.ecs.component.PlayerComponent;

public class PlayerCameraSystem extends IteratingSystem {
    final OrthographicCamera gameCamera;

    public PlayerCameraSystem(final MysticGarden context) {
        super(Family.all(PlayerComponent.class, Box2DComponent.class).get());
        gameCamera = context.getGameCamera();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        gameCamera.position.set(ECSEngine.box2DCmpMapper.get(entity).renderPosition, 0);
    }
}
