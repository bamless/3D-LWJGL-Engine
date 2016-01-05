package com.sirbizio.shaders;

import org.lwjgl.util.vector.Matrix4f;

import com.sirbizio.entities.Camera;
import com.sirbizio.toolbox.Maths;

public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/resources/vertexShader.txt";
    private static final String FRAGMENT_FILE = "/resources/fragmentShader.txt";
    
    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    
    public StaticShader() {
	super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
	super.bindAttribute(0, "position");
	super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
	locationTransformationMatrix = getUniformLocation("transformationMatrix");
	locationProjectionMatrix = getUniformLocation("projectionMatrix");
	locationViewMatrix = getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
	loadMatrix(locationTransformationMatrix, matrix);
    }
    
    public void loadViewMatrix(Camera camera) {
	    Matrix4f viewMatrix = Maths.createViewMatrix(camera);
	    loadMatrix(locationViewMatrix, viewMatrix);
    }
    
    public void loadProjectionMatrix(Matrix4f matrix) {
	loadMatrix(locationProjectionMatrix, matrix);
    }
    
}
