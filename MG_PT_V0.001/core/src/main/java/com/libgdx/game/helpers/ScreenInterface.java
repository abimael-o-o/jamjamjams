package com.libgdx.game.helpers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public interface ScreenInterface extends Screen
{
    World world = new World(new Vector2(0,0), false); //World y = gravity;

    public default World getWorld(){
        return world;
    }

    public default void createEnemiesInWorld(Vector2 pos, String name){}
}
