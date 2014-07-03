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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// screen displayed when the game is over

public class GameOverScreen implements Screen {
	
	private Start game;
	private Stage stage;
	private SpriteBatch batch;
	private Image playAgain, gameOver;
	private Texture playAgainTexture, gameOverTexture;
	private boolean first;
	private GameScreen gameScreen;
	private IntroScreen introScreen;
	private String line;
	
	public GameOverScreen(Start game, int score) {
		
		line = "";
		
		// get previous file contents
		try {
			Scanner scanner = new Scanner(new FileReader("scores.txt"));
			while (scanner.hasNextLine()){
				line += scanner.nextLine() + "\n";
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			// file not found
		}
		
		// add new content
		try {
			
			String content = line + Integer.toString(score);
			
			File file = new File("scores.txt");
 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
 
		} catch (IOException e) {
			// no line found
		}
		
		gameScreen = new GameScreen(game, updateHighScore());
		introScreen = new IntroScreen(game);
		
		this.game = game;
		stage = new Stage();
		batch = new SpriteBatch();
		
		gameOverTexture = new Texture("gameOver.png");
		playAgainTexture = new Texture("playAgain.png");
		gameOver = new Image(gameOverTexture);
		playAgain = new Image(playAgainTexture);
		
		gameOver.setPosition(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
		playAgain.setPosition(Gdx.graphics.getWidth()/10, -Gdx.graphics.getHeight()/2);
		
		first = true;
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.input.setInputProcessor(stage);
		
		// listener for play image
		playAgain.addListener(new InputListener(){
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				
				// if its in that y region
				if (356 <= y && y <= 414 && first){
					
					// if yes
					if (54 <= x && x <= 173) {
						game.setScreen(gameScreen);
						first = false;
					}
					
					// if no
					if (213 <= x && x <= 302) {
						game.setScreen(introScreen);
						first = false;
					}
				}
			}
			
		});
		
		stage.addActor(gameOver);
		stage.addActor(playAgain);
		
		stage.act(delta);
		batch.begin();
		stage.draw();
		batch.end();
		
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
		
	}

}
