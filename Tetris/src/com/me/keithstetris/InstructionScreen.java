package com.me.keithstetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class InstructionScreen implements Screen {
	
	private Start game;
	private Image image, back;
	private Stage stage;
	private SpriteBatch batch;
	private IntroScreen introScreen;
	
	public InstructionScreen(Start game, IntroScreen introScreen){
		
		this.game = game;
		image = new Image(new Texture("IntroScreen/instructions.png"));
		image.setPosition(-10,Gdx.graphics.getHeight()/9);
		image.setScale(1.08f);
		back = new Image(new Texture("back.png"));
		back.setPosition(10,10);
		back.setScale(1.3f);
		stage = new Stage();
		batch = new SpriteBatch();
		this.introScreen = introScreen;
		
		stage.addActor(image);
		stage.addActor(back);
	}
	
	@Override
	public void render(float delta) {
		
		// cl screen
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.input.setInputProcessor(stage);
		
		back.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				
				if (0 <= x && x <= 80 && 0 <= y && y <= 35) 
					game.setScreen(introScreen);
				
			}
			
		});
		
		stage.act(delta);
		batch.begin();
		stage.draw();
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

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
		batch.dispose();
		stage.dispose();
	}

}
