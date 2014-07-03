package com.me.keithstetris;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

// handling input when in game screen

public class InputHandler implements InputProcessor {
	
	public boolean moveLeft, moveRight, moveDown, space, rotate, quit, piece[], paused, hold;
	
	public InputHandler() {
		
		moveLeft = moveRight = moveDown = space = rotate = quit = paused = hold = false;
		piece = new boolean[7];
		
	}
	
	@Override
	public boolean keyDown(int key) {
		
		if (key == Input.Keys.DPAD_LEFT) {
			moveLeft = true;
			moveRight = false;
		}
		
		if (key == Input.Keys.DPAD_RIGHT){
			moveLeft = false;
			moveRight = true;
		}
		
		if (key == Input.Keys.DPAD_DOWN) moveDown = true;
	
		if (key == Input.Keys.SPACE) space = true;
		
		if (key == Input.Keys.DPAD_UP) rotate = true;
		
		if (key == Input.Keys.Q) quit = true;
		
		if (key == Input.Keys.P) paused = true; 
		
		if (key == Input.Keys.NUM_0) piece[0] = true;
		if (key == Input.Keys.NUM_1) piece[1] = true;
		if (key == Input.Keys.NUM_2) piece[2] = true;
		if (key == Input.Keys.NUM_3) piece[3] = true;
		if (key == Input.Keys.NUM_4) piece[4] = true;
		if (key == Input.Keys.NUM_5) piece[5] = true;
		if (key == Input.Keys.NUM_6) piece[6] = true;
		
		if (key == Input.Keys.SHIFT_LEFT) hold = true;

		return false;
		
	}

	@Override
	public boolean keyUp(int key) {
		
		if (key == Input.Keys.DPAD_LEFT) moveLeft = false;
			
		if (key == Input.Keys.DPAD_RIGHT) moveRight = false;
			
		if (key == Input.Keys.DPAD_DOWN) moveDown = false;
		
		if (key == Input.Keys.SPACE) space = false;
		
		if (key == Input.Keys.DPAD_UP) rotate = false;
		
		if (key == Input.Keys.Q) quit = false;
		
		if (key == Input.Keys.P) paused = false; 
		
		if (key == Input.Keys.NUM_0) piece[0] = false;
		if (key == Input.Keys.NUM_1) piece[1] = false;
		if (key == Input.Keys.NUM_2) piece[2] = false;
		if (key == Input.Keys.NUM_3) piece[3] = false;
		if (key == Input.Keys.NUM_4) piece[4] = false;
		if (key == Input.Keys.NUM_5) piece[5] = false;
		if (key == Input.Keys.NUM_6) piece[6] = false;
		
		if (key == Input.Keys.SHIFT_LEFT) hold = false; 
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
