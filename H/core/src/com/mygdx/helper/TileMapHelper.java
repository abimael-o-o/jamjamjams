package com.mygdx.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.enemies.EnemyActions;
import com.mygdx.game.Scene01;
import com.mygdx.resources.ArrayInterface;
import com.mygdx.resources.LinkedArray;

public class TileMapHelper {
    private TiledMap tiledMap;
    private Scene01 gameScreen;

    private float timeSeconds = 0f;
    private float period = 5f; //second cooldown for spawn enemies

    ArrayInterface<EnemyActions> enemiesContainer;

    public TileMapHelper(Scene01 gameScreen){
        this.gameScreen = gameScreen;
    }

    public OrthogonalTiledMapRenderer setuMap(){
        tiledMap = new TmxMapLoader().load("levels/Maze.tmx");
        enemiesContainer = new LinkedArray<>();
        parseMapObjects(tiledMap.getLayers().get("Objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    public Vector2 getPlayerSpawn(){
        MapObject playerPos = tiledMap.getLayers().get("Spawns").getObjects().get("PlayerSpawn");
        RectangleMapObject rectangleMapObject = (RectangleMapObject) playerPos;

        float x = rectangleMapObject.getRectangle().getX();
        float y = rectangleMapObject.getRectangle().getY();
        return new Vector2(x, y);
    }

    public void AddEnemies(){
        //Execute handleEvent each specified time.
        timeSeconds += Gdx.graphics.getDeltaTime();
        if(timeSeconds > period){
            timeSeconds -= period;
            enemiesContainer.add(new EnemyActions(new Vector2(32, 32), gameScreen.getWorld()));
            System.out.println("number of entries: " + enemiesContainer.getCurrentSize());
        }
    }

    public void UpdateEnemies(SpriteBatch batch){
        int numberOfEntries = enemiesContainer.getCurrentSize();
        Object[] entries = enemiesContainer.toArray();
        for(int i = 0; i < numberOfEntries; i++){
            if(entries[i] instanceof EnemyActions){
                EnemyActions e = (EnemyActions) entries[i];
                e.Draw(batch);
            }
        }
    }

    public void parseMapObjects(MapObjects mapObjects){
        for(MapObject mapObject: mapObjects){
            if(mapObject instanceof PolygonMapObject){
                createStaticBody((PolygonMapObject) mapObject);
            }
        }
    }

    private void createStaticBody(PolygonMapObject polygonMapObject){
        new MapBodyDefiner().createBody(polygonMapObject, gameScreen.getWorld(), polygonMapObject.getName());
    }
}
