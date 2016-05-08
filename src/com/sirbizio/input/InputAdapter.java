package com.sirbizio.input;

import org.lwjgl.input.Keyboard;

import com.sirbizio.application.LWJGLApplication;

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
		if(keyCode == Keyboard.KEY_ESCAPE) {
			LWJGLApplication.exit();
		}
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode) {
		return false;
	}

}
