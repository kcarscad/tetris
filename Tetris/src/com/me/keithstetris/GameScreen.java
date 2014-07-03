package com.me.keithstetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

// playing the actual game

public class GameScreen implements Screen {

	// initial declarations
	private BitmapFont font;
	private Label scoreLabel, levelLabel, highScoreLabel;
	private ShapeRenderer blockRenderer, lineRenderer;
	private InputHandler inputHandler;
	private Image pausedLabel, sidePanel;
	private Stage stage;
	private SpriteBatch batch;
	private int worldWidth, worldHeight, blockIndex, nextBlockIndex, holdBlockIndex, lastHoldBlockIndex;
	private FinishedBlocks finishedBlocks;
	private NextPiece nextPiece;
	private Start game;
	private Block block;
	private HoldPiece holdPiece;
	private boolean onHold, holdAlreadySwitched;
	
	// set the colours
	public final Color[] COLOR = {
			Color.CYAN, Color.BLUE, Color.ORANGE, 
			Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED};

	// constants
	private final int length = 30;
	private final int panelWidth = (int)(6*length);
	
	// initial value settings
	public GameScreen(Start game, int highScore) {
		
		this.game = game;
		blockRenderer = new ShapeRenderer();
		lineRenderer = new ShapeRenderer();
		inputHandler = new InputHandler();
		finishedBlocks = new FinishedBlocks(length, COLOR);
		nextPiece = new NextPiece(panelWidth, length);
		holdPiece = new HoldPiece(blockIndex, panelWidth, length);
		
		worldWidth = Gdx.graphics.getWidth();
		worldHeight = Gdx.graphics.getHeight();
		
		stage = new Stage();
		batch = new SpriteBatch();
		
		font = new BitmapFont (Gdx.files.internal("whiteFont.fnt"), false);
		
		scoreLabel = new Label("0", new Label.LabelStyle(font, Color.WHITE));
		scoreLabel.setPosition((float)(worldWidth - panelWidth + 2.7*length), (float)(worldHeight - length*8.78));
		scoreLabel.setFontScale(.7f, .7f);
		
		levelLabel = new Label("1", new Label.LabelStyle(font, Color.WHITE));
		levelLabel.setPosition((float)(worldWidth - panelWidth + 2.7*length), (float)(worldHeight - length*6.5));
		levelLabel.setFontScale(1f,1f);
		
		highScoreLabel = new Label(Integer.toString(highScore), new Label.LabelStyle(font, Color.WHITE));
		highScoreLabel.setPosition((float) ((worldWidth - panelWidth + 3.1*length) - (Integer.toString(highScore).length()*.24*length)), (float)(worldHeight - length*4.25));
		highScoreLabel.setFontScale(.7f, .7f);
		
		pausedLabel = null;
		sidePanel = new Image(new Texture("sidebar.png"));
		sidePanel.setPosition(worldWidth - panelWidth - 6, worldHeight - sidePanel.getHeight());
		stage.addActor(sidePanel);

		updateScore(finishedBlocks.getScore());
		updateLevel(finishedBlocks.getLevel());
		
		blockIndex = (int)(Math.random()*7);
		nextBlockIndex = (int)(Math.random()*7);
		holdBlockIndex = 7;
		
		block = new Block(length, blockIndex, this, panelWidth, finishedBlocks);
		
		holdAlreadySwitched = false;
		
	}

	// called each frame
	@Override
	public void render(float delta) {
		
		// clear the screen
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.input.setInputProcessor(inputHandler);

		// begin renderers, set line's color
		blockRenderer.begin(ShapeType.Filled);
		lineRenderer.begin(ShapeType.Line);
		
		// draw background grid, side panel
		drawGrid();
		
		// render background
		finishedBlocks.renderBlocks(blockRenderer, lineRenderer);
		
		// update position, check for hits, etc
		block.getOldPosition();
		block.handleInput(inputHandler, finishedBlocks);
		if (!block.checkIfPaused()) {
			block.updatePositionWithTime(delta, finishedBlocks.getBG());
			unDrawPause();
		} else drawPause();		
		block.checkForImpact(finishedBlocks);
		block.checkIfWithinBoundaries(false, null);
		block.renderBlocks(blockRenderer, lineRenderer, finishedBlocks.getBG(), COLOR[block.blockIndex]);
		
		// if the block reached bottom, make a new block
		if (block.checkIfAtBottom()) {
			finishedBlocks.addNewBlock(block.position, block.blockIndex, block.xParam, block.yParam);
			finishedBlocks.checkLine(block.checkIfSpaceHit());
			updateScore(finishedBlocks.getScore());
			updateLevel(finishedBlocks.getLevel());
			makeNewBlock(false);
		}
		
		// if the game is over, go to gameOver
		if (block.checkIfGameOver() || finishedBlocks.checkIfStuck()) gameOver();
		
		// update
		updateScore(finishedBlocks.getScore());
		updateLevel(finishedBlocks.getLevel());
		
		// update next block
		blockRenderer.setColor(COLOR[nextBlockIndex]);
		nextPiece.updateNextPiece(blockRenderer, lineRenderer, block, nextBlockIndex);
		holdPiece.updateHoldPiece(blockRenderer, lineRenderer, block, holdBlockIndex, COLOR);
		
		if (block.checkForHold()) holdPushed();
		
		if (pausedLabel != null) stage.addActor(pausedLabel);
		
		stage.addActor(highScoreLabel);
		
		// draw labels
		stage.act(delta);
		batch.begin();
		stage.draw();
		batch.end();
		
		// end renderers
		blockRenderer.end();
		lineRenderer.end();

	}
	
	
	
	/********* DRAW/OTHER METHODS *********/
	
	// do everything necessary for when hold block is pushed
	private void holdPushed(){
		
		if (!holdAlreadySwitched){
			
			lastHoldBlockIndex = holdBlockIndex;
			holdBlockIndex = block.getBlockIndex();
			
			// update piece display
			holdPiece.updateHoldPiece(blockRenderer, lineRenderer, block, holdBlockIndex, COLOR);
			
			makeNewBlock(onHold);
			
			onHold = true;
			
		}
		
		holdAlreadySwitched = true;
		
	}

	// redraw the grid (backgorund of main field)
	private void drawGrid(){
		
		lineRenderer.setColor(Color.valueOf("1F1F1F"));
		
		for (int x=0;x<=(worldWidth-panelWidth);x+=length)
			lineRenderer.line(x, 0, x, worldHeight);
		
		for (int y=0;y<=worldHeight;y+=length)
			lineRenderer.line(0, y, worldWidth-panelWidth, y);
		
		lineRenderer.setColor(Color.BLACK);
		
	}
	
	// make a new block, make new indexes
	public void makeNewBlock(boolean onHold) {
		
		// if it was called from the main render class, just do whats normal
		if (!onHold){
			blockIndex = nextBlockIndex;
			nextBlockIndex = (int)(Math.random()*7);
			holdAlreadySwitched = false;
			
			// new block
			block = null;
			block = new Block(length, blockIndex, this, panelWidth, finishedBlocks);
		}
		
		// if it was called from holdPushed, do other stuff
		if (onHold && !holdAlreadySwitched){
			blockIndex = lastHoldBlockIndex;
			holdBlockIndex = block.getBlockIndex();
			
			// new block
			block = null;
			block = new Block(length, blockIndex, this, panelWidth, finishedBlocks);
		}
		
		block.updateRate(finishedBlocks.getLevel());
		
	}
	
	
	
	/********* SMALLER METHODS *********/
	
	// set the next block index (cheat)
	public void setNextBlockIndex(int nextBlockIndex){
		//this.nextBlockIndex = nextBlockIndex;
	}
	
	// make sure there's no pause label
	private void unDrawPause(){
		pausedLabel = null;
		stage = null;
		stage = new Stage();
		stage.addActor(sidePanel);
	}
	
	// display the pause label
	private void drawPause(){
		pausedLabel = new Image(new Texture("paused.png"));
		pausedLabel.setPosition((worldWidth-panelWidth)/2 - pausedLabel.getWidth()/2, worldHeight/2 - pausedLabel.getHeight()*2/3);
	}
	
	// update score
	public void updateScore(int score){
		scoreLabel.setText(Integer.toString(score));
		scoreLabel.setPosition((float) ((worldWidth - panelWidth + 3.1*length) - (Integer.toString(score).length()*.24*length)), (float)(worldHeight - length*8.78));
		stage.addActor(scoreLabel);
	}
	
	// update the level
	public void updateLevel (int level){
		levelLabel.setText(Integer.toString(level));
		stage.addActor(levelLabel);
	}
	
	// called when block hits the top
	private void gameOver() {
		game.setScreen(new SplashScreen(game,3, finishedBlocks.getScore()));
	}
	
	
	
	/********* METHODS I DONT CARE ABOUT *********/

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
		
		blockRenderer.dispose();
		lineRenderer.dispose();
		
	}

}
