package com.sirbizio.application;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.sirbizio.input.InputAdapter;
import com.sirbizio.input.InputProcessor;
import com.sirbizio.renderEngine.DisplayManager;

public class LWJGLApplication implements Application {
	
	private static LWJGLApplication app;
	private static LWJGLConfiguration config;
	
	private ApplicationListener listener;
	private InputProcessor inputProcessor;
	private boolean hasFocus;
	
	public static void create(ApplicationListener listener, LWJGLConfiguration configuration) {
		if(app != null) throw new RuntimeException("An application already exists. Only one application can be created at any time!");
		config = configuration;
		app = new LWJGLApplication(listener);
	}
	
	public static LWJGLApplication getInstance() {
		if(app == null) throw new RuntimeException("You need to create an application first!");
		return app;
	}
	
	private LWJGLApplication(ApplicationListener listener) {
		this.listener = listener;
		this.inputProcessor = new InputAdapter();
		
		onCreate();
		mainLoop();
		cleanUp();
	}
	
	@Override
	public void onCreate() {
		DisplayManager.createDisplay(config);
		listener.onCreate();
	}
	
	private void mainLoop() {
		while(!Display.isCloseRequested()) {
			if(!Display.isActive() && hasFocus) {
				hasFocus = false;
				onPause();
			} else if(Display.isActive() && !hasFocus) {
				hasFocus = true;
				onResume();
			}
			checkForInputs();
			listener.render();
			DisplayManager.updateDisplay();
		}
	}
	
	@Override
	public void onPause() {
		listener.onPause();
	}

	@Override
	public void onResume() {
		listener.onResume();
	}
	
	private void checkForInputs() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState())
				inputProcessor.keyPressed(Keyboard.getEventKey());
			else
				inputProcessor.keyReleased(Keyboard.getEventKey());
		}
		
		while(Mouse.next() && Mouse.getEventButton() != -1) {
			if(Mouse.getEventButtonState())
				inputProcessor.mouseClicked(Mouse.getEventButton());
			else
				inputProcessor.mouseReleased(Mouse.getEventButton());
		}
	}
	
	@Override
	public void cleanUp() {
		listener.onPause();
		listener.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	public void setInputProcessor(InputProcessor processor) {
		this.inputProcessor = processor;
	}

}