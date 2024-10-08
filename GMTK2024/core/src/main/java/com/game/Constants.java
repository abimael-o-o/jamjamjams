package com.game;

import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class Constants {
    public static final short GROUND_FLAG = 1 << 8;
    public static final short OBJECT_FLAG = 1 << 9;
    public static final short ALL_FLAG = -1;
    public static final int KINEMATIC_FLAG = btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT;
    public static final int DYNAMIC_FLAG = 0;
}
