package com.me.keithstetris;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;

// called initially, sets the screen and logs everything (FPS, tweens, etc)

public class Start extends Game {
	
	FPSLogger log;
	public Music music;
	
	@Override
	public void create() {
		log = new FPSLogger();
		setScreen(new IntroScreen(this));
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
		log.log();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
}
