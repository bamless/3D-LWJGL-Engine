package com.sirbizio.engineTester;

import com.sirbizio.application.LWJGLApplication;
import com.sirbizio.application.LWJGLConfiguration;

public class Launcher {
	public static void main(String[] args) {
		LWJGLConfiguration config = new LWJGLConfiguration();
		config.limitFPS = 60;
		config.width = 1280;
		config.height = 720;
		config.fullscreen = false;
		config.vsync = true;
		LWJGLApplication.create(new Test(), config);
	}
}
