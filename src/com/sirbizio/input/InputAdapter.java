package com.sirbizio.input;

import org.lwjgl.input.Keyboard;

import com.sirbizio.application.Context;

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
			Context.app().exit();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode) {
		return false;
	}

}
