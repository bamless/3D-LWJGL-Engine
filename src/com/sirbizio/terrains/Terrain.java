package com.sirbizio.terrains;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.models.RawModel;
import com.sirbizio.renderEngine.Loader;
import com.sirbizio.textures.TerrainTexture;
import com.sirbizio.textures.TerrainTexturePack;
import com.sirbizio.toolbox.Maths;
import com.sirbizio.toolbox.StreamUtils;

public class Terrain {
	
	private static final float SIZE = 800;
	private final float MAX_HEIGHT = 100;
	//private final float MIN_HEIGHT = -40;
	private final float MAX_PIXEL_COLOUR = 256 * 256 * 256;	
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	private RawModel generateTerrain(Loader loader, String heightMap) {
		
		InputStream is = getClass().getResourceAsStream("/resources/"+heightMap+".png");
		if(is == null) throw new RuntimeException(new FileNotFoundException("Couldn't find file resources/"+heightMap+".png"));
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			StreamUtils.closeQuietly(is);
		}
		
		int vetexCount = image.getHeight();
		heights = new float[vetexCount][vetexCount];
		
		int count = vetexCount * vetexCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(vetexCount-1)*(vetexCount-1)];
		int vertexPointer = 0;
		for(int i=0 ; i<vetexCount ; i++){
			for(int j=0;j<vetexCount;j++){
				vertices[vertexPointer*3] = (float)j/((float)vetexCount - 1) * SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)vetexCount - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)vetexCount - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)vetexCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<vetexCount-1;gz++){
			for(int gx=0;gx<vetexCount-1;gx++){
				int topLeft = (gz*vetexCount)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*vetexCount)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private Vector3f calculateNormal(int x, int y, BufferedImage image) {
		final float heighL = getHeight(x - 1, y, image);
		final float heighR = getHeight(x + 1, y, image);
		final float heighD = getHeight(x, y - 1, image);
		final float heighU = getHeight(x, y + 1, image);
		Vector3f normal = new Vector3f(heighL - heighR, 2f, heighD - heighU);
		normal.normalise();
		return normal;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}
	
	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ) {
		final float terrainX = worldX - this.x;
		final float terrainZ = worldZ - this.z;
		final float gridSquareSize = SIZE / ((float) heights.length - 1);
		final int gridX = (int) Math.floor(terrainX / gridSquareSize);
		final int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if(gridX >= heights.length - 1 || gridZ >= heights[0].length - 1 || gridX < 0 || gridZ < 0)
			return 0;
		final float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		final float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float height;
		if (xCoord <= (1-zCoord)) {
			height = Maths.baryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			height = Maths.baryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return height;
	}
	
	private float getHeight(int x, int y, BufferedImage image) {
		if(x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight())
			return 0;
		float height = image.getRGB(x, y);
		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;
		return height;
	}
	
}
