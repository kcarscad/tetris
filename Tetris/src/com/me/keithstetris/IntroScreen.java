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

// main menu

public class IntroScreen implements Screen {

	private Stage stage;
	private Texture menuTexture;
	private Image menu;
	private SpriteBatch batch;
	private Start game;
	private boolean play, quit, instructions, first = true;
	private SplashScreen splashScreen;
	private InstructionScreen instructionScreen;
	private int count;
	
	public IntroScreen(Start game) {
		
		splashScreen = new SplashScreen(game, 2, 0);
		play = quit = instructions = false;
		instructionScreen = new InstructionScreen(game, this);
		this.game = game;
		batch = new SpriteBatch();
		menuTexture = new Texture("IntroScreen/menuNormal.png");
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		menu = new Image(menuTexture);
		setTitleImage();
		
	}
	
	@Override
	public void render(float delta) {
		
		// clear the screen
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.input.setInputProcessor(stage);
		
		menu.addListener(new InputListener(){
			
			@Override
			public boolean mouseMoved (InputEvent event, float x, float y){
				
				if (40 <= x && x <= 220 && first){
					
					// check for each button
					if (222 <= y && y <= 278){
						play();
						first = false;
					} else if (133 <= y && y <= 189){
						instructions();
						first = false;
					} else if (43 <= y && y <= 99){
						quit();
						first = false;
					}
					
				}
				
				if (!first){
					// check if mouse is inbetween the buttons
					if ((0 <= y && y <= 43) || (99 <= y && y <= 133) || (189 <= y && y <= 222) || (278 <= y && y <= Gdx.graphics.getHeight())){
						menuTexture = new Texture("IntroScreen/menuNormal.png");
						setTitleImage();
						first = true;
					}
					
					// check if mouse is on either side
					if ((0 <= x && x <= 40) || (220 <= x && x <= Gdx.graphics.getWidth())){
						menuTexture = new Texture("IntroScreen/menuNormal.png");
						setTitleImage();
						first = true;
					}
					
				}
				
				return true;
				
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				
				if (40 <= x && x <= 220){
					
					if (222 <= y && y <= 278){
						quit = false;
						play = true;
						instructions = false;
					}
					
					if (133 <= y && y <= 189){
						quit = false;
						play = false;
						instructions = true;
					}
					
					if (43 <= y && y <= 99){
						quit = true;
						play = false;
						instructions = false;
					}
					
				}
				
				count = 0;
				
				return true;
				
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				
				if (play) 
					game.setScreen(new GameScreen(game, button));
				
				if (instructions && count == 0) {
					game.setScreen(instructionScreen);
					menuTexture = new Texture("IntroScreen/menuNormal.png");
					setTitleImage();
				}
				
				if (quit) System.exit(0);
				
				count=1;
				
			}
			
		});
		
		stage.addActor(menu);
		
		stage.act(delta);
		batch.begin();
		stage.draw();
		batch.end();
		
	}
	
	// nothing pushed
	public void setTitleImage(){
		menu = new Image(menuTexture);
		menu.setPosition(Gdx.graphics.getWidth()/4 -41, -36);
		menu.scale(.2f);
		stage = new Stage();
		stage.addActor(menu);
	}

	// play button pushed
	public void play() {
		menuTexture = new Texture("IntroScreen/menuPlay.png");
		setTitleImage();
		play = true;
	}
	
	// instructions button pushed
	public void instructions(){
		menuTexture = new Texture("IntroScreen/menuInstructions.png");
		setTitleImage();
		instructions = true;
	}
	
	// quit button pushed
	public void quit(){
		menuTexture = new Texture("IntroScreen/menuQuit.png");
		setTitleImage();
		quit = true;
	}
		
	@Override
	public void resize(int width, int height) {
		
		if (stage == null)
			stage = new Stage(width, height, true);
		stage.clear();
		
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
