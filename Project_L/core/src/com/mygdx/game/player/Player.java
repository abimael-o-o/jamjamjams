package com.mygdx.game.player;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestNotMeRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.game.Main;
import com.mygdx.game.helpers.GameObject;
import com.mygdx.game.helpers.Utils;

public class Player extends InputAdapter{

    //Player
    private final GameObject playerObject;
    protected final IntIntMap keys = new IntIntMap();
	public int strafeLeftKey = Keys.A;
	public int strafeRightKey = Keys.D;
	public int forwardKey = Keys.W;
	public int backwardKey = Keys.S;
    public boolean autoUpdate = true;
    protected float velocity = 5f;
    private final Vector3 movement = new Vector3(0,0,0);
    private final Vector3 position = new Vector3();
    private final Vector3 normal = new Vector3();
    private final Vector3 tmpPosition = new Vector3();
    private final Vector3 linearVelocity = new Vector3();
    private final Vector3 angularVelocity = new Vector3();


    //Camera
    Camera camera;
    float rotateSpeed = 0.2f;
	private int dragX, dragY;
    private final ClosestNotMeRayResultCallback callback;
    private final Vector3 cameraOffset = new Vector3(0, 5, 20); // Adjust camera position relative to player
    private final Main mainPhysicsSystem;

    public Player(Camera cam, Main mps){
        this.camera = cam;
        this.mainPhysicsSystem = mps;
        @SuppressWarnings("unused")
        ModelBuilder modelBuilder = new ModelBuilder();
        Model playerModel = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("Player/Player.g3dj"));
        playerObject = new GameObject(playerModel, "Player", new btSphereShape(1f), 2f, 0);
        playerObject.body.setAngularFactor(Vector3.Y);
        playerObject.body.setDamping(0.75f, 0.99f);


        camera.position.set(playerObject.center.cpy().add(cameraOffset));
        camera.lookAt(playerObject.center);
        camera.update();

        callback = new ClosestNotMeRayResultCallback(playerObject.body);
    }

    @Override
	public boolean keyDown (int keycode) {
		keys.put(keycode, keycode);
		return true;
	}

	@Override
	public boolean keyUp (int keycode) {
		keys.remove(keycode, 0);
		return true;
	}
    /** Sets the velocity in units per second for moving forward, backward and strafing left/right.
	 * @param velocity the velocity in units per second */
	public void setVelocity (float velocity) {
		this.velocity = velocity;
	}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Handle mouse button click
        // For example, you can use this method to shoot or perform other actions
        if (button == Input.Buttons.LEFT) {
            // Handle left mouse button click
        } else if (button == Input.Buttons.RIGHT) {
            // Handle right mouse button click
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // rotating on the y-axis
        float x = dragX -screenX;
        camera.rotate(Vector3.Y, x * rotateSpeed);
        //playerObject.transform.rotate(Vector3.Y, direction.y);

        // rotating on the x and z axis is different
        float y = (float) Math.sin( (double)(dragY -screenY)/180f);
        if (Math.abs(camera.direction.y + y * (rotateSpeed*5.0f))< 0.9) {
            camera.direction.y +=  y * (rotateSpeed*5.0f) ;
        }

        camera.update();
        dragX = screenX;
        dragY = screenY;
        return true;
    }
    
    public void Update(float deltaTime) {
        float speed = velocity;
        boolean isOnGround = isGrounded();

        // A slightly hacky work around to allow climbing up and preventing sliding down slopes
        if (isOnGround) {
            callback.getHitNormalWorld(normal);

            // dot product returns 1 if same direction, -1 if opposite direction, zero if perpendicular
            // So we get the dot product of the normal and the Up (Y) vector.
            float dot = normal.dot(Vector3.Y);

            // If the dot product is NOT 1, meaning the ground is not flat, then we disable gravity
            if (dot != 1.0) {
                playerObject.body.setGravity(Vector3.Zero);
            }
        } else {
            playerObject.body.setGravity(Main.DEFAULT_GRAVITY);
        }

        float horizontal = 0;
        float vertical = 0;
        //If chosen keys are press then player is moving
        if ((keys.containsKey(forwardKey) | keys.containsKey(backwardKey)) & (keys.containsKey(strafeRightKey) | keys.containsKey(strafeLeftKey))) {
            speed /= Math.sqrt(2);
        }
        if (keys.containsKey(forwardKey)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.x *= speed;
            v.z *= speed;
            vertical += v.z;
            horizontal += v.x;
        }
        if (keys.containsKey(backwardKey)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.x = -v.x;
            v.z = -v.z;
            v.x *= speed;
            v.z *= speed;
            vertical += v.z;
            horizontal += v.x;
        }
        if (keys.containsKey(strafeLeftKey)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.rotate(Vector3.Y, 90);
            v.x *= speed;
            v.z *= speed;
            vertical += v.z;
            horizontal += v.x;
        }
        if (keys.containsKey(strafeRightKey)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.rotate(Vector3.Y, -90);
            v.x *= speed;
            v.z *= speed;
            vertical += v.z;
            horizontal += v.x;
        }

        movement.z = vertical;
        movement.x = horizontal;
        playerObject.body.setLinearVelocity(movement);
        camera.position.set(playerObject.body.getCenterOfMassPosition());
        if (autoUpdate) camera.update(true);
    }
    
    public GameObject GetPlayerObj(){
        return playerObject;
    }

    /**
     * Check if we are standing on something by casting a ray straight down.
     * @return true if there is collision
     */
    private boolean isGrounded() {
        // Reset out callback
        callback.setClosestHitFraction(1.0f);
        callback.setCollisionObject(null);

        Utils.getPosition(playerObject.transform, position);

        // The position we are casting a ray to, slightly below the players current position.
        tmpPosition.set(position).sub(0, 1.4f, 0);

        mainPhysicsSystem.raycast(position, tmpPosition, callback);

        return callback.hasHit();
    }

    private void resetVelocity() {
        angularVelocity.set(0,0,0);
        linearVelocity.set(0,0,0);
    }
}
