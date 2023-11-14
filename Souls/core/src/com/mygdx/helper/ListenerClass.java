package com.mygdx.helper;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ListenerClass implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Fixture fb = contact.getFixtureA();
        Fixture fa = contact.getFixtureB(); 

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        //Collision between two bodies. NOT including walls and floor / misc items.
        if(fa.getUserData() instanceof BodyHelper && fb.getUserData() instanceof BodyHelper){
            BodyHelper tbb = (BodyHelper) fa.getUserData();
            BodyHelper tba = (BodyHelper) fb.getUserData();
            tba.SetContactStateWithEntity(true, tbb);
            tbb.SetContactStateWithEntity(true, tba);
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        //Collision between two bodies. NOT including walls and floor / misc items.
        if(fa.getUserData() instanceof BodyHelper && fb.getUserData() instanceof BodyHelper){
            BodyHelper tbb = (BodyHelper) fa.getUserData();
            BodyHelper tba = (BodyHelper) fb.getUserData();
            tba.SetContactStateWithEntity(false, null);
            tbb.SetContactStateWithEntity(false, null);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    
}
