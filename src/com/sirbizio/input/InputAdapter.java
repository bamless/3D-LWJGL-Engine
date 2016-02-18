package com.sirbizio.input;

public class InputAdapter implements InputProcessor {

	@Override
	public boolean mouseClicked(int buttonCode) {
		return false;
	}
	
	@Override
	public boolean mouseReleased(int buttonCode) {
		return false;
	}

	@Override
	public boolean keyPressed(int keyCode) {
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode) {
		return false;
	}

}
