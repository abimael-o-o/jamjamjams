package com.mygdx.game.helpers;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Utils {
    private static final Vector3 tmpVec = new Vector3();

    /**
     * Gets the urrent facing direction of transform, assuming the default forward is Z vector.
     *
     * @param transform modelInstance transform
     * @param out out vector to be populated with direction
     */
    public static void getDirection(Matrix4 transform, Vector3 out) {
        tmpVec.set(Vector3.Z);
        out.set(tmpVec.rot(transform).nor());
    }

    /**
     * Gets the world position of modelInstance and sets it on the out vector
     *
     * @param transform modelInstance transform
     * @param out out vector to be populated with position
     */
    public static void getPosition(Matrix4 transform, Vector3 out) {
        transform.getTranslation(out);
    }
}
