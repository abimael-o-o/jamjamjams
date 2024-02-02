package com.mygdx.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface EnemyInterface{

    public void AttackPlayer();

    public void IsPlayerClose();

    public void Draw(SpriteBatch b, Vector2 playerPos);
}
