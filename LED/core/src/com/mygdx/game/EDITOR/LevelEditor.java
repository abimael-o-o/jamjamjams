package com.mygdx.game.EDITOR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.mygdx.game.helpers.GameObject;


public class LevelEditor extends ScreenAdapter{
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputs camController;
	public ModelBatch modelBatch;
	public Model model;
	public Array<GameObject> instances;
	public Stage stage;

	btCollisionConfiguration collisionConfig;
	btDispatcher dispatcher;
	btBroadphaseInterface broadphase;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;
	DebugDrawer debugDrawer;

	public LevelEditor(){
		Bullet.init();

		//* Environment works with global light only */
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		//* ---- */
		
		//* Adds batch and physics simulation to the scene */
		modelBatch = new ModelBatch();
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
		instances = new Array<GameObject>();
		//* ---- */

		//* Create the camera used in the scene */
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
        
        camController = new CameraInputs(cam); //* Controls movement for camera and selecting objects. */
		
		//* UI and input processors setup */
		VisUI.load(SkinScale.X1);
		stage = new Stage( new ScreenViewport());
		stage.addActor(new Commands(this));
		stage.addActor(new TestFileChooser(this));


		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(multiplexer);

		//* Initialise Debugger -- Remove in production */
		debugDrawer = new DebugDrawer();
		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
		dynamicsWorld.setDebugDrawer(debugDrawer);
		//* --- END */
	}

	@Override
	public void render(float deltaTime) {
		dynamicsWorld.stepSimulation(deltaTime, 5, 1f / 60f);
		camController.update();

		//* Only used on Debug -- Remove in production */
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) System.exit(0);
		//* --- END */
		
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		//* Debugger for drawing collision boxes and wireframes */
		debugDrawer.begin(cam);
		dynamicsWorld.debugDrawWorld();
		debugDrawer.end();
		//* --- END OF DEBUGGER */

        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
		
		//CheckForTouchObj();
		//* Stage goes below modelBatch to make sure UI is always on top of camera */
		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose() {
		modelBatch.dispose();
		model.dispose();
		VisUI.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public void SetInstances(GameObject object){
		instances.add(object);
		dynamicsWorld.addRigidBody(object.body);
	}
	
}
