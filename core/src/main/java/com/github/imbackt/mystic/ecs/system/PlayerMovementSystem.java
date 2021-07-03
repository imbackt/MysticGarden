package com.github.imbackt.mystic.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.imbackt.mystic.MysticGarden;
import com.github.imbackt.mystic.ecs.ECSEngine;
import com.github.imbackt.mystic.ecs.component.Box2DComponent;
import com.github.imbackt.mystic.ecs.component.PlayerComponent;
import com.github.imbackt.mystic.input.GameKeys;
import com.github.imbackt.mystic.input.InputListener;
import com.github.imbackt.mystic.input.InputManager;

public class PlayerMovementSystem extends IteratingSystem implements InputListener {
    private boolean directionChane;
    private int xFactor;
    private int yFactor;

    public PlayerMovementSystem(final MysticGarden context) {
        super(Family.all(PlayerComponent.class, Box2DComponent.class).get());
        context.getInputManager().addInputListener(this);
        directionChane = false;
        xFactor = yFactor = 0;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (directionChane) {
            final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
            final Box2DComponent box2DComponent = ECSEngine.box2DCmpMapper.get(entity);

            directionChane = false;
            box2DComponent.body.applyLinearImpulse(
                    (xFactor * playerComponent.speed.x - box2DComponent.body.getLinearVelocity().x) * box2DComponent.body.getMass(),
                    (yFactor * playerComponent.speed.y - box2DComponent.body.getLinearVelocity().y) * box2DComponent.body.getMass(),
                    box2DComponent.body.getWorldCenter().x, box2DComponent.body.getWorldCenter().y, true
            );
        }
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
