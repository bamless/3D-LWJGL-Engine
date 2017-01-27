package com.sirbizio.application;

/**
 * Context app that hold the various app modules (for now only the core application)
 * in order to grant application wide access
 * @author fabrizio
 *
 */
public final class Context {
	
	protected static Application app;
	
	private Context() {
	}
	
	public static Application app() {
		return app;
	}
}
