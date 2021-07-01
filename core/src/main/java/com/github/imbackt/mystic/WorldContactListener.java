package com.github.imbackt.mystic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Gdx.app.debug("CONTACT", "Begin: " + fixtureA.getBody().getUserData() + " " + fixtureA.isSensor());
        Gdx.app.debug("CONTACT", "Begin: " + fixtureB.getBody().getUserData() + " " + fixtureB.isSensor());
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Gdx.app.debug("CONTACT", "END: " + fixtureA.getBody().getUserData() + " " + fixtureA.isSensor());
        Gdx.app.debug("CONTACT", "END: " + fixtureB.getBody().getUserData() + " " + fixtureB.isSensor());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
