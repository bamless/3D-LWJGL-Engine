package com.sirbizio.application;

import com.sirbizio.input.InputProcessor;

/**
 * Models an application.
 * @author Fabrizio
 *
 */
public interface Application extends Cleanable {

	/**Called when the application is first instantiated*/
	void onCreate();
	
	/**Called when the application loses focus*/
	void onPause();
	
	/**Called when the application gains focus*/
	void onResume();
	
	/**Sets a {@link InputProcessor} */
	void setInputProcessor(InputProcessor processor);
	
}
