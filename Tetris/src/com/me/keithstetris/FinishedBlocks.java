package com.me.keithstetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

// hold all values for the already placed tetriminos

public class FinishedBlocks {
	
	private boolean bg[][], bgOld[][];
	private float length;
	private int color[][], level, colorOld[][], checkForLine[], maxy, score, lineCount, totalLineCount;
	public Color[] COLOR;
	private boolean stuck;
	private Music lineClear;
			
	public FinishedBlocks (float length, Color[] COLOR) {
		
		bg = new boolean[10][20];
		bgOld = new boolean[10][20];
		color = new int[10][20];
		this.COLOR = COLOR;
		this.length = length;
		checkForLine = new int[20];
		
		maxy = 0;
		score = 0;
		lineCount = 0;
		totalLineCount = 0;
		level = 1;
		stuck = false;
		
		lineClear = Gdx.audio.newMusic(Gdx.files.internal("lineClear.mp3"));
		lineClear.setLooping(false);
		
	}
	
	// add used blocks to the background
	public void addNewBlock(Vector2[][] blockPosition, int shapeIndex, int[] xParam, int[] yParam){
		
		for (int a=0;a<4;a++){
			try {
				bg[(int)(blockPosition[xParam[a]][yParam[a]].x/length)][(int)(blockPosition[xParam[a]][yParam[a]].y/length)] = true;
				color[(int)(blockPosition[xParam[a]][yParam[a]].x/length)][(int)(blockPosition[xParam[a]][yParam[a]].y/length)] = shapeIndex;
			} catch (ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
				stuck = true;
			}
		}
		
	}
	
	public boolean checkIfStuck () {
		return stuck;
	}
	
	// check if a line is made
	public void checkLine (boolean spaceHit) {
		
		boolean musicPlayed = false;
		
		// make a tick to each line's array 
		for (int y=0;y<20;y++)
			for (int x=0;x<10;x++)
				if (bg[x][y])
					checkForLine[y]++;
		
		// go through each line (top to bottom) and delete it
		for (int y=19;y>=0;y--)
			if (checkForLine[y] == 10){
				if (!musicPlayed){
					lineClear.play();
					musicPlayed = true;
				}
				lineCount++;
				totalLineCount++;
				deleteLine(y);
			}
		
		// reset all values
		for (int y=0;y<20;y++) checkForLine[y] = 0;
		
		// update score/level
		updateScore(spaceHit);
		updateLevel();
		
		// reset line count for next time
		lineCount = 0;
		
	}
	
	// delete completed line, shift everything down
	private void deleteLine(int line){
		
		// set maxx/maxy values
		for (int y=0;y<20;y++)
			for (int x=0;x<10;x++)
				if (bg[x][y] && y > maxy) 
					maxy = y;
		
		// set cells in that line to false (erases it)
		for (int x=0;x<10;x++){
			bg[x][line] = false;
			color[x][line] = 0;
		}
		
		// set old values
		bgOld = bg;
		colorOld = color;
		
		// shift all bg/color values down one
		
		for (int y=line;y<19;y++)
			for (int x=0;x<10;x++){
				bg[x][y] = bgOld[x][y+1];
				color[x][y] = colorOld[x][y+1];
			}
		
		// get rid of old values
		bgOld = null;
		colorOld = null;
				
	}
	
	// update the score
	private void updateScore(boolean spaceHit){
		
		// update score from lines
		if (lineCount == 4) score += 800*level;
		else if (lineCount > 0) score += ((lineCount * 2) - 1)*100*level;
		
		if (spaceHit) score += 8;
		else score += 4;
	}
	
	// update the level
	private int updateLevel(){
		level = (totalLineCount / 10) + 1;
		return level;
	}
	
	// getter methods
	public int getScore () { return score; }
	public boolean[][] getBG () { return bg; }
	public int getLevel(){ return level; }
	
	// render all background blocks
	public void renderBlocks(ShapeRenderer blockRenderer, ShapeRenderer lineRenderer) {
		
		for (int x=0;x<10;x++)
			for (int y=0;y<20;y++)
				if (bg[x][y]){
					blockRenderer.setColor(COLOR[color[x][y]]);
					blockRenderer.rect(x*length, y*length, length, length);
					lineRenderer.rect(x*length, y*length, length, length);
					lineRenderer.rect(x*length + 1, y*length + 1, length - 2, length - 2);
				}
		
	}
	
}
