package com.mygdx.player;
import static com.mygdx.helper.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
import com.mygdx.helper.Animator;
import com.mygdx.helper.BodyHelper;

public class Player extends ScreenAdapter{
    public Sprite sprite;
    public Vector2 position;
    public float speed = 2f;
    //private float defaultSpeed;
    //private float runSpeed = .25f;
    private float stateTime; //For animations.

    boolean shouldFlip = false;

    Body body;
    SpriteBatch batch;

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
        Texture idleTexture = new Texture("player/idle-sheet.png");
        Texture walkTexture = new Texture("player/walk-sheet.png");
        Texture upTexture = new Texture("player/up-sheet.png");
        Texture downTexture = new Texture("player/down-sheet.png");

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
        if(shouldFlip && body.getLinearVelocity().x > 0f || !shouldFlip && body.getLinearVelocity().x < 0f){
            shouldFlip = !shouldFlip;
        }
    }

    private void CheckDeathState(){
        BodyHelper ta = (BodyHelper) body.getFixtureList().first().getUserData();
        if(ta.healhtComponent.DeathState()){
            System.out.println("Dead");
        }
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

    @Override
    public void dispose(){
        batch.dispose();
    }

    public Vector3 GetPlayerPosition(){
        return new Vector3(position.x, position.y, 0);
    }
 
}
