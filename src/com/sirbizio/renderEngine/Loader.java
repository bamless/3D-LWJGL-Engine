package com.sirbizio.renderEngine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.sirbizio.models.RawModel;
import com.sirbizio.objConverter.ModelData;

/**
 * Loads a 3D model in memory by storing positional data in a VAO
 * 
 * @author Fabrizio
 *
 */
public class Loader {
	/**
	 * Keeps track of all the vaos created in memory
	 */
	private List<Integer> vaos = new ArrayList<>();

	/**
	 * Keeps track of all the vbos created in memory
	 */
	private List<Integer> vbos = new ArrayList<>();

	/**
	 * Keeps track of all the textures
	 */
	private List<Integer> textures = new ArrayList<>();

	/**
	 * Loads the vbo containing the vertices of the model as the no.0 of the vao
	 * attribute list.
	 * 
	 * @param positions
	 * @return RawModel the model
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public RawModel loadToVao(ModelData data) {
		return this.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
	}

	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			InputStream is = Loader.class.getResourceAsStream("/resources/" + fileName + ".png");
			if (is == null)
				throw new RuntimeException(new FileNotFoundException("couldn't fin file res/resources/" + fileName + ".png"));
			texture = TextureLoader.getTexture("PNG", is);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}

	public void cleanUp() {
		for (int vao : vaos)
			GL30.glDeleteVertexArrays(vao);
		for (int vbo : vbos)
			GL15.glDeleteBuffers(vbo);
		for (int texture : textures)
			GL11.glDeleteTextures(texture);
	}

	/**
	 * Creates an empty VAO
	 * 
	 * @return the VAO ID
	 */
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();// creates a new vao
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);// binds it
		return vaoID;
	}

	/**
	 * Stores a set of data in a certain VAO attribute list as VBO
	 * 
	 * @param attributeNumber the number of the attribute list for the data to be stored off
	 * @param data the data that needs to be stored
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();// creates a new vbo
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);// binds it
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		// stores data in it
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		// stores it in the pos 0 of the vao attr. list
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);// unbinds it
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	/**
	 * Loads the index buffer vbo and binds it to the vao
	 * 
	 * @param indices
	 */
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntegerBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

	}

	/**
	 * Converts the int array in a int buffer (needed to store data in the VBO)
	 * 
	 * @param data
	 * @return
	 */
	private IntBuffer storeDataInIntegerBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	/**
	 * Converts the float array in a float buffer (needed to store data in the
	 * VBO)
	 * 
	 * @param data
	 * @return
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
