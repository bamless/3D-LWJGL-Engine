package com.sirbizio.engineTester;

import com.sirbizio.application.LWJGLApplication;
import com.sirbizio.application.LWJGLConfiguration;

public class Launcher {
	public static void main(String[] args) {
		LWJGLConfiguration config = new LWJGLConfiguration();
		config.limitFPS = 2500;
		config.logFPS = true;
		LWJGLApplication.create(new Test(), config);
	}
}
