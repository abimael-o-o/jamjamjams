package com.game;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

public class ObjMotionState extends btMotionState{
    Matrix4 transform;
    
    @Override
    public void getWorldTransform(Matrix4 worldTrans){
        worldTrans.set(transform);
    }
    
    @Override
    public void setWorldTransform(Matrix4 worldTrans){
        transform.set(worldTrans);
    }
}
