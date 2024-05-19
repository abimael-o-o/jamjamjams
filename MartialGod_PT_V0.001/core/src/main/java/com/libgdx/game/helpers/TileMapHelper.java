package com.libgdx.game.helpers;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class TileMapHelper {
    private TiledMap tiledMap;
    private String tiledMapPath;
    private ScreenInterface gameScreen;

    public TileMapHelper(ScreenInterface gameScreen, String levelPath){
        this.gameScreen = gameScreen;
        this.tiledMapPath = levelPath;
    }

    public OrthogonalTiledMapRenderer setuMap(){
        tiledMap = new TmxMapLoader().load(tiledMapPath);
        parseMapObjects(tiledMap.getLayers().get("Objects").getObjects());
        parseEnemyObjects(tiledMap.getLayers().get("EnemySpawns").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    public Vector2 getPlayerSpawn(){
        MapObject playerPos = tiledMap.getLayers().get("Spawns").getObjects().get("PlayerSpawn");
        RectangleMapObject rectangleMapObject = (RectangleMapObject) playerPos;

        float x = rectangleMapObject.getRectangle().getX();
        float y = rectangleMapObject.getRectangle().getY();
        return new Vector2(x, y);
    }
    public void parseEnemyObjects(MapObjects mapObjects){
        for (MapObject mapObject : mapObjects) {
            if(mapObject instanceof RectangleMapObject){
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;

                float x = rectangleMapObject.getRectangle().getX();
                float y = rectangleMapObject.getRectangle().getY();

                gameScreen.createEnemiesInWorld(new Vector2(x,y), mapObject.getName());
            }
        }
    }

    public Array<Vector2> parseSpawns(String layerName){
        MapObjects mapObjects = tiledMap.getLayers().get(layerName).getObjects();
        Array<Vector2> spawns = new Array<>();
        for (MapObject mapObject : mapObjects) {
            if(mapObject instanceof RectangleMapObject){
                RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;

                float x = rectangleMapObject.getRectangle().getX();
                float y = rectangleMapObject.getRectangle().getY();

                spawns.add(new Vector2(x, y));
            }
        }

        return spawns;
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
