package com.mygdx.enemy;
import static com.mygdx.helper.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.helper.BodyHelper;

public class Enemy extends ScreenAdapter{
    Body body;
    Texture img;
    SpriteBatch batch;
    Vector2 position;
    Sprite sprite;
    boolean shouldFlip = false;

    public Enemy(World world, Vector2 p){
        this.batch = new SpriteBatch();
        this.position = p;
        Texture texture = new Texture("assets/enemy/enemy-Sheet.png");
        sprite = new Sprite(texture);
        this.body = new BodyHelper().createBody(position.x, position.y, PPM, PPM * 2 , false, world, "Player");
        body.setLinearDamping(5f);
    }

    private void Flip(){
        if(shouldFlip && body.getLinearVelocity().x > 0f || !shouldFlip && body.getLinearVelocity().x < 0f){
            shouldFlip = !shouldFlip;
        }
    }

    public void Update(float delta){
        Flip();
    }

    public void Draw(SpriteBatch batch){
        this.batch = batch;
        Update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose(){
        img.dispose();
        batch.dispose();
    }
}
