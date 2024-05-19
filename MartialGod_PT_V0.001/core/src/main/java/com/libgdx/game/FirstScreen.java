package com.libgdx.game;

import static com.libgdx.game.helpers.Constants.PPM;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.libgdx.game.boss.Boss;
import com.libgdx.game.helpers.ScreenInterface;
import com.libgdx.game.helpers.TileMapHelper;
import com.libgdx.game.helpers.ListenerClass;


/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements ScreenInterface {

    private SpriteBatch batch;
    private Player player;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;
    private Box2DDebugRenderer box2dDebugRenderer;

    private OrthographicCamera camera;
    private Vector3 cameraOffset = new Vector3(0, 50, 0);

    private RayHandler rayHandler;
    private PointLight pointLight;

    private Boss mgBoss;

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.box2dDebugRenderer = new Box2DDebugRenderer(); //Set collision box debug
        this.batch = new SpriteBatch();

        //Tiled map init.
        this.tileMapHelper = new TileMapHelper(this, "assets/map/start.tmx");
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
        pointLight = new PointLight(rayHandler, 50, Color.BLACK, 600, 0, 0);
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
        mgBoss.Draw();
        player.Draw(batch);
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        pointLight.dispose();
        batch.dispose();
        world.dispose();
        orthogonalTiledMapRenderer.dispose();
        box2dDebugRenderer.dispose();
        player.batch.dispose();
        mgBoss.batch.dispose();
    }

    @Override
    public void createEnemiesInWorld(Vector2 pos, String name){
        mgBoss = new Boss(world, pos, tileMapHelper, batch);
    }
}
