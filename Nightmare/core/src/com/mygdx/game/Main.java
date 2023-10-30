package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.map.Scene01;
public class Main extends Game {

	public static Main INSTANCE;
	OrthographicCamera camera;

	public Main(){
		INSTANCE = this;
	}
	
	@Override
	public void create () {
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		setScreen(new Scene01(camera));
	}
}
