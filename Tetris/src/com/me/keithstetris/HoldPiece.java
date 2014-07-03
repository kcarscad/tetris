package com.me.keithstetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HoldPiece {
	
	private int length, xParam[], yParam[], panelWidth, xadj, yadj;
	private float worldWidth;
	
	public HoldPiece(int shapeIndex, int panelWidth, int length){
		worldWidth = Gdx.graphics.getWidth();
		this.panelWidth = panelWidth;
		this.length = length;
		xParam = new int[4];
		yParam = new int[4];
	}
	
	public void updateHoldPiece(ShapeRenderer blockRenderer, ShapeRenderer lineRenderer, Block block, int blockIndex, Color[] COLOR){
		
		if (0 <= blockIndex && blockIndex <= 6){
			// set adjustments
			if (blockIndex == 3) xadj = length;
			else if (blockIndex == 0) yadj = -length/2;
			else xadj = length/2;
			
			// set parameters
			for (int a=0;a<4;a++){
				xParam[a] = block.SHAPES[0][blockIndex][0][a];
				yParam[a] = block.SHAPES[0][blockIndex][1][a];
			}
			
			blockRenderer.setColor(COLOR[blockIndex]);
			
			// draw it, reset vals
			for (int a=0;a<4;a++){
				blockRenderer.rect(worldWidth - panelWidth + length + xParam[a]*length + xadj,(float)((yParam[a] + 7.1)*length) + yadj, length, length);
				lineRenderer.rect(worldWidth - panelWidth + length + xParam[a]*length + xadj,(float)((yParam[a] + 7.1)*length) + yadj, length, length);
				lineRenderer.rect(worldWidth - panelWidth + length + xParam[a]*length + xadj + 1,(float)((yParam[a] + 7.1)*length) + yadj + 1, length - 2, length - 2);
			} xadj = 0; yadj = 0;
		}
		
	}
	
}