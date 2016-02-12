package com.sirbizio.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.entities.Camera;
import com.sirbizio.entities.Light;
import com.sirbizio.toolbox.Maths;

public class TerrainShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/resources/shaders/terrainVertexShader.vert";
	private static final String FRAGMENT_FILE = "/resources/shaders/terrainFragmentShader.frag";

	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightPosition;
	private int locationLightColour;
	private int locationShineDamper;
	private int locationReflectivity;
	private int locationSkyColour;

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = getUniformLocation("transformationMatrix");
		locationProjectionMatrix = getUniformLocation("projectionMatrix");
		locationViewMatrix = getUniformLocation("viewMatrix");
		locationLightPosition = getUniformLocation("lightPosition");
		locationLightColour = getUniformLocation("lightColour");
		locationShineDamper = getUniformLocation("shineDamper");
		locationReflectivity = getUniformLocation("reflectivity");
		locationSkyColour = getUniformLocation("skyColour");
	}
	
	public void loadSkyColour(float r, float g, float b) {
		loadVector(locationSkyColour, new Vector3f(r, g, b));
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		loadFloat(locationShineDamper, damper);
		loadFloat(locationReflectivity, reflectivity);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		loadMatrix(locationTransformationMatrix, matrix);
	}

	public void loadLight(Light light) {
		loadVector(locationLightPosition, light.getPosition());
		loadVector(locationLightColour, light.getColour());
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(locationViewMatrix, viewMatrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		loadMatrix(locationProjectionMatrix, matrix);
	}
	
}
