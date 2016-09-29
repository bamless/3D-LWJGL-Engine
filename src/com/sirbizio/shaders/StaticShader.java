package com.sirbizio.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.entities.Camera;
import com.sirbizio.entities.Light;
import com.sirbizio.utils.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/resources/shaders/vertexShader.vert";
	private static final String FRAGMENT_FILE = "/resources/shaders/fragmentShader.frag";

	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightPosition;
	private int locationLightColour;
	private int locationShineDamper;
	private int locationReflectivity;
	private int locationUseFakeLighting;
	private int locationSkyColour;
	private int locationTextureSampler;
	private int locationToShadowMapSpace;
	private int locationShadowMap;
	private int locationShadowDistance;
	private int locationMapSize;
	
	public StaticShader() {
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
		locationUseFakeLighting = getUniformLocation("useFakeLighting");
		locationSkyColour = getUniformLocation("skyColour");
		locationToShadowMapSpace = getUniformLocation("toShadowMapSpace");
		locationShadowMap = getUniformLocation("shadowMap");
		locationTextureSampler = getUniformLocation("textureSampler");
		locationShadowDistance = getUniformLocation("shadowDistance");
		locationMapSize = getUniformLocation("mapSize");
	}
	
	public void connectTextureUnits() {
		loadInt(locationTextureSampler, 0);
		loadInt(locationShadowMap, 5);
	}
	
	public void loadSkyColour(float r, float g, float b) {
		loadVector(locationSkyColour, new Vector3f(r, g, b));
	}
	
	public void loadFakeLighting(boolean useFake) {
		loadBoolean(locationUseFakeLighting, useFake);
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
	
	public void loadToShadowSpaceMatrix(Matrix4f mat) {
		loadMatrix(locationToShadowMapSpace, mat);
	}
	
	public void loadToShadowDistance(float distance) {
		loadFloat(locationShadowDistance, distance);
	}
	
	public void loadMapSize(int size) {
		loadFloat(locationMapSize, (float) size);
	}

}
