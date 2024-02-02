package com.mygdx.enemies;

import static com.mygdx.helper.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.helper.Animator;
import com.mygdx.helper.BodyHelper;

public class EnemyA implements EnemyInterface{
    public SpriteBatch batch;
    public Vector2 position;
    public Body body;
    public Body _attackBody;

    private int radius = 120;
    private float speed = .1f;
    private boolean canAttack = false;
    private Sprite sprite;
    private float stateTime; //For animations.

    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> attackAnimation;

    public EnemyA(World world, Vector2 p){
        position = p;
        body = new BodyHelper().createBody(position.x, position.y, 
                32, 32, false, world, "Enemy", 50);
        _attackBody = new BodyHelper().createAttackBody(
                position.x + 32, position.y, 32, 32, world, "Attack");
        _attackBody.setActive(false);

        AnimationSetup();
        stateTime = 0f;
    }

    //Cut animation sheets into animations.
    public void AnimationSetup(){
        Texture idleTexture = new Texture("enemies/EnemyA/Idle.png");
        Texture attackTexture = new Texture("enemies/EnemyA/Attack.png");

        Animator anim = new Animator();
        idleAnimation = anim.AnimatorInit(idleTexture);
        attackAnimation = anim.AnimatorInit(attackTexture);
    }

    private int calculateDistance(Vector2 a, Vector2 b){
        int ac = (int) Math.abs(b.y - a.y);
        int cb = (int) Math.abs(b.x - a.x);
        return (int) Math.hypot(ac, cb);
    }

    public void Update(){
        //Sprite render.
        position = new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM);
        _attackBody.setTransform(new Vector2(position.x + 32, position.y), 0f);
        if (canAttack) AttackPlayer();
        else animationHandler(idleAnimation, false, true);
        stateTime += Gdx.graphics.getDeltaTime() * .2f; //Animation speed.
    }
    
    public void animationHandler(Animation<TextureRegion> anim, boolean isFlipx, boolean loop){
        TextureRegion currentFrame = anim.getKeyFrame(stateTime, loop);
        sprite = new Sprite(currentFrame);
        sprite.flip(isFlipx, false);
        sprite.setPosition(position.x  - 16, position.y - 16); //Adjust sprite position.
        sprite.draw(batch);
    }

    @Override
    public void Draw(SpriteBatch b, Vector2 playerPos){
        this.batch = b;

        int distanceToPlayer = calculateDistance(playerPos, position);
        if(distanceToPlayer <= radius && distanceToPlayer >= 32){
            Vector2 direction = playerPos.sub(position);
            direction.nor();
            direction.x += direction.x * speed;
            direction.y += direction.y * speed;

            body.setLinearVelocity(direction); //Move only when in range.
            if(distanceToPlayer == 42) canAttack = true;

        } else body.setLinearVelocity(new Vector2(0,0)); //Don't move at all.

        Update();
    }

    @Override
    public void AttackPlayer() {
        BodyHelper bHelper = (BodyHelper) body.getFixtureList().first().getUserData();
        if(bHelper.ContactState() && bHelper.GetContactEntity().getBodyID() == "Player"){
            animationHandler(attackAnimation, false, false);
        }
    }

    @Override
    public void IsPlayerClose() {
    }

}
