package com.mygdx.game.map;

import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.helpers.GameObject;

public class MapConfigA {
    Array<GameObject> instances;
    String pathA = "assets/mapConfigA/FirstFloor/";
    String pathB = "assets/mapConfigA";

    public MapConfigA(){
        instances = new Array<GameObject>();
        ModelLoader<?> loader = new ObjLoader();
		GameObject room = new GameObject(loader.loadModel(Gdx.files.internal(pathA + "Room.obj")), "Room", 
								null , 0f, new Vector3(0,0,0), KINEMATIC_FLAG);

        //*Add all objects to the instance arr */	
        instances.add(room);
    }

    public Array<GameObject> ReturnInstances(){
        return instances;
    }
}
