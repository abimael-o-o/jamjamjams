package com.mygdx.helper;
import static com.mygdx.helper.Constants.PPM;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyHelper {
    private boolean groundFixtureState = true;
    private String ID;
    
    public Body createBody(float x, float y, float width, float height, boolean isStatic, World world, String id){
        this.ID = id;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
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

    public void SetGroundFixture(boolean state){
        groundFixtureState = state;
    }
    public boolean GetGroundState(){
        return groundFixtureState;
    }
    public String getBodyID(){
        if(ID == null) return "No ID";
        else return ID;
    }
}
