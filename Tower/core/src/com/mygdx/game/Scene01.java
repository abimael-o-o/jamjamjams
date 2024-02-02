package com.mygdx.game;

import static com.mygdx.helper.Constants.PPM;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.enemies.EnemyA;
import com.mygdx.enemies.EnemyInterface;
import com.mygdx.helper.ListenerClass;
import com.mygdx.helper.TileMapHelper;
import com.mygdx.player.Player;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Scene01 extends ScreenAdapter{

    SpriteBatch batch;
	Player player;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;
    
    private World world;
    private Box2DDebugRenderer box2dDebugRenderer; 

    private OrthographicCamera camera;
    private Vector3 cameraOffset = new Vector3(0, 0, 0);

    private RayHandler rayHandler;
    private PointLight pointLight;

    Array<EnemyInterface> enemiesArray;
    
    //Constructs the game scene.
    public Scene01(OrthographicCamera camera){
        this. camera = camera;
        this.world = new World(new Vector2(0,0), false); //World y = gravity
        this.box2dDebugRenderer = new Box2DDebugRenderer(); //Set collision box debug
        this.batch = new SpriteBatch();
        
        //Enemies init
        enemiesArray = new Array<EnemyInterface>();

        //Tiled map init.
        this.tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setuMap();

        //Player init. 
		player = new Player(world, tileMapHelper.getPlayerSpawn());
        
        //Set camera at start to player pos.
        this.camera.position.set(player.GetPlayerPosition().add(cameraOffset));
        this.camera.zoom -= .25;
        
        world.setContactListener(new ListenerClass()); //For physics collisions.

        //Lighting
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.1f);
        pointLight = new PointLight(rayHandler, 100, Color.BLACK, 200, 0, 0);
        pointLight.setSoftnessLength(0f);
        pointLight.setXray(false);

    }

    public void update(){
        world.step(1/60f, 6, 2);
        rayHandler.update();
        cameraUpdate();

        batch.setProjectionMatrix(camera.combined);
        rayHandler.setCombinedMatrix(camera);
        orthogonalTiledMapRenderer.setView(camera);
    }

    public void cameraUpdate(){
        Vector3 targetPos = player.GetPlayerPosition().add(cameraOffset);
        camera.position.lerp(targetPos, .1f);
        camera.update();
        pointLight.setPosition(player.GetPlayerPositionBody());
    }

    @Override
    public void render(float delta){
        this.update();
        ScreenUtils.clear(0, 0, 0, 1, true); //Color screen needed for buffer
        orthogonalTiledMapRenderer.render(); //Render tile map
        
        //Render objects.
        batch.begin();
        player.Draw(batch);
        for (EnemyInterface enemy : enemiesArray) {
            enemy.Draw(batch, player.GetPlayerPositionBody());
        }
        batch.end();
        
        
        rayHandler.render(); //Lights renderer.

        //Collision box2D Debug wireframe
        box2dDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight  = height;
        camera.update();
    }

    @Override
	public void dispose () {
        pointLight.dispose();
		batch.dispose();
        world.dispose();
        orthogonalTiledMapRenderer.dispose();
        box2dDebugRenderer.dispose();
        player.batch.dispose();
	}
    
    public World getWorld(){
        return world;
    }

    public void createEnemiesInWorld(Vector2 pos, String path){
        enemiesArray.add(new EnemyA(world, pos));
    }
}
