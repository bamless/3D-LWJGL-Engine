package com.sirbizio.input;

public interface InputProcessor {
	
	boolean mouseClicked(int buttonCode);
	
	boolean mouseReleased(int buttonCode);
	
	boolean keyPressed(int keyCode);
	
	boolean keyReleased(int keyCode);
	
}
