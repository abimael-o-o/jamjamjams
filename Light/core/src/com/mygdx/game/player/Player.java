package com.mygdx.game.player;

import static com.mygdx.game.helpers.Constants.DYNAMIC_FLAG;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.mygdx.game.helpers.GameObject;

public class Player extends FirstPersonCameraController{
    private Camera camera;
    private GameObject playerObject;

    public Player(Camera c){
        super(c);
        this.camera = c;
        playerObject = new GameObject(null, null, null, 1f, null, DYNAMIC_FLAG);
    }

    @Override
    public void update(float deltaTime){
        
    }

    public Camera GetPlayerCamera(){
        return camera;
    }

    public GameObject GetPlayerObj(){
        return playerObject;
    }
}
