package com.sirbizio.application;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.sirbizio.input.InputAdapter;
import com.sirbizio.input.InputProcessor;
import com.sirbizio.renderEngine.DisplayManager;

public class LWJGLApplication implements Application {
	
	private static LWJGLApplication app;
	private static LWJGLConfiguration config;
	
	private ApplicationListener listener;
	private InputProcessor inputProcessor;
	private boolean hasFocus;
	private boolean exit;
	
	public static void create(ApplicationListener listener, LWJGLConfiguration configuration) {
		if(app != null) throw new RuntimeException("An application already exists. Only one application can be created at any time!");
		config = configuration;
		app = new LWJGLApplication(listener);
		app.start();
	}
	
	public static LWJGLApplication getInstance() {
		if(app == null) throw new RuntimeException("You need to create an application first!");
		return app;
	}
	
	private LWJGLApplication(ApplicationListener listener) {
		this.listener = listener;
		this.inputProcessor = new InputAdapter();
	}
	
	private void start() {
		app.onCreate();
		app.mainLoop();
		app.cleanUp();
	}
	
	@Override
	public void onCreate() {
		DisplayManager.createDisplay(config);
		if(config.renderLines) GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		listener.onCreate();
	}
	
	private void mainLoop() {
		while(!Display.isCloseRequested()) {
			if(exit) break;
			
			//checks for the onPause/onResume callbacks
			if(!Display.isActive() && hasFocus) {
				hasFocus = false;
				onPause();
			} else if(Display.isActive() && !hasFocus) {
				hasFocus = true;
				onResume();
			}
			
			//checks for input in the inputprocessor
			checkForInputs();
			
			//renders the listener
			listener.render();
			
			//updates the display
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
	
	public static void exit() {
		app.exit = true;
	}
	
	public void setInputProcessor(InputProcessor processor) {
		this.inputProcessor = processor;
	}

}
