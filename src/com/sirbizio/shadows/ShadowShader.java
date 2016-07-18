package com.sirbizio.shadows;

import org.lwjgl.util.vector.Matrix4f;

import com.sirbizio.shaders.ShaderProgram;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/resources/shaders/shadowVertexShader.vert";
	private static final String FRAGMENT_FILE = "/resources/shaders/shadowFragmentShader.frag";
	
	private int location_mvpMatrix;

	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
		
	}
	
	protected void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "in_position");
		bindAttribute(1, "in_textureCoords");
	}

}
