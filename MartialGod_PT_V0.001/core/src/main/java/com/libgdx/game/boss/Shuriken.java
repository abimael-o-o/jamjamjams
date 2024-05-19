package com.libgdx.game.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.libgdx.game.helpers.BodyHelper;

import static com.libgdx.game.helpers.Constants.PPM;

public class Shuriken implements Disposable {
    public Sprite sprite;
    private Vector2 position;
    private final Vector2 finalPosition;
    public final SpriteBatch batch;
    public final World world;

    public final Body bulletBody;
    public float speed = 4f;

    public Shuriken(Vector2 startPos, Vector2 finalPos, SpriteBatch b, World w){
        this.position = startPos;
        this.finalPosition = finalPos;
        this.batch = b;
        this.world = w;

        Texture tex = new Texture("boss/bullet.png");
        sprite = new Sprite(tex);

        bulletBody = new BodyHelper().createAttackBody(position.x, position.y, 16, 16, world, "Bullet");
    }

    private void Update(float delta){
        position = new Vector2(bulletBody.getPosition().x * PPM, bulletBody.getPosition().y * PPM);

        Vector2 direction = finalPosition.cpy().sub(position);
        direction.nor();
        direction.x += direction.x * speed;
        direction.y += direction.y * speed;

        bulletBody.setLinearVelocity(direction); //Move only when in range.
        SpriteHandle();
    }

    public void Draw(){
        Update(Gdx.graphics.getDeltaTime());
    }

    private void SpriteHandle(){
        sprite.setPosition(position.x - 8, position.y - 8); //Adjust sprite position.
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
    }
}
