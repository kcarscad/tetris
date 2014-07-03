package com.me.keithstetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// class for the next piece displayed at bottom right

public class NextPiece {
	
	private float worldWidth, xadj, yadj;
	private int panelWidth, xParam[], yParam[];
	private int length;
	
	// constructor
	public NextPiece (int panelWidth, int length) {
		
		worldWidth = Gdx.graphics.getWidth();
		this.panelWidth = panelWidth;
		this.length = length;
		xParam = new int[4];
		yParam = new int[4];
		
	}
	
	// called from gamescreen, update the piece
	public void updateNextPiece (ShapeRenderer blockRenderer, ShapeRenderer lineRenderer, Block block, int shapeIndex) {
		
		// set adjustments
		if (shapeIndex == 3) xadj = length;
		else if (shapeIndex == 0) yadj = -length/2;
		else xadj = length/2;
		
		// set parameters
		for (int a=0;a<4;a++){
			xParam[a] = block.SHAPES[0][shapeIndex][0][a];
			yParam[a] = block.SHAPES[0][shapeIndex][1][a];
		}
		
		// draw it, reset vals
		for (int a=0;a<4;a++){
			blockRenderer.rect(worldWidth - panelWidth + length + xParam[a]*length + xadj,(float)((yParam[a] + 1.5)*length) + yadj, length, length);
			lineRenderer.rect(worldWidth - panelWidth + length + xParam[a]*length + xadj,(float)((yParam[a] + 1.5)*length) + yadj, length, length);
			lineRenderer.rect(worldWidth - panelWidth + length + xParam[a]*length + xadj + 1,(float)((yParam[a] + 1.5)*length) + yadj + 1, length - 2, length - 2);
		} xadj = 0; yadj = 0;
		
	}
	
}

