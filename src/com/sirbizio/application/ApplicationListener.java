package com.sirbizio.application;

/**
 * Models an applicationListener. This is the entry point in the framework.
 * You need to instantiate the class that implements this interface in the launcher, in the constructor
 * of the Application.
 * @author Fabrizio
 *
 */
public interface ApplicationListener extends Cleanable {
	
	/**Called when the listener is created. All field initialization must be done here*/
	void onCreate();
	
	/**Called when the Display loses focus and before the application is closed*/
	void onPause();
	
	/**Called when the Display gains focus and after the creation of the Application*/
	void onResume();
	
	/**Called every frame*/
	void render();
	
}
