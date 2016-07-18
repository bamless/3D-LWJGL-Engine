package com.sirbizio.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.entities.Camera;
import com.sirbizio.entities.Light;
import com.sirbizio.utils.Maths;

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
	private int locationBackgroundTexture;
	private int locationRTexture;
	private int locationGTexture;
	private int locationBTexture;
	private int locationBlendMap;
	private int locationToShadowMapSpace;
	private int locationShadowMap;

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
		locationBackgroundTexture = getUniformLocation("backgroundTexture");
		locationRTexture = getUniformLocation("rTexture");
		locationGTexture = getUniformLocation("gTexture");
		locationBTexture = getUniformLocation("bTexture");
		locationBlendMap = getUniformLocation("blendMap");
		locationToShadowMapSpace = getUniformLocation("toShadowMapSpace");
		locationShadowMap = getUniformLocation("shadowMap");
	}
	
	public void connectTextureUnits() {
		loadInt(locationBackgroundTexture, 0);
		loadInt(locationRTexture, 1);
		loadInt(locationGTexture, 2);
		loadInt(locationBTexture, 3);
		loadInt(locationBlendMap, 4);
		loadInt(locationShadowMap, 5);
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
	
	public void loadToShadowSpaceMatrix(Matrix4f mat) {
		loadMatrix(locationToShadowMapSpace, mat);
	}
	
}
