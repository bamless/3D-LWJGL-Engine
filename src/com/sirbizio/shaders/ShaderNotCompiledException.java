package com.sirbizio.shaders;

public class ShaderNotCompiledException extends RuntimeException {

	private static final long serialVersionUID = 717349924485936436L;
	
	public ShaderNotCompiledException(int shaderID, String log) {
		super(String.format("Couldn't compile shader %d:\n%s", shaderID, log.replace("\n\n", "")));
	}

}
