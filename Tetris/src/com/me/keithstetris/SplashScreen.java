package com.me.keithstetris;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// loading screen

public class SplashScreen implements Screen {
	
	private Texture splashTexture;
	private Sprite splashSprite;
	private SpriteBatch batch;
	private Start game;
	private TweenManager manager;
	private int index, score;

	public SplashScreen(Start game, int index, int score) {
		this.game = game;
		this.index = index;
		this.score = score;
		
		// first loading screen
		// not using anymore
		if (index == 1){
			
			splashTexture = new Texture("splashScreen.png");
			splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			splashSprite = new Sprite(splashTexture);
			splashSprite.setColor(1, 1, 1, 0);
			splashSprite.setX(Gdx.graphics.getWidth() / 2 - (splashSprite.getWidth() / 2));
			splashSprite.setY(0);
			
			batch = new SpriteBatch();
			
			Tween.registerAccessor(Sprite.class, new SpriteTween());
			
			manager = new TweenManager();
			
			TweenCallback cb = new TweenCallback(){			
				@Override
				public void onEvent(int type, BaseTween<?> baseTween) {
					easeIntoTitle();
				}
			};
			
			Tween.to(splashSprite, 1, 1.4f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, .7f).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
		
		// loading screen
		// not using anymore, but still gets called
		} else if (index == 2){
			
//			splashTexture = new Texture("loadingScreen.png");
//			splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//			
//			splashSprite = new Sprite(splashTexture);
//			splashSprite.setColor(1, 1, 1, 0);
//			splashSprite.setX(Gdx.graphics.getWidth() / 2 - (splashSprite.getWidth() / 2));
//			splashSprite.setY(0);
//			
//			batch = new SpriteBatch();
//			
//			Tween.registerAccessor(Sprite.class, new SpriteTween());
//			
//			manager = new TweenManager();
//			
//			TweenCallback cb = new TweenCallback(){			
//				@Override
//				public void onEvent(int type, BaseTween<?> baseTween) {
//					tweenCompleted();
//				}
//			};
//			
//			Tween.to(splashSprite, 1, 1f).target(1).ease(TweenEquations.easeInQuad).repeatYoyo(1, 1f).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
			
			tweenCompleted();
			
		// game over screen
		} else if (index == 3){
			
			splashTexture = new Texture("gameOver.png");
			splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			splashSprite = new Sprite(splashTexture);
			splashSprite.setColor(1, 1, 1, 0);
			splashSprite.setX(Gdx.graphics.getWidth()/10);
			splashSprite.setY(Gdx.graphics.getHeight()/10);
			
			batch = new SpriteBatch();
			
			Tween.registerAccessor(Sprite.class, new SpriteTween());
			
			manager = new TweenManager();
			
			TweenCallback cb = new TweenCallback(){			
				@Override
				public void onEvent(int type, BaseTween<?> baseTween) {
					tweenCompleted();
					goToGameScreen();
				}
			};
			
			Tween.to(splashSprite, 1, 1f).target(1).ease(TweenEquations.easeInQuad).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
			
		}

	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		manager.update(delta);
		batch.begin();
		splashSprite.draw(batch);
		batch.end();
	
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
	}
	
	// fade into title screen
	private void easeIntoTitle(){
		
		splashTexture = new Texture("IntroScreen/menuNormal.png");
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		splashSprite = new Sprite(splashTexture);
		splashSprite.setColor(1, 1, 1, 0);
		splashSprite.scale(.2f);
		splashSprite.setPosition(Gdx.graphics.getWidth()/4 + 10, 15);
		
		batch = new SpriteBatch();
		
		Tween.registerAccessor(Sprite.class, new SpriteTween());
		
		manager = new TweenManager();
		
		TweenCallback cb = new TweenCallback(){			
			@Override
			public void onEvent(int type, BaseTween<?> baseTween) {
				tweenCompleted();
			}
		};
		
		Tween.to(splashSprite, 1, 1f).target(1).ease(TweenEquations.easeInQuad).setCallback(cb).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
		
	}
	
	private void goToGameScreen(){
		game.setScreen(new GameOverScreen(game, score));
	}
	
	private void tweenCompleted(){
		if (index == 1){
			Gdx.app.log("", "LOG: intro complete");
			game.setScreen(new IntroScreen(game));
		} else if (index == 2){
			Gdx.app.log("", "LOG: loading complete");
			game.setScreen(new GameScreen(game, updateHighScore()));
		} else if (index == 3){
			Gdx.app.log("", "LOG: gameover complete");
			game.setScreen(new IntroScreen(game));
		}
	}
	
	private int updateHighScore(){
		
		int highScore = 0;
		ArrayList<Integer> score = new ArrayList<Integer>();
		
		try {
			Scanner scanner = new Scanner(new FileReader("scores.txt"));
			while (scanner.hasNextLine())
				score.add(Integer.parseInt(scanner.nextLine()));
			scanner.close();
		} catch (FileNotFoundException e) {
			//System.out.println("FILE NOT FOUND");
		}
		
		for (int a=0;a<score.size();a++)
			if (score.get(a) > highScore)
				highScore = score.get(a);
		
		return highScore;
		
	}
	
	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}