package com.mygdx.game.player;

import static com.mygdx.game.helpers.Constants.DYNAMIC_FLAG;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.IntIntMap;
import com.mygdx.game.helpers.GameObject;

public class Player extends InputAdapter{
    Camera camera;
    GameObject playerObject;
    protected final IntIntMap keys = new IntIntMap();
	public int strafeLeftKey = Keys.A;
	public int strafeRightKey = Keys.D;
	public int forwardKey = Keys.W;
	public int backwardKey = Keys.S;
    public boolean autoUpdate = true;
	protected float velocity = 5;
	protected float degreesPerPixel = 0.5f;
	protected final Vector3 tmp = new Vector3();
	private int dragX, dragY;
    float rotateSpeed = 0.2f;

    public Player(Camera c){
        this.camera = c;
        ModelLoader<?> loader = new ObjLoader();
        Model playerModel = loader.loadModel(Gdx.files.internal("assets/playerAssets/Player.obj"));
        playerObject = new GameObject(playerModel, "Player", new btBoxShape(new Vector3(1f, 2f, 1f)), 
                        1f, new Vector3(0,0,0), DYNAMIC_FLAG);
		//playerObject.body.setAngularFactor(new Vector3(0,1,0));
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

	/** Sets how many degrees to rotate per pixel the mouse moved.
	 * @param degreesPerPixel */
	public void setDegreesPerPixel (float degreesPerPixel) {
		this.degreesPerPixel = degreesPerPixel;
	}

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 direction = camera.direction.cpy();

            // rotating on the y axis
            float x = dragX -screenX;
            camera.rotate(Vector3.Y, x * rotateSpeed);
			playerObject.transform.rotate(Vector3.Y, direction.y);
			
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
        
        public void update(){
            update(Gdx.graphics.getDeltaTime());
        }
        public void update(float deltaTime){
        float speed = velocity;
        if ((keys.containsKey(forwardKey) | keys.containsKey(backwardKey)) & (keys.containsKey(strafeRightKey) | keys.containsKey(strafeLeftKey))) {
            speed /= Math.sqrt(2);
        }

        if (keys.containsKey(forwardKey)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.x *= speed * deltaTime;
            v.z *= speed * deltaTime;
            playerObject.body.translate(v);
        }
        if (keys.containsKey(backwardKey)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.x = -v.x;
            v.z = -v.z;
            v.x *= speed * deltaTime;
            v.z *= speed * deltaTime;
			playerObject.body.translate(v);
        }
        if (keys.containsKey(strafeLeftKey)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.rotate(Vector3.Y, 90);
            v.x *= speed * deltaTime;
            v.z *= speed * deltaTime;
            playerObject.body.translate(v);
        }
        if (keys.containsKey(strafeRightKey)) {
            Vector3 v = camera.direction.cpy();
            v.y = 0f;
            v.rotate(Vector3.Y, -90);
            v.x *= speed * deltaTime;
            v.z *= speed * deltaTime;
            playerObject.body.translate(v);
        }
		camera.position.set(playerObject.body.getCenterOfMassPosition());
		//camera.update();
        playerObject.transform.set(playerObject.body.getWorldTransform());
        if (autoUpdate) camera.update(true);
    }
    
    public GameObject GetPlayerObj(){
        return playerObject;
    }
}
