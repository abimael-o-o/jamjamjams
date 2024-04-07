package com.mygdx.game.helpers;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.LinearMathJNI;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;

public class ObjMotionState extends btMotionState{
    public btTransform bT;
    public Matrix4 transform;

	@Override
	public void getWorldTransform (Matrix4 worldTrans) {
		worldTrans.set(transform);
	}
	@Override
	public void setWorldTransform (Matrix4 worldTrans) {
		transform.set(worldTrans);
	}

}
