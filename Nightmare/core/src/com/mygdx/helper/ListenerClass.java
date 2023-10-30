package com.mygdx.helper;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ListenerClass implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB(); //This is the player fixture.

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        //Do things here when colliding with the tiled map objects / colliders.
        if(fa.getUserData() instanceof MapBodyDefiner){
            MapBodyDefiner tbb = (MapBodyDefiner) fa.getUserData();
            if(tbb.getBodyID().equals("Ground")){
                if(fb.getUserData() instanceof BodyHelper){
                    BodyHelper tba = (BodyHelper) fb.getUserData();
                    tba.SetGroundFixture(true);
                }
            }
        }

        //Do stuff here for interactable objects

        //Do Stuff here for enemies.
        
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        //Do things here when ending collision with the tiled map objects / colliders.
        if(fa.getUserData() instanceof MapBodyDefiner){
            MapBodyDefiner tbb = (MapBodyDefiner) fa.getUserData();
            if(tbb.getBodyID().equals("Ground")){
                if(fb.getUserData() instanceof BodyHelper){
                    BodyHelper tba = (BodyHelper) fb.getUserData();
                    tba.SetGroundFixture(false);
                }
            }
        }

        //Do stuff here for interactable objects

        //Do Stuff here for enemies.
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    
}
