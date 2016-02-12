package com.sirbizio.engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.entities.Camera;
import com.sirbizio.entities.Entity;
import com.sirbizio.entities.Light;
import com.sirbizio.entities.Player;
import com.sirbizio.models.RawModel;
import com.sirbizio.models.TexturedModel;
import com.sirbizio.objConverter.ModelData;
import com.sirbizio.objConverter.OBJFileLoader;
import com.sirbizio.renderEngine.DisplayManager;
import com.sirbizio.renderEngine.Loader;
import com.sirbizio.renderEngine.MasterRenderer;
import com.sirbizio.terrains.Terrain;
import com.sirbizio.textures.ModelTexture;
import com.sirbizio.textures.TerrainTexture;
import com.sirbizio.textures.TerrainTexturePack;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		//********ENTITIES********************
		ModelData modelData = OBJFileLoader.loadOBJ("dragon");
		RawModel model = loader.loadToVao(modelData);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		texture.setReflectivity(100);
		texture.setShineDamper(200);
		TexturedModel texturedmodel = new TexturedModel(model, texture);
		Entity dragon = new Entity(texturedmodel, new Vector3f(150, 0, -150), 0, 0, 0, 2);
		
		ModelData grassModelData = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassModel = loader.loadToVao(grassModelData);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grassTexture"));
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		TexturedModel grassTexModel = new TexturedModel(grassModel, grassTexture);
		Entity grass = new Entity(grassTexModel, new Vector3f(100, 0, -100), 0, 0, 0, 1);
		
		Entity fern = new Entity(new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("fern")), 
				new ModelTexture(loader.loadTexture("fern"))), new Vector3f(20, 0, -20),  0, 0, 0, 1);
		fern.getModel().getTexture().setHasTransparency(true);
		
		Player player = new Player(new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("bunny")), texture), 0, 0, 0);
		
		//********TERRAINS STUFF**************
		TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(1, -1, loader, texturePack, blendMap);
		
		//********LIGHT CAMERA N' STUFF*******
		Light light = new Light(new Vector3f(2000, 20000, 20000), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();

		MasterRenderer renderer = new MasterRenderer();
		
		//********GAME LOOP*******************
		while (!Display.isCloseRequested()) {
			camera.move();

			player.move();
			dragon.increaseRotation(0, 2, 0);
			
			//process entities and terrains
			renderer.processEntity(player);
			renderer.processEntity(dragon);
			renderer.processEntity(grass);
			renderer.processEntity(fern);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			// render
			renderer.render(light, camera);

			DisplayManager.updateDisplay();
		}

		//********CLEAN UP THINGS**************
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
