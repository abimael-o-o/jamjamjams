package com.mygdx.game;
import com.badlogic.gdx.Game;

public class Main extends Game {

	public static Main INSTANCE;

	public Main(){
		INSTANCE = this;
	}
	@Override
	public void create () {
		//Set main menu as screen on initial launch.
		setScreen(new MainMenu(INSTANCE));
	}
}
