package com.mygdx.game.helpers;

import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends ModelInstance implements Disposable{
    public final btRigidBody body;
    public final ObjMotionState motionState;
    public final btCollisionShape shape;
    private static Vector3 localInertia = new Vector3();
    public final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    BoundingBox boundingBox;

    public GameObject(Model m, String n, btCollisionShape s, float mass, Vector3 origin, int cflag) {
        super(m, n);
        if(s == null){
            this.shape = Bullet.obtainStaticNodeShape(m.nodes);
        } else this.shape = s;
        if(mass > 0f) shape.calculateLocalInertia(mass, localInertia);
        else localInertia.set(0, 0, 0);
        
        this.constructionInfo = new btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        this.body = new btRigidBody(constructionInfo);
        this.motionState = new ObjMotionState();
        motionState.transform = transform;
        body.setMotionState(motionState);
        BoundingBox bb = new BoundingBox();
        model.calculateBoundingBox(bb);
        boundingBox = bb;

        Vector3 v = new Vector3();
        bb.getCenter(v);
        body.translate(v);

        body.setCollisionFlags(body.getCollisionFlags() | cflag);
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
    
    public BoundingBox getBounds() {
        return boundingBox.mul(transform);
    }
}
