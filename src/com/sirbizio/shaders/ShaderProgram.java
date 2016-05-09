package com.sirbizio.shaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.application.Cleanable;
import com.sirbizio.utils.StreamUtils;

public abstract class ShaderProgram implements Cleanable {

	/**Shader's IDs*/
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	/**Buffer used for the loading of matrices*/
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	/**
	 * Use to get all uniform variable locations.
	 */
	protected abstract void getAllUniformLocations();

	/**
	 * Gets the uniform variable
	 * 
	 * @param uniformNam the name
	 * @return the uniform variable ID
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	/**Enables the shader for rendering*/
	public void start() {
		GL20.glUseProgram(programID);
	}

	/**Disables the shader*/
	public void stop() {
		GL20.glUseProgram(0);
	}

	@Override
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	/**Binds all the VAO attributes*/
	protected abstract void bindAttributes();

	/**
	 * Binds a VAO attribute
	 * @param attribute the number of the attribute that will be enalble
	 * @param variableName the name of the attribute
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}

	/**Loads an int in a uniform*/
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	/**
	 * Loads a float into an uniform variable
	 * 
	 * @param location
	 *            the location of the variable
	 * @param value
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}

	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	protected void loadBoolean(int location, boolean value) {
		int toLoad = value ? 1 : 0;
		GL20.glUniform1i(location, toLoad);
	}

	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	/**
	 * Loads a vertex and a fragment shader
	 * 
	 * @param file the shader's code path
	 * @param type the shader's type
	 * @return the shaderID
	 * @throws ShaderNotCompiledException 
	 */
	protected int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		InputStream is = ShaderProgram.class.getResourceAsStream(file);
		if(is == null)
			throw new RuntimeException(new FileNotFoundException("Couldn't find file res"+file));
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			String line;
			while ((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			throw new ShaderNotCompiledException(shaderID, GL20.glGetShaderInfoLog(shaderID, 500));
		}
		return shaderID;
	}
}
