package com.mygdx.game.helpers;

import static com.mygdx.game.helpers.Constants.CHARACTER_FLAG;
import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends ModelInstance implements Disposable{
    public final btRigidBody body;
    public final ObjMotionState motionState;
    public final btCollisionShape shape;
    private static Vector3 localInertia = new Vector3();
    public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    public final Vector3 center = new Vector3();

    public GameObject(Model m, String n, btCollisionShape s, float mass, int cflag) {
        super(m, n);
        if(s == null){
            this.shape = Bullet.obtainStaticNodeShape(m.nodes);
        } else{
            this.shape = s;
        }

        if(mass > 0f) shape.calculateLocalInertia(mass, localInertia);
        else localInertia.set(0, 0, 0);

        this.motionState = new ObjMotionState();
        this.motionState.transform = transform;

        this.constructionInfo = new btRigidBodyConstructionInfo(mass, motionState, shape, localInertia);
        this.body = new btRigidBody(constructionInfo);
        this.body.setMotionState(motionState);

        if (cflag != 0) body.setCollisionFlags(cflag);
        if(cflag == KINEMATIC_FLAG){
            body.setActivationState(Collision.DISABLE_DEACTIVATION);
        }
    }

    @Override
    public void dispose() {
        body.dispose();
        motionState.dispose();
        constructionInfo.dispose();
    }

}
