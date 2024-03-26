package com.mygdx.game.EDITOR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
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

	private Vector3 position = new Vector3();
	private int selected = -1, selecting = -1;
	private Material selectionMaterial;
	private Material originalMaterial;

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
		multiplexer.addProcessor(this.new SelectObjects());
		Gdx.input.setInputProcessor(multiplexer);

		//* Initialise Debugger -- Remove in production */
		debugDrawer = new DebugDrawer();
		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
		dynamicsWorld.setDebugDrawer(debugDrawer);
		//* --- END */

		selectionMaterial = new Material();
		selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
		originalMaterial = new Material();
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
		//*TODO*/
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

	public class SelectObjects extends InputAdapter{
		@Override
		public boolean touchDown (int screenX, int screenY, int pointer, int button) {
			selecting = getObject(screenX, screenY);
			return selecting >= 0;
		}

		@Override
		public boolean touchDragged (int screenX, int screenY, int pointer) {
			if (selecting < 0) return false;
			if (selected == selecting) {
				Ray ray = cam.getPickRay(screenX, screenY);
				final float distance = -ray.origin.y / ray.direction.y;
				position.set(ray.direction).scl(distance).add(ray.origin);
				instances.get(selected).transform.setTranslation(position);
			}
			return true;
		}

		@Override
		public boolean touchUp (int screenX, int screenY, int pointer, int button) {
			if (selecting >= 0) {
				if (selecting == getObject(screenX, screenY)) setSelected(selecting);
				selecting = -1;
				return true;
			}
			return false;
		}

		public void setSelected (int value) {
			if (selected == value) return;
			if (selected >= 0) {
				Material mat = instances.get(selected).materials.get(0);
				mat.clear();
				mat.set(originalMaterial);
			}
			selected = value;
			if (selected >= 0) {
				Material mat = instances.get(selected).materials.get(0);
				originalMaterial.clear();
				originalMaterial.set(mat);
				mat.clear();
				mat.set(selectionMaterial);

				//*TODO - Fix multiple windows opening when click on other instance.*/
				//Set UI for editing information about the GameObject instance.
				stage.addActor(new ObjectSetState(instances.get(selected)));
			}
		}

		public int getObject (int screenX, int screenY) {
			Ray ray = cam.getPickRay(screenX, screenY);
			int result = -1;
			float distance = -1;
			for (int i = 0; i < instances.size; ++i) {
				final GameObject instance = instances.get(i);
				instance.transform.getTranslation(position);
				position.add(instance.center);
				final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
				if (len < 0f)
					continue;
				float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
				if (distance >= 0f && dist2 > distance) 
					continue;
				if (dist2 <= instance.radius * instance.radius) {
					result = i;
					distance = dist2;
				}
			}
			return result;
		}
	}
	
}
