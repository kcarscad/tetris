package com.me.keithstetris;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Tetris";
		cfg.useGL20 = false;
		cfg.resizable = false;
		cfg.width = 480;
		cfg.height = 600;
		
		new LwjglApplication(new Start(), cfg);
	}
}
