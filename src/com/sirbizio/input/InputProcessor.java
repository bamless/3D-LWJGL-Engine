package com.sirbizio.input;

public interface InputProcessor {
	
	boolean mouseClick(int buttonCode);
	
	boolean mouseJustClicked(int buttonCode);
	
	boolean keyPressed(int keyCode);
	
	boolean keyJustPressed(int keyCode);
	
	boolean keyReleased(int keyCode);
	
}
