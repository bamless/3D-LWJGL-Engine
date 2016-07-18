package com.sirbizio.shaders;

/**
 * Shader for rendering realistic water.
 * W.I.P
 * @author fabrizio
 *
 */
public class WaterShader extends ShaderProgram {
	
	private final static String VERTEX = "tobeadded";
	private final static String FRAGMENT = "tobeadded";

	public WaterShader(String vertexFile, String fragmentFile) {
		super(VERTEX, FRAGMENT);
	}

	@Override
	protected void getAllUniformLocations() {
		//Add water shine uniform
		//Add water displacement factor uniform
		//Add water fersnel effect uniform
	}

	@Override
	protected void bindAttributes() {
		//Bind displacement texture
		//Bind normalmap texture
	}
	

}
