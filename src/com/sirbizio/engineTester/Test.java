package com.sirbizio.engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.application.ApplicationListener;
import com.sirbizio.entities.Camera;
import com.sirbizio.entities.Entity;
import com.sirbizio.entities.Light;
import com.sirbizio.entities.Player;
import com.sirbizio.models.RawModel;
import com.sirbizio.models.TexturedModel;
import com.sirbizio.objConverter.ModelData;
import com.sirbizio.objConverter.OBJFileLoader;
import com.sirbizio.renderEngine.Loader;
import com.sirbizio.renderEngine.MasterRenderer;
import com.sirbizio.terrains.Terrain;
import com.sirbizio.textures.ModelTexture;
import com.sirbizio.textures.TerrainTexture;
import com.sirbizio.textures.TerrainTexturePack;

public class Test implements ApplicationListener {

	private Player player;
	private List<Entity> entities = new ArrayList<>();
	private List<Terrain> terrains = new ArrayList<>();
	private Light sun;
	
	private Loader loader;
	private MasterRenderer renderer;
	
	private Camera camera;
	
	@Override
	public void onCreate() {
		//creates loader, renderer and camera
		loader = new Loader();
		renderer = new MasterRenderer();
		camera = new Camera();
		
		//********ENTITIES********************
		ModelData modelData = OBJFileLoader.loadOBJ("dragon");
		RawModel model = loader.loadToVao(modelData);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		texture.setReflectivity(100);
		texture.setShineDamper(200);
		TexturedModel texturedmodel = new TexturedModel(model, texture);
		Entity dragon = new Entity(texturedmodel, new Vector3f(150, 0, -150), 0, 0, 0, 2);
		entities.add(dragon);
		
		ModelData grassModelData = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassModel = loader.loadToVao(grassModelData);
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grassTexture"));
		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		TexturedModel grassTexModel = new TexturedModel(grassModel, grassTexture);
		Entity grass = new Entity(grassTexModel, new Vector3f(100, 0, -100), 0, 0, 0, 1);
		entities.add(grass);
		
		Entity fern = new Entity(new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("fern")), 
				new ModelTexture(loader.loadTexture("fern"))), new Vector3f(20, 0, -20),  0, 0, 0, 1);
		fern.getModel().getTexture().setHasTransparency(true);
		entities.add(fern);
		
		player = new Player(new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("bunny")), texture), 0, 0, -800);
		
		//********TERRAINS STUFF**************
		TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(1, -1, loader, texturePack, blendMap);
		terrains.add(terrain);
		terrains.add(terrain2);
		
		//********LIGHT CAMERA N' STUFF*******
		sun = new Light(new Vector3f(2000, 20000, 20000), new Vector3f(1, 1, 1));
	}

	@Override
	public void render() {
		camera.move();
		player.move();
		
		renderer.processEntity(player);
		for(Entity e : entities)
			renderer.processEntity(e);
		for(Terrain t : terrains)
			renderer.processTerrain(t);
		
		renderer.render(sun, camera);
	}

	@Override
	public void cleanUp() {
		loader.cleanUp();
		renderer.cleanUp();
	}

}
