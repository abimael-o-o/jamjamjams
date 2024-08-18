package com.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
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

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    
    PerspectiveCamera camera;
    ModelBatch batch;
    Environment environment;
    btCollisionConfiguration collisionConfiguration;
    btDispatcher dispatcher;
    btBroadphaseInterface broadphase;
    btDynamicsWorld dynamicsWorld;
    btConstraintSolver constraintSolver;
    
    Array<GameObject> instances;
    G3dModelLoader g3dLoader = new G3dModelLoader(new JsonReader());
   
    DebugDrawer debugDrawer;
    
    @Override
    public void show() {
        /* Set up physics scene where objs will be added and lighting */
        Bullet.init();
        batch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        
        /* Camera */
        camera = new PerspectiveCamera(70, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 20f, 80f);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();
        
        /***
         * Initialize world and collision logic
         * TODO: Create and add ContactListener.java here
         */
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfiguration);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
        instances = new Array<>();
        
        /* Engine Debugs Init Only */
        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_DrawWireframe);
        dynamicsWorld.setDebugDrawer(debugDrawer);
        
        SpawnObjects(); // Instantiate static models and game objects with logic

        //Engine inputs. will need game inputs later and disable engine inputs when game is on.
        InputMultiplexer multiplexer = new InputMultiplexer();
        //Add inputs for player movement and for ui when game is paused
        Gdx.input.setInputProcessor(multiplexer);
    }
    
    private void SpawnObjects(){
        AddInstance("counters.g3dj", Constants.KINEMATIC_FLAG);
        AddInstance("floor.g3dj", Constants.KINEMATIC_FLAG);
    }
    
    public void AddInstance(String str, int cflag){
        try {
            Model m = g3dLoader.loadModel(Gdx.files.internal("models/" + str));
            GameObject obj = new GameObject(m, str.substring(0, str.lastIndexOf('.')), null, 0f, cflag);
            instances.add(obj);
            dynamicsWorld.addRigidBody(obj.body);
            Gdx.app.log("Instance", "Model " + str + " loaded to environment and added successfully.");
        } catch (Exception e) {
            Gdx.app.error("Instance", "Error loading model.", e);
        } 
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) System.exit(0); //Exit Game
        
        /* Time logic and physics renders */
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1.f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        debugDrawer.begin(camera);
        dynamicsWorld.debugDrawWorld();
        debugDrawer.end();
                
        /* Render Here */
        batch.begin(camera);
        batch.render(instances, environment);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        for (GameObject obj : instances)
            obj.dispose();
        
        instances.clear();
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfiguration.dispose();
        batch.dispose();
    }
}