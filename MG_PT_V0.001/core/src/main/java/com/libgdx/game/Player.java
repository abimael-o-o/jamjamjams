package com.libgdx.game;
import static com.libgdx.game.helpers.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.game.helpers.Animator;
import com.libgdx.game.helpers.BodyHelper;

public class Player {
    public Sprite sprite;
    public Vector2 position;
    public float speed = 2f;
    private float stateTime; //For animations.

    boolean shouldFlip = false;

    Body body;
    public SpriteBatch batch;

    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;

    public Player(World world, Vector2 p){
        this.batch = new SpriteBatch();
        this.position = p;
        this.body = new BodyHelper().createBody(position.x, position.y, 32, 32, false, world, "Player", 100);
        body.setLinearDamping(5f);

        AnimationSetup();
        stateTime = 0f;
    }
    //Cut animation sheets into animations.
    public void AnimationSetup(){
        Texture idleTexture = new Texture("player/player-temp.png");
        Texture walkTexture = new Texture("player/player-temp.png");
        Texture upTexture = new Texture("player/player-temp.png");
        Texture downTexture = new Texture("player/player-temp.png");

        Animator anim = new Animator();
        idleAnimation = anim.AnimatorInit(idleTexture);
        walkAnimation = anim.AnimatorInit(walkTexture);
        upAnimation = anim.AnimatorInit(upTexture);
        downAnimation = anim.AnimatorInit(downTexture);
    }

    public void Update(float deltaTime){
        Flip(); //Face the corresponding direction.

        float horizontalForce = 0; //For x movement.
        float verticalForce = 0;

        //Input listener for movement
        if(Gdx.input.isKeyPressed(Keys.D)) {
            horizontalForce += speed;
        }
        if(Gdx.input.isKeyPressed(Keys.A)) {
            horizontalForce -= speed;
        }
        if(Gdx.input.isKeyPressed(Keys.W)){
            verticalForce += speed;
        }
        if(Gdx.input.isKeyPressed(Keys.S)){
            verticalForce -= speed;
        }

        //Move the player.
        body.setLinearVelocity(horizontalForce, verticalForce);
        //Set the sprite position to the physics body.
        position = new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM);

        //Set animations based on movement.
        if(body.getLinearVelocity().x != 0) animationHandler(walkAnimation, shouldFlip);
        else if(body.getLinearVelocity().y > 0 ){
            animationHandler(upAnimation, shouldFlip);
        }else if(body.getLinearVelocity().y < 0){
            animationHandler(downAnimation, shouldFlip);
        }else animationHandler(idleAnimation, shouldFlip);

        stateTime += deltaTime * .2f; //Animation speed.

        CheckDeathState();
    }

    private void Flip(){
        //* TODO FIX ISSUE FLIPPLING WHEN COLLIDING */

        // if((shouldFlip && body.getLinearVelocity().x > 0f) || (!shouldFlip && body.getLinearVelocity().x < 0f)){
        //     shouldFlip = !shouldFlip;
        // }
        shouldFlip = !(body.getLinearVelocity().x > 0.1f);
    }

    private void CheckDeathState(){
        BodyHelper ta = (BodyHelper) body.getFixtureList().first().getUserData();
        if(ta.healhtComponent.GetHealth() < 100){
        }
        if(ta.healhtComponent.DeathState()) System.out.println("DEAD");
    }

    public void Draw(SpriteBatch batch){
        this.batch = batch;
        Update(Gdx.graphics.getDeltaTime());
    }

    public void animationHandler(Animation<TextureRegion> anim, boolean isFlipx){
        TextureRegion currentFrame = anim.getKeyFrame(stateTime, true);
        sprite = new Sprite(currentFrame);
        sprite.flip(isFlipx, false);
        sprite.setPosition(position.x  - 16, position.y - 16); //Adjust sprite position.
        sprite.draw(batch);
    }

    public Vector3 GetPlayerPosition(){
        return new Vector3(position.x, position.y, 0);
    }

    public Vector2 GetPlayerPositionBody(){
        return new Vector2(position.x, position.y);
    }

    public Body GetPlayerBody(){
        return body;
    }
}
