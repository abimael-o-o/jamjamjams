package com.libgdx.game.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.libgdx.game.helpers.Animator;
import com.libgdx.game.helpers.BodyHelper;
import com.libgdx.game.helpers.TileMapHelper;

import java.util.Random;

import static com.libgdx.game.helpers.Constants.PPM;

public class Boss {

    public Sprite sprite;
    public Vector2 position;
    public float speed = 5f;
    private float stateTime; //For animations.

    boolean shouldFlip = false;
    private float attackTimer = 2f;
    private float moveTimer = 10f;
    private boolean isAttacking = false;
    private final Body body;
    public final SpriteBatch batch;

    Animation<TextureRegion> idleAnimation;


    private final Array<Vector2> bAPos;
    private final Array<Vector2> bBPos;
    private final Array<Vector2> bossPositions;
    private final Array<Shuriken> bulletsA = new Array<>(8);
    Vector2 direction = new Vector2();
    Random random = new Random();
    int posIndex;
    public final World world;

    public Boss (World w, Vector2 pos, TileMapHelper mapHelper, SpriteBatch b){
        this.batch = b;
        this.world = w;
        this.position = pos;
        this.body = new BodyHelper().createBody(position.x, position.y, 64, 64, true, world, "Boss", 100);
        body.setLinearDamping(5f);

        //Parse bullet spawns
        bAPos = mapHelper.parseSpawns("BulletsA");
        bBPos = mapHelper.parseSpawns("BulletsB");
        bossPositions = mapHelper.parseSpawns("BossPositions");

        AnimationSetup();
        stateTime = 0f;
    }

    private void AnimationSetup() {
        Texture idleTexture = new Texture("boss/boss-temp.png");

        Animator anim = new Animator();
        idleAnimation = anim.AnimatorInit(idleTexture);
    }

    public void Update(float delta){
        attackTimer -= delta;
        moveTimer -= delta;
        position = new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM);

        if (attackTimer <= 0.1f) {
            for (Shuriken e : bulletsA) {
                world.destroyBody(e.bulletBody);
            }
            bulletsA.clear();

            AttackHandler();
            attackTimer = 2f;
        }

        //Boss change positions
        direction = bossPositions.get(posIndex).cpy().sub(position);
        if (moveTimer <= 0.1f){
            //Update boss positions
            posIndex = random.nextInt(5);
            direction.nor(); //when this is in if statement move goes too fast, out is too slow
            direction.x += direction.x * speed;
            direction.y += direction.y * speed;

            moveTimer = 10f;
        }
        body.setLinearVelocity(direction);

        //Animate
        animationHandler(idleAnimation, false);
        stateTime += delta * .2f; //Animation speed
    }

    private void AttackHandler() {
        for (int i = 0; i < bAPos.size; i++){
            Shuriken s = new Shuriken(position, bAPos.get(i), batch, world);
            bulletsA.add(s);
        }

        System.out.println(bulletsA.size);

    }

    public void Draw(){
        Update(Gdx.graphics.getDeltaTime());
        for (Shuriken sk: bulletsA){
            sk.Draw();
        }
    }

    public void animationHandler(Animation<TextureRegion> anim, boolean isFlipx){
        TextureRegion currentFrame = anim.getKeyFrame(stateTime, true);
        sprite = new Sprite(currentFrame);
        sprite.flip(isFlipx, false);
        sprite.setPosition(position.x  - 32, position.y - 32); //Adjust sprite position.
        sprite.draw(batch);
    }
}
