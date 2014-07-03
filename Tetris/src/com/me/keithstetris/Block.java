package com.me.keithstetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

// holds all values for each tetrimino

public class Block {
	
	// declarations
	private int length, areaWidth, areaHeight, xOffsetLeft, yOffset, xPrevious[], yPrevious[], positionBeforeTick[];
	private float deltaSum, rate;
	private int maxx, maxy, state, newBlockCount;
	private boolean newBlock, gameOver, hit, rotationInterference, paused, hold, spaceHit;
	public int blockIndex, oldxParam[], oldyParam[], xParam[], yParam[];
	public Vector2 position[][], oldPosition[][];
	private GameScreen gameScreen;
	
	/* 
	  SHAPE INDEXES 
	 * 
	 * 0 I
	 * 1 L (backwards)
	 * 2 L
	 * 3 o
	 * 4 s
	 * 5 prong
	 * 6 z
	 */
	
	// position number (0-3), shape number (0-6), x or y (0-1), which set of x and y (0-3)
	// [state][blockIndex][x/y][0/3]
	// index of all shapes/rotations
	public final int[][][][] SHAPES = {
			
			// initial positions
			{{{0,1,2,3},{1,1,1,1}},
			{{0,1,2,2},{1,1,1,0}},
			{{0,0,1,2},{0,1,1,1}},
			{{0,0,1,1},{1,0,1,0}},
			{{0,1,1,2},{0,0,1,1}},
			{{0,1,1,2},{1,0,1,1}},
			{{0,1,1,2},{1,0,1,0}}},
			
			// second positions 
			{{{2,2,2,2},{0,1,2,3}},
			{{0,1,1,1},{0,0,1,2}},
			{{0,1,1,1},{2,0,1,2}},
			{{0,0,1,1},{1,0,1,0}},
			{{1,1,2,2},{1,2,0,1}},
			{{0,1,1,1},{1,0,1,2}},
			{{1,1,2,2},{0,1,1,2}}},
			
			// third positions 
			{{{0,1,2,3},{1,1,1,1}},
			{{0,0,1,2},{1,2,1,1}},
			{{0,1,2,2},{1,1,1,2}},
			{{0,0,1,1},{1,0,1,0}},
			{{0,1,1,2},{0,0,1,1}},
			{{0,1,1,2},{1,1,2,1}},
			{{0,1,1,2},{1,0,1,0}}},
			
			// fourth positions 
			{{{2,2,2,2},{0,1,2,3}},
			{{1,1,1,2},{0,1,2,2}},
			{{1,1,1,2},{0,1,2,0}},
			{{1,1,2,2},{1,0,1,0}},
			{{1,1,2,2},{1,2,0,1}},
			{{1,1,1,2},{0,1,2,1}},
			{{1,1,2,2},{0,1,1,2}}}
			
			// fourth position, x values all add 1*length
						
	};
	
	// constructor, initial values
	public Block (int length, int blockIndex, GameScreen gameScreen, int panelWidth, FinishedBlocks finishedBlocks){
		
		rate = 0.5625f;
		this.gameScreen = gameScreen;
		this.blockIndex = blockIndex;
		this.length = length;
		paused = false;
		areaWidth = Gdx.graphics.getWidth() - panelWidth;
		areaHeight = Gdx.graphics.getHeight();
		position = new Vector2[4][4];
		oldPosition = new Vector2[4][4];
		state = 0;
		newBlock = false;
		xParam = new int[4];
		yParam = new int[4];
		oldxParam = new int[4];
		oldyParam = new int[4];
		xPrevious = new int[4];
		yPrevious = new int[4];
		hit = false;
		positionBeforeTick = new int[4];
		for (int a=0;a<4;a++) positionBeforeTick[a] = 0;
		newBlockCount = 0;
		gameOver = false;
		spaceHit = false;
		hold = false;
		
		for (int a=0;a<4;a++) {
			xParam[a] = SHAPES[state % 4][blockIndex][0][a];
			yParam[a] = SHAPES[state % 4][blockIndex][1][a];
		} setShape();

		// set max values for x and y
		for (int a=0;a<4;a++){
			if (xParam[a] > maxx) maxx = xParam[a];
			if (yParam[a] > maxy) maxy = yParam[a];
		} maxx++; maxy++;

		setInitialPosition();
		makeAdjustments();
		
		for (int a=0;a<4;a++)
			if (finishedBlocks.getBG()[(int)(position[xParam[a]][yParam[a]].x/length)][(int)(position[xParam[a]][yParam[a]].y/length)])
				gameOver = true;
		
	}
	
	// set shape/position initial vectors
	private void setShape(){
		
		for (int a=0;a<4;a++){
			position[xParam[a]][yParam[a]] = new Vector2(0,0);
		}
		
	}
	
	// draw the initial shape
	private void setInitialPosition(){
		
		for (int a=0;a<4;a++){
			position[xParam[a]][yParam[a]].x = ((xParam[a] + 4)*(length));
			position[xParam[a]][yParam[a]].y = areaHeight + (yParam[a]*length) - (maxy*length);
		}
			
	}
	
	// make different offsets for different shapes/rotations (boundaries)
	private void makeAdjustments(){
		
		// reset values for each new rotation
		xOffsetLeft = yOffset = 0;
		
		// first adjustments
		if (blockIndex == 0 && state % 2 == 0) 
			yOffset = -length;
		
		// second adjustments
		if ((blockIndex == 4 || blockIndex == 6) && state % 4 == 1) 
			xOffsetLeft = -length;
		
		// third adjustments
		if ((blockIndex == 1 || blockIndex == 2 || blockIndex == 5) && state % 4 == 2) 
			yOffset = -length;
			
		// fourth adjustments
		if ((blockIndex != 0 && blockIndex != 3) & state % 4 == 3) 
			xOffsetLeft = -length;
		
		// extra adjustments
		if (blockIndex == 0 && state % 2 == 1) 
			xOffsetLeft = -2*length;
				
	}

	// rotate the block
	private void rotateBlock(boolean bg[][]){
		
		rotationInterference = false;
		
		// check if anything will interfere with rotation (blocks or sides)
		for (int a=0;a<4;a++)
			try {
				if (bg[(int)(((position[xParam[a]][yParam[a]].x) - (xParam[a]*length) + (SHAPES[(state+1)%4][blockIndex][0][a]*length))/length)]
						[(int)(((position[xParam[a]][yParam[a]].y) - (yParam[a]*length) + (SHAPES[(state+1)%4][blockIndex][1][a]*length))/length)])
					if (checkIfWithinBoundaries(true, position))
						rotationInterference = true;
			} catch (ArrayIndexOutOfBoundsException e){
			//	System.out.println(a);
			}
		
		// if there's no interference, business as usual
		if (!rotationInterference){
			
			// add one to state, reset maxx/y
			state++;
			maxx = maxy = 0;
			
			// set old values for later use
			for (int a=0;a<4;a++){
				oldPosition[xParam[a]][yParam[a]] = new Vector2(0,0);
				oldPosition[xParam[a]][yParam[a]].x = position[xParam[a]][yParam[a]].x;
				oldPosition[xParam[a]][yParam[a]].y = position[xParam[a]][yParam[a]].y;
			}
			
			// set old param values
			for (int a=0;a<4;a++){
				oldxParam[a] = xParam[a];
				oldyParam[a] = yParam[a];
			}
			
			// make old position values null
			for (int a=0;a<4;a++)
				position[xParam[a]][yParam[a]] = null;
			
			// set new params
			for (int a=0;a<4;a++){
				xParam[a] = SHAPES[state % 4][blockIndex][0][a];
				yParam[a] = SHAPES[state % 4][blockIndex][1][a];
			}
			
			// set new max values
			for (int a=0;a<4;a++){
				if (xParam[a] > maxx) maxx = xParam[a];
				if (yParam[a] > maxy) maxy = yParam[a];
			} maxx++;maxy++;
			
			// initialize new position/shape values so they can be used
			setShape();
			
			// set new position to old position - oldDistanceToOrigin + newDistanceFromOrigin
			for (int a=0;a<4;a++){
				position[xParam[a]][yParam[a]].x = (oldPosition[oldxParam[a]][oldyParam[a]].x) - (oldxParam[a]*length) + (xParam[a]*length);
				position[xParam[a]][yParam[a]].y = (oldPosition[oldxParam[a]][oldyParam[a]].y) - (oldyParam[a]*length) + (yParam[a]*length);
			}
			
			// get rid of old values
			for (int a=0;a<4;a++){
				oldPosition[oldxParam[a]][oldyParam[a]] = null;
				oldxParam[a] = 0;
				oldyParam[a] = 0;
			}
			
			makeAdjustments(); 
			
		}
		
	}
	
	// move it down every second
	public void updatePositionWithTime(float delta, boolean bg[][]) {
		
		int spaceUnderBlocks = 0;
		
		// add to total time
		deltaSum += delta;
		
		// see if the block can continue moving down, even when stationary for a while
		try {
			// if all blocks below it are false, change positionBeforeTick
			for (int a=0;a<4;a++)
				if (!bg[(int)(position[xParam[a]][yParam[a]].x/length)][(int)(position[xParam[a]][yParam[a]].y/length) - 1])
					spaceUnderBlocks++;
			
			if (spaceUnderBlocks == 4)
				for (int a=0;a<4;a++)
					positionBeforeTick[a] -= length;
			
		// doesnt matter when its at the bottom
		} catch (ArrayIndexOutOfBoundsException e){
			
		}
		
		// if 0.5s pass, move down one
		if (deltaSum >= rate){
			
			// count each time the position doesnt change
			for (int a=0;a<4;a++)
				if (positionBeforeTick[a] == position[xParam[a]][yParam[a]].y)
					newBlockCount++;
			
			// if none of them change
			if (newBlockCount == 4)
				newBlock = true;
			
			// set the position before tick to its current position (use next tick)
			for (int a=0;a<4;a++)
				positionBeforeTick[a] = (int)(position[xParam[a]][yParam[a]].y);
			
			// actually move it down
			for (int a=0;a<4;a++) position[xParam[a]][yParam[a]].y -= length;
			
			// reset
			deltaSum = 0;
			newBlockCount = 0;
			
		}
		
	}

	// input handler, check for different inputs
	public void handleInput (InputHandler input, FinishedBlocks finishedBlocks){
		
		// check if paused
		if (input.paused)
			paused = !paused;
		
		// if its not paused
		if (!paused){
			// check for all different inputs
			if (input.moveLeft) 
				for (int a=0;a<4;a++)
					position[xParam[a]][yParam[a]].x -= length;
			
			if (input.moveRight) 
				for (int a=0;a<4;a++)
					position[xParam[a]][yParam[a]].x += length;
			
			if (input.moveDown)
				for (int a=0;a<4;a++)
					position[xParam[a]][yParam[a]].y -= length;
			
			if (input.space){
				findShadow(finishedBlocks.getBG(), true);
				newBlock = true;
				spaceHit = true;
			}
			
			if (input.quit)
				System.exit(0);
			
			if (input.rotate && blockIndex != 3)
				rotateBlock(finishedBlocks.getBG());
			
			if (input.hold)
				hold = true;
			
			for (int a=0;a<7;a++)
				if (input.piece[a]) gameScreen.setNextBlockIndex(a);
			
			// reset necessary inputs to false
			input.moveDown = false;
			input.moveLeft = false;
			input.moveRight = false;
			input.rotate = false;
			input.space = false;
			input.hold = false;
			for (int a=0;a<7;a++) input.piece[a] = false;
		}
		
		input.paused = false;
		
	}
	
	// check if it hits the edges, no need for top
	public boolean checkIfWithinBoundaries(boolean checkForRotate, Vector2 position[][]) {
		
		// if this is checking for rotation interference
		if (checkForRotate){
			
			// each block
			for (int a=0;a<4;a++){
				
				// left
				if (position[xParam[a]][yParam[a]].x < xParam[a] * length) 
					return false;
				
				// right
				if (position[xParam[a]][yParam[a]].x + (maxx-xParam[a])*length > areaWidth) 
					return false;
							
				// bottom
				if (position[xParam[a]][yParam[a]].y < yParam[a] * length)
					return false;
				
			}
			
		// if this was called from the main screen (every frame)
		} else {
		
			// check if it hit wall
			for (int a=0;a<4;a++){
				
				// left
				if (this.position[xParam[a]][yParam[a]].x < xParam[a] * length + xOffsetLeft) 
					this.position[xParam[a]][yParam[a]].x = xParam[a] * length + xOffsetLeft;
				
				// right
				if (this.position[xParam[a]][yParam[a]].x + (maxx-xParam[a])*length > areaWidth) 
					this.position[xParam[a]][yParam[a]].x = areaWidth - (maxx-xParam[a])*length;
							
				// bottom
				if (this.position[xParam[a]][yParam[a]].y < yParam[a] * length + yOffset)
					this.position[xParam[a]][yParam[a]].y = yParam[a] * length + yOffset;
					
			}
			
		}
		
		return true;
		
	}
	
	// get the old position for later use
	public void getOldPosition(){
		
		// set previous positions
		for (int a=0;a<4;a++){
			xPrevious[a] = (int)(position[xParam[a]][yParam[a]].x);
			yPrevious[a] = (int)(position[xParam[a]][yParam[a]].y);
		}
		
	}
	
	// check if current block hit any other blocks
	public void checkForImpact(FinishedBlocks finishedBlocks){
		
		// set temp variable
		boolean[][] bg = finishedBlocks.getBG();
		
		System.out.println(bg);
		
		// check for impact
		// cycle through 0-3 values (x/yParam)
		// as well as 0-9, 0-19 for x and y of background (bg)
		for (int x=0;x<10;x++)
			for (int y=0;y<20;y++)
				if (bg[x][y]){
					
					// check if any blocks hit
					for (int a=0;a<4;a++)
						if ((position[xParam[a]][yParam[a]].x/length == x) && (position[xParam[a]][yParam[a]].y/length == y))
							hit = true;
						
				}
		
		// if any of them hit, reset their position
		if (hit)
			for (int a=0;a<4;a++){
			
				position[xParam[a]][yParam[a]].x = xPrevious[a];
				position[xParam[a]][yParam[a]].y = yPrevious[a];
				
			}
			
		hit = false;
		bg = null;
		
	}
	
	// create shadow at bottom
	private float findShadow(boolean bg[][], boolean newBlock){
		
		int minCount = 20;
		int count[] = new int[4];
		
		// go through each block
		for (int a=0;a<4;a++)
			try {
				if (blockIndex == 0 && position[xParam[a]][yParam[a]].y/length >= 20)
					count[a]++;
				
				while (!bg[(int)(position[xParam[a]][yParam[a]].x/length)][(int)((position[xParam[a]][yParam[a]].y - count[a]*length)/length) - 1])
						count[a]++;
			} catch (Exception e){
			//	if (blockIndex == 0 && position[xParam[a]][yParam[a]].y/length >= 20;
			}
		
		// make the minCount equal to the smallest count
		for (int a=0;a<4;a++)
			if (count[a] < minCount) 
				minCount = count[a];
		
		return minCount;
		
	}
	
	public void updateRate(int level) {rate -= 0.04625*level;}
	public boolean checkIfAtBottom(){return newBlock;}
	public boolean checkIfPaused(){return paused;}
	public boolean checkForHold(){return hold;}
	public boolean checkIfSpaceHit(){return spaceHit;}
	public boolean checkIfGameOver(){return gameOver;}
	public int getBlockIndex(){return blockIndex;}
	
	// render the blocks
	public void renderBlocks(ShapeRenderer blockRenderer, ShapeRenderer lineRenderer, boolean bg[][], Color color) {
		
		// set distance to next block/color for shadow
		float minCount = findShadow(bg, false);
		lineRenderer.setColor(color);
		
		// render shadow
		for (int a=0;a<4;a++)
			lineRenderer.rect(position[xParam[a]][yParam[a]].x + 1, position[xParam[a]][yParam[a]].y - minCount*length + 1, length - 2, length - 2);
		
		lineRenderer.setColor(Color.BLACK);
		blockRenderer.setColor(color);
		
		// if they had previously pressed space, move actual block down
		if (newBlock)
			for (int a=0;a<4;a++)
				position[xParam[a]][yParam[a]].y -= minCount * length;

		// go through each block, render it
		for (int a=0;a<4;a++){
			blockRenderer.rect(position[xParam[a]][yParam[a]].x, position[xParam[a]][yParam[a]].y, length, length);
			lineRenderer.rect(position[xParam[a]][yParam[a]].x, position[xParam[a]][yParam[a]].y, length, length);
			lineRenderer.rect(position[xParam[a]][yParam[a]].x + 1, position[xParam[a]][yParam[a]].y + 1, length - 2, length - 2);
		}
		
	}
	
}
