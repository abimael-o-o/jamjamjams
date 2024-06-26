package com.libgdx.game.helpers;
import static com.libgdx.game.helpers.Constants.PPM;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyHelper {

    public Health healhtComponent;

    private String ID;
    private boolean isContact;
    private BodyHelper contactEntity;

    public Body createBody(float x, float y, float width, float height, boolean isStatic, World world, String id, int healthPoints){
        this.ID = id;
        this.healhtComponent = new Health(healthPoints);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.KinematicBody : BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 /PPM, height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = .1f;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        return body;
    }

    public Body createAttackBody(float x, float y, float width, float height, World world, String id){
        this.ID = id;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 /PPM, height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = .1f;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        return body;
    }

    public void SetContactStateWithEntity(boolean t, BodyHelper entity){
        isContact = t;
        if(entity != null)  contactEntity = entity;
    }

    public BodyHelper GetContactEntity(){
        return contactEntity;
    }

    public boolean ContactState(){
        return isContact;
    }

    //An entity is giving damage to this body. Code should be on that entity.
    public void BodyGiveDamage(int amount){
        healhtComponent.TakeDamage(amount);
    }

    public String getBodyID(){
        if(ID == null) return "No-ID";
        else return ID;
    }
}
