package com.mygdx.helper;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Scene01;


public class TileMapHelper {
    private TiledMap tiledMap;
    private Scene01 gameScreen;

    public TileMapHelper(Scene01 gameScreen){
        this.gameScreen = gameScreen;
    }

    public OrthogonalTiledMapRenderer setuMap(){
        tiledMap = new TmxMapLoader().load("levels/Maze.tmx");
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
