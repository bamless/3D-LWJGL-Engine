package com.sirbizio.engineTester;

import com.sirbizio.application.LWJGLApplication;
import com.sirbizio.application.LWJGLConfiguration;


public class Launcher {
	public static void main(String[] args) {
		LWJGLConfiguration config = new LWJGLConfiguration();
		config.limitFPS = 60;
		config.width = 640;
		config.height = 360;
		config.fullscreen = true;
		config.vsync = false;
		LWJGLApplication.create(new Test(), config);
	}
}
