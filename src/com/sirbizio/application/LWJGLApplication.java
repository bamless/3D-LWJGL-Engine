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
	
	private int lastMouseButton;
	
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
			checkForInputs();
			listener.render();
			DisplayManager.updateDisplay();
		}
	}
	
	private void checkForInputs() {
		if(!Keyboard.isRepeatEvent())
			inputProcessor.keyJustPressed(Keyboard.getEventKey());
		else
			inputProcessor.keyPressed(Keyboard.getEventKey());
		
		final int mouseButton = Mouse.getEventButton();
		if(mouseButton == lastMouseButton) {
			lastMouseButton = mouseButton;
			inputProcessor.mouseClick(mouseButton);
		} else if(mouseButton != lastMouseButton) {
			lastMouseButton = mouseButton;
			inputProcessor.mouseJustClicked(mouseButton);
		} else if(mouseButton < 0) {
			lastMouseButton = mouseButton;
		}
	}
	
	@Override
	public void cleanUp() {
		listener.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	public void setInputProcessor(InputProcessor processor) {
		this.inputProcessor = processor;
	}

}
