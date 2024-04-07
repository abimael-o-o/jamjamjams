package com.mygdx.game.player;

import static com.mygdx.game.helpers.Constants.DYNAMIC_FLAG;
import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.JsonReader;
import com.mygdx.game.helpers.GameObject;

public class Player extends InputAdapter{

    //Player
    private GameObject playerObject;
    protected final IntIntMap keys = new IntIntMap();
	public int strafeLeftKey = Keys.A;
	public int strafeRightKey = Keys.D;
	public int forwardKey = Keys.W;
	public int backwardKey = Keys.S;
    public boolean autoUpdate = true;
    protected float velocity = 5;

    //Camera
    Camera camera;
    float rotateSpeed = 0.2f;
	protected float degreesPerPixel = 0.5f;
	protected final Vector3 tmp = new Vector3();
	private int dragX, dragY;
    private final Vector3 cameraOffset = new Vector3(0, 5, 20); // Adjust camera position relative to player

    public Player(Camera cam){
        this.camera = cam;
        @SuppressWarnings("unused")
        ModelBuilder modelBuilder = new ModelBuilder();
        Model playerModel = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("assets/Player/Player.g3dj"));
        playerObject = new GameObject(playerModel, "Player", new btBoxShape(new Vector3(1f, 1f, 1f)), 
                        1f, new Vector3(0,0,0), DYNAMIC_FLAG);

        playerObject.body.setAngularFactor(new Vector3(0, 1, 0));
        //playerObject.body.setDamping(0f, 0f);

        camera.position.set(playerObject.center.cpy().add(cameraOffset));
        camera.lookAt(playerObject.center);
        camera.update();
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
        // Calculate mouse movement deltas
        float deltaX = dragX - screenX;
        float deltaY = dragY - screenY;

        // Rotate camera around the player horizontally (around the Y-axis)
        camera.rotateAround(playerObject.center, Vector3.Y, deltaX * rotateSpeed);

        // Rotate camera around the player vertically (around camera's right vector)
        tmp.set(camera.direction).crs(camera.up).nor();
        camera.rotateAround(playerObject.center, tmp, deltaY * rotateSpeed);

        // Update camera position
        camera.update();

        // Update drag positions
        dragX = screenX;
        dragY = screenY;

        return true;
    }
    
    public void Update() {
        float speed = velocity;
        float deltaTime = Gdx.graphics.getDeltaTime();

        
        //If chosen keys are press then player is moving
        if ((keys.containsKey(forwardKey) | keys.containsKey(backwardKey)) & (keys.containsKey(strafeRightKey) | keys.containsKey(strafeLeftKey))) {
            speed /= Math.sqrt(2);
        }
        
        Vector3 v = new Vector3(0, 0 ,0 );
        if (keys.containsKey(forwardKey)) {
            v.y = 0f;
            v.z += speed;
        }

        playerObject.body.translate(v);;
    }
    
    public GameObject GetPlayerObj(){
        return playerObject;
    }
}
