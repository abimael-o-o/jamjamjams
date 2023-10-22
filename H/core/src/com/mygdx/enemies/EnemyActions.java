package com.mygdx.enemies;
import static com.mygdx.helper.Constants.PPM;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.helper.BodyHelper;

public class EnemyActions implements EnemyInterface{

    public Sprite sprite;
    public Vector2 position;
    public float speed = .2f;

    private Body enemyBody;
    private SpriteBatch batch;
    Texture img;

    public EnemyActions(Vector2 pos, World world){
        this.batch = new SpriteBatch();
        this.position = pos;
        this.enemyBody = new BodyHelper().createBody(position.x, position.y, 32, 32, false, world, "Enemy", 50);
        this.batch = new SpriteBatch();

        img = new Texture("enemies/enemy00.png");
        sprite = new Sprite(img);
    }

    public void Draw(SpriteBatch batch){
        this.batch = batch;
        position = new Vector2(enemyBody.getPosition().x * PPM, enemyBody.getPosition().y * PPM);
        enemyBody.setLinearVelocity(.01f, 0);
        sprite.setPosition(position.x - 16, position.y - 16); //Adjust sprite position.d
        sprite.draw(batch);
    }

    @Override
    public void AttackPlayer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'AttackPlayer'");
    }

    @Override
    public void IsPlayerClose() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CheckIfPlayerClose'");
    }
    
}
