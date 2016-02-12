package com.sirbizio.engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.entities.Camera;
import com.sirbizio.entities.Entity;
import com.sirbizio.entities.Light;
import com.sirbizio.models.RawModel;
import com.sirbizio.models.TexturedModel;
import com.sirbizio.renderEngine.DisplayManager;
import com.sirbizio.renderEngine.Loader;
import com.sirbizio.renderEngine.MasterRenderer;
import com.sirbizio.renderEngine.OBJLoader;
import com.sirbizio.terrains.Terrain;
import com.sirbizio.textures.ModelTexture;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();

		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		texture.setReflectivity(100);
		texture.setShineDamper(200);
		TexturedModel texturedmodel = new TexturedModel(model, texture);
		Entity dragon = new Entity(texturedmodel, new Vector3f(150, 0, -150), 0, 0, 0, 2);
		
		RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grassTexture"));
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		TexturedModel grassTexModel = new TexturedModel(grassModel, grassTexture);
		Entity grass = new Entity(grassTexModel, new Vector3f(100, 0, -100), 0, 0, 0, 1);
		
		Entity fern = new Entity(new TexturedModel(OBJLoader.loadObjModel("fern", loader), 
				new ModelTexture(loader.loadTexture("fern"))), new Vector3f(20, 0, -20),  0, 0, 0, 1);
		fern.getModel().getTexture().setHasTransparency(true);
		
		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		
		Light light = new Light(new Vector3f(2000, 20000, 20000), new Vector3f(1, 1, 1));
		
		Camera camera = new Camera();

		MasterRenderer renderer = new MasterRenderer();
		
		while (!Display.isCloseRequested()) {
			camera.move();

			dragon.increaseRotation(0, 2, 0);
			
			//process entities and terrains
			renderer.processEntity(dragon);
			renderer.processEntity(grass);
			renderer.processEntity(fern);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			// render
			renderer.render(light, camera);

			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
