package com.mygdx.game.map;

import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.helpers.GameObject;

 //* DROPS FPS BY 100! FiX OR DON'T USE */
 
public class MapConfigA {
    Array<GameObject> instances;
    String pathA = "assets/mapConfigA/FirstFloor/";
    String pathB = "assets/mapConfigA";

    public MapConfigA(){
        instances = new Array<GameObject>();
        ModelLoader<?> loader = new ObjLoader();
		GameObject room = new GameObject(loader.loadModel(Gdx.files.internal(pathB + "/Floor.obj")), "Floor", 
								null , 0f, new Vector3(0,0,0), KINEMATIC_FLAG);
        GameObject cube = new GameObject(loader.loadModel(Gdx.files.internal(pathB + "/Cube.obj")), "Cube", 
                                null , 0f, new Vector3(0,0,0), KINEMATIC_FLAG);
        GameObject cube2 = new GameObject(loader.loadModel(Gdx.files.internal(pathB + "/Cube2.obj")), "Cube2", 
								null , 0f, new Vector3(0,0,0), KINEMATIC_FLAG);
        //*Add all objects to the instance arr */	
        instances.add(room);
        instances.add(cube);
        instances.add(cube2);
    }

    public Array<GameObject> ReturnInstances(){
        return instances;
    }
}
