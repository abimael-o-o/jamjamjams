package com.mygdx.game;

import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;
import static com.mygdx.game.helpers.Constants.STATIC_FLAG;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;

import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.helpers.GameObject;
import com.mygdx.game.player.Player;

public class Main extends ApplicationAdapter {
	public static final Vector3 DEFAULT_GRAVITY = new Vector3(0, -9.81f, 0f);
	PerspectiveCamera cam;
	FirstPersonCameraController firstPersonCam;
	CameraInputController camController;
	ModelBatch modelBatch;
	Environment environment;
	Array<GameObject> instances;
	Player player;
	
	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;
	DebugDrawer debugDrawer;

	//*FPS Code */
	long lastTimeCounted;
	private float sinceChange;
	private float frameRate;
	private BitmapFont font;
	private SpriteBatch batch;

	Vector3 playerPosDebug;

	// Debug drawing ray casts
	private final Vector3 lastRayFrom = new Vector3();
	private final Vector3 lastRayTo = new Vector3();
	private final Vector3 rayColor = new Vector3(1, 0, 1);
	
	@Override
	public void create () {
		Bullet.init();
		
		modelBatch = new ModelBatch();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
		cam = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 0f, 0f);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(DEFAULT_GRAVITY);
		//Contact listener code goes here.
		instances = new Array<GameObject>();
		
		//* Initialise Debugger -- Remove in production */
		debugDrawer = new DebugDrawer();
		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
		dynamicsWorld.setDebugDrawer(debugDrawer);
		//* --- END */
        
        SpawnObjects();
		player = new Player(cam, this);
		instances.add(player.GetPlayerObj());
		dynamicsWorld.addRigidBody(player.GetPlayerObj().body);
		Gdx.input.setInputProcessor(player);
		Gdx.input.setCursorCatched(true); //Hide Cursor

		//*FPS Code */
		lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();
        font = new BitmapFont();
		batch = new SpriteBatch();

		//* Player Position Debug */
		playerPosDebug = player.GetPlayerObj().body.getCenterOfMassPosition();
	}

	public void UpdateScene(){
		//* Only used on Debug -- Remove in production */
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) System.exit(0);

		//FPS CODE ---
		long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if(sinceChange >= 1000) {
			sinceChange = 0;
            frameRate = Gdx.graphics.getFramesPerSecond();
        }
		//* Player Position Debug */
		playerPosDebug = player.GetPlayerObj().body.getCenterOfMassPosition();
		//* --- END */
	}

	@Override
	public void render () {
		final float delta = Math.min(1f / 30f, Gdx.graphics.getDeltaTime());
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);

		player.Update(delta); //Player inputs and updates
		UpdateScene();

		Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		//* Debugger for drawing collision boxes and wireframes */
		debugDrawer.begin(cam);
		dynamicsWorld.debugDrawWorld();
		debugDrawer.end();
		//* --- END OF DEBUGGER */
		
		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
		
		//* 2D Stuff i.e UI and other */
		batch.begin();
		font.draw(batch, (int)frameRate + " fps", 30, Gdx.graphics.getHeight() - 30);
		font.draw(batch, "Player Position: " + playerPosDebug , 30, Gdx.graphics.getHeight() - 900);
		batch.end();
	}
	
	//* Manual spawning of objects in the world */
	private void SpawnObjects(){
		String levelPath = "assets/Level/";
		@SuppressWarnings("unused")
		ModelBuilder modelBuilder = new ModelBuilder();

		G3dModelLoader g3dLoader = new G3dModelLoader(new JsonReader());

        Model planeModel = g3dLoader.loadModel(Gdx.files.internal(levelPath + "floor.g3dj"));
        GameObject plane = new GameObject(planeModel, "floor",null,0f, KINEMATIC_FLAG);

		Model treeModel = g3dLoader.loadModel(Gdx.files.internal(levelPath + "tree.g3dj"));
		GameObject tree = new GameObject(treeModel, "tree",null,0f, KINEMATIC_FLAG);


		Model columnModel = g3dLoader.loadModel(Gdx.files.internal(levelPath + "column.g3dj"));
		GameObject column = new GameObject(columnModel, "column",null,0f, KINEMATIC_FLAG);

		instances.add(plane);
		instances.add(tree);
		instances.add(column);
		dynamicsWorld.addRigidBody(plane.body);
		dynamicsWorld.addRigidBody(tree.body);
		dynamicsWorld.addRigidBody(column.body);
	}
	/**
	 * Perform a raycast in the physics world.
	 * @param from the starting position (origin) of the ray
	 * @param to the end position of the ray
	 * @param callback the callback object to use
	 */
	public void raycast(Vector3 from, Vector3 to, RayResultCallback callback) {
		lastRayFrom.set(from).sub(0, 5f, 0f);

		dynamicsWorld.rayTest(from, to, callback);

		if (callback.hasHit() && callback instanceof ClosestRayResultCallback) {
			// Use interpolation to determine the hitpoint where the ray hit the object
			// This is what bullet does behind the scenes as well
			lastRayTo.set(from);
			lastRayTo.lerp(to, callback.getClosestHitFraction());
		} else {
			lastRayTo.set(to);
		}
	}
	@Override
	public void dispose () {
		for (GameObject obj : instances)
			obj.dispose();
		instances.clear();

		dynamicsWorld.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();
		modelBatch.dispose();

		font.dispose();
        batch.dispose();
	}
}
