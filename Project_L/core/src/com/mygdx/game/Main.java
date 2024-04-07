package com.mygdx.game;

import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;

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
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
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
		dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
		//Contact listener code goes here.
		instances = new Array<GameObject>();
		
		//* Initialise Debugger -- Remove in production */
		debugDrawer = new DebugDrawer();
		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
		dynamicsWorld.setDebugDrawer(debugDrawer);
		//* --- END */
        
        SpawnObjects();
		player = new Player(cam);
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
		player.Update(); //Player inputs and updates

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
        GameObject plane = new GameObject(planeModel, "floor",null, 
                        0f, new Vector3(0,0,0), KINEMATIC_FLAG);

		Model treeModel = g3dLoader.loadModel(Gdx.files.internal(levelPath + "tree.g3dj"));
		GameObject tree = new GameObject(treeModel, "tree",null, 
						0f, new Vector3(0,0,0), KINEMATIC_FLAG);


		Model columnModel = g3dLoader.loadModel(Gdx.files.internal(levelPath + "column.g3dj"));
		GameObject column = new GameObject(columnModel, "column",null, 
						0f, new Vector3(0,0,0), KINEMATIC_FLAG);

		instances.add(plane);
		instances.add(tree);
		instances.add(column);
		dynamicsWorld.addRigidBody(plane.GetBody());
		dynamicsWorld.addRigidBody(tree.GetBody());
		dynamicsWorld.addRigidBody(column.GetBody());
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
