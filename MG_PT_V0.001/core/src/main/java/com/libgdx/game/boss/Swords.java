package com.libgdx.game.boss;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.game.helpers.BodyHelper;

import static com.libgdx.game.helpers.Constants.PPM;

public class Swords {
    public Sprite sprite;
    private Vector2 position;
    public final SpriteBatch batch;
    public final World world;

    public final Body bulletBody;
    public float speed = 2f;

    public Swords(Vector2 pos, World w, SpriteBatch b){
        this.batch =  b;
        this.world = w;
        this.position = pos;

        Texture tex = new Texture("boss/swords.png");
        sprite = new Sprite(tex);

        bulletBody = new BodyHelper().createAttackBody(position.x, position.y, 32, 32, world, "Sword");
    }

    public void Draw(){
        position = new Vector2(bulletBody.getPosition().x * PPM, bulletBody.getPosition().y * PPM);
        sprite.setPosition(position.x - 16, position.y - 16); //Adjust sprite position.
        sprite.draw(batch);
    }
}
