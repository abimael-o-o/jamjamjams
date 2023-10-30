package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	static int userHeight = 800;
	static int userWidth = 1000;

	static int fullScreenX = 0;
	static int fullScreenY = 0;

	public static void main (String[] arg) {
		
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		fullScreenX = Lwjgl3ApplicationConfiguration.getDisplayMode().width;
		fullScreenY = Lwjgl3ApplicationConfiguration.getDisplayMode().height;
		config.setWindowedMode(userWidth, userHeight);
		config.setWindowSizeLimits(userWidth, userHeight, fullScreenX, fullScreenY);
		config.setTitle("Draoidh_V0.01_Proto");
		new Lwjgl3Application(new Main(), config);
	}
}
