package com.sirbizio.engineTester;

import com.sirbizio.application.LWJGLApplication;
import com.sirbizio.application.LWJGLConfiguration;


public class Launcher {
	public static void main(String[] args) {
		LWJGLConfiguration config = new LWJGLConfiguration();
		config.limitFPS = 60;
		config.width = 1920;
		config.height = 1080;
		config.fullscreen = true;
		config.vsync = true;
		LWJGLApplication.create(new Test(), config);
	}
}
