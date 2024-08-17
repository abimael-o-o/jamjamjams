package com.mygdx.game;
import com.badlogic.gdx.Game;
import com.mygdx.game.EDITOR.LevelEditor;


public class Main extends Game {
	public static Main INSTANCE;

	public Main(){
		INSTANCE = this;
	}
	
	@Override
	public void create () {
		setScreen(new LevelEditor());
	}
}
