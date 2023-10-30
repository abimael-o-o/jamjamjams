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
    public float speed = .20f;
    private float defaultSpeed;
    private float runSpeed = .25f;
    private float stateTime; //For animations.
    private float gravityScale = 2f; //Used for falling.

    float jumpForce = .6f; //How fast player goes up.
    boolean isJumping = false;
    float jumpStartTime = .25f;//How long can player be in air.
    float jumpTime = 0;
    boolean shouldFlip = false;

    Body body;
    Texture img;
    SpriteBatch batch;

    Animation<TextureRegion> animation;
    Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> runAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;
    
    public Player(World world, Vector2 p){
        this.batch = new SpriteBatch();
        this.position = p;
        this.body = new BodyHelper().createBody(position.x, position.y, PPM, PPM * 2 , false, world, "Player");
        body.setLinearDamping(5f);
        
        AnimationSetup();
        defaultSpeed = speed;
        stateTime = 0f;
    }
    //Cut animation sheets into animations.
    public void AnimationSetup(){
        Texture idleTexture = new Texture("player/idle-Sheet.png");
        Texture walkTexture = new Texture("player/idle-Sheet.png");
        Texture upTexture = new Texture("player/idle-Sheet.png");
        Texture downTexture = new Texture("player/idle-Sheet.png");

        Animator anim = new Animator();
        animation = anim.AnimatorInit(idleTexture);
        walkAnimation = anim.AnimatorInit(walkTexture);
        upAnimation = anim.AnimatorInit(upTexture);
        downAnimation = anim.AnimatorInit(downTexture);
    }
    
    public void Update(float deltaTime){
        //If falling add fall gravity speed.
        if(body.getLinearVelocity().y < 0){
            animationHandler(downAnimation, shouldFlip);
            body.setGravityScale(gravityScale);
        }
        
        Flip(); //Face the corresponding direction.
        //Animate character based on velocity.
        if(body.getLinearVelocity().nor().x != 0 && body.getLinearVelocity().y == 0) {
            if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
                speed = runSpeed;
                animationHandler(runAnimation, shouldFlip);
            }else {
                speed = defaultSpeed;
                animationHandler(walkAnimation, shouldFlip);
            }
        }
        if(body.getLinearVelocity().y > 0 && isJumping) animationHandler(upAnimation, shouldFlip);
        if(body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0) animationHandler(animation, shouldFlip);
        
        //Amount of time player can jump.
        jumpTime -= deltaTime;
        if(jumpTime < 0) jumpTime = 0;
        
        float horizontalForce = 0; //For x movement.
        float verticalForce = 0;
        //Input listener for movement
        if(Gdx.input.isKeyPressed(Keys.D)) {
            horizontalForce += speed;
        }
        if(Gdx.input.isKeyPressed(Keys.A)) {
            horizontalForce -= speed;
        }
        
        //Jump listener.
        if(Gdx.input.isKeyJustPressed(Keys.SPACE) && isGrounded()){
            jumpTime = jumpStartTime;
            isJumping = true;
        }
        if(Gdx.input.isKeyPressed(Keys.SPACE) && isJumping && jumpTime > 0){
            horizontalForce = horizontalForce !=0 ? horizontalForce*1.2f : horizontalForce;
            verticalForce = jumpForce;
        } else isJumping = false;
        
        //Move the player.
        body.setLinearVelocity(horizontalForce, verticalForce);
        //Set the sprite position to the physics body.
        position = new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM);
        
        body.setGravityScale(1.8f);
        stateTime += deltaTime * .2f; //Animation speed.
    }
    
    private void Flip(){
        if(shouldFlip && body.getLinearVelocity().x > 0f || !shouldFlip && body.getLinearVelocity().x < 0f){
            shouldFlip = !shouldFlip;
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
        sprite.setScale(2);
        sprite.setPosition(position.x - PPM / 2, position.y - PPM / 2);
        sprite.draw(batch);
    }

    public boolean isGrounded(){
        BodyHelper tba = (BodyHelper) body.getFixtureList().first().getUserData();
        return tba.GetGroundState();
    }

    @Override
    public void dispose(){
        img.dispose();
        batch.dispose();
    }

    public Vector3 GetPlayerPosition(){
        return new Vector3(position.x, position.y, 0);
    }
 
}
