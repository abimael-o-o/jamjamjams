package com.mygdx.helper;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.map.Scene01;

public class TileMapHelper {
    private TiledMap tiledMap;
    private Scene01 gameScreen;

    public TileMapHelper(Scene01 gameScreen){
        this.gameScreen = gameScreen;
    }
    
    public OrthogonalTiledMapRenderer setuMap(){
        tiledMap = new TmxMapLoader().load("map/Map.tmx");
        parseMapObjects(tiledMap.getLayers().get("Objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    public Array<Vector2> _ReturnEnemySpawns(){
        Array<Vector2> arr = new Array<>();
        MapObjects mapObjects = tiledMap.getLayers().get("Spawns").getObjects();
        for(MapObject mp : mapObjects){
            if(mp.getName().equals("EnemySpawn")){
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mp;
                Vector2 e = new Vector2(rectangleMapObject.getRectangle().getX(), rectangleMapObject.getRectangle().getY());
                arr.add(e);
            }
        }
        return arr;
    }

    public Vector2 getPlayerSpawn(){
        MapObject playerPos = tiledMap.getLayers().get("Spawns").getObjects().get("PlayerSpawn");
        RectangleMapObject rectangleMapObject = (RectangleMapObject) playerPos;

        float x = rectangleMapObject.getRectangle().getX();
        float y = rectangleMapObject.getRectangle().getY();
        return new Vector2(x, y);
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
