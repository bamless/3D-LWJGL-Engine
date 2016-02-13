package com.sirbizio.application;

import com.sirbizio.input.InputProcessor;

public interface Application extends Cleanable {

	void onCreate();
	
	void setInputProcessor(InputProcessor processor);
	
}
