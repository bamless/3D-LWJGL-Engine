package com.sirbizio.input;

import org.lwjgl.input.Keyboard;

public class InputAdapter implements InputProcessor {

	@Override
	public boolean mouseClicked(int buttonCode) {
		System.out.println("BUTTON PRESSED " + buttonCode);
		return false;
	}
	
	@Override
	public boolean mouseReleased(int buttonCode) {
		System.out.println("BUTTON RELEASED " + buttonCode);
		return false;
	}

	@Override
	public boolean keyPressed(int keyCode) {
		System.out.println("KEY PRESSED " + Keyboard.getKeyName(keyCode));
		return false;
	}

	@Override
	public boolean keyReleased(int keyCode) {
		System.out.println("KEY RELEASED " + Keyboard.getKeyName(keyCode));
		return false;
	}

}
