package com.sirbizio.renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import com.sirbizio.application.LWJGLConfiguration;

public class DisplayManager {
	
	private static long lastFrameTime;
	private static float delta;
	private static LWJGLConfiguration config;

	public static void createDisplay(LWJGLConfiguration config) {
		DisplayManager.config = config;
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try {
	        DisplayMode displayMode = null;
	        DisplayMode[] modes = Display.getAvailableDisplayModes();

	        for (int i = 0; i < modes.length; i++) {
	            if (modes[i].getWidth() == config.width && modes[i].getHeight() == config.height 
	           		 	&& modes[i].isFullscreenCapable() && modes[i].getFrequency() == 60) {
	                   displayMode = modes[i];
	            }
	        }
			Display.setDisplayMode(displayMode);
			Display.setFullscreen(config.fullscreen);
			Display.setVSyncEnabled(config.vsync);
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("GameEngineTest");
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		}

		GL11.glViewport(0, 0, config.width, config.height);
		lastFrameTime = getCurrentTime();
	}

	public static void updateDisplay() {
		Display.sync(config.limitFPS);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
		if(config.logFPS) System.out.println(1/delta);
	}
	
	public static float getDelta() {
		return delta;
	}

	public static void closeDisplay() {
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime()*1000 / Sys.getTimerResolution();
	}

}
