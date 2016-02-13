package com.sirbizio.input;

public class InputAdapter implements InputProcessor {

	@Override
	public boolean mouseClick(int buttonCode) {
		return false;
	}

	@Override
	public boolean mouseJustClicked(int buttonCode) {
		return false;
	}

	@Override
	public boolean keyPressed(int keyCode) {
		return false;
	}

	@Override
	public boolean keyJustPressed(int keyCode) {
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode) {
		return false;
	}

}
