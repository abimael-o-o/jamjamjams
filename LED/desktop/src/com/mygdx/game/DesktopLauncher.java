package com.mygdx.game;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		Monitor monitor = Lwjgl3ApplicationConfiguration.getPrimaryMonitor();
		DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode(monitor);
		config.setFullscreenMode(displayMode);
		//config.setWindowedMode(1200, 800);

		config.setTitle("V0.01");
		new Lwjgl3Application(new Main(), config);
	}
}
