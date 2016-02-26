package com.sirbizio.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.sirbizio.renderEngine.DisplayManager;
import com.sirbizio.renderEngine.Loader;
import com.sirbizio.renderEngine.MasterRenderer;
import com.sirbizio.terrains.Terrain;
import com.sirbizio.textures.ModelTexture;
import com.sirbizio.textures.TerrainTexture;
import com.sirbizio.textures.TerrainTexturePack;

/**
 * A test application class
 * @author fabrizio
 *
 */
public class Test implements ApplicationListener {

	private Player player;
	private Entity dragon;
	private List<Entity> entities = new ArrayList<>();
	private List<Terrain> terrains = new ArrayList<>();
	private Light sun;
	
	private Loader loader;
	private MasterRenderer renderer;
	private Camera camera;
	
	private Random rand = new Random();
	private boolean isActive = true;
	
	@Override
	public void onCreate() {
		//creates loader, renderer and camera
		loader = new Loader();
		renderer = new MasterRenderer();
		
		//********ENTITIES********************
		ModelData modelData = OBJFileLoader.loadOBJ("dragon");
		RawModel model = loader.loadToVao(modelData);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		texture.setReflectivity(100);
		texture.setShineDamper(200);
		TexturedModel texturedmodel = new TexturedModel(model, texture);
		Entity dragon = new Entity(texturedmodel, new Vector3f(150, 0, -150), 0, 0, 0, 2);
		entities.add(dragon);
		this.dragon = dragon;
		
		player = new Player(new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("person")), 
				new ModelTexture(loader.loadTexture("playerTexture"))), 0, 0, 0);
		//creates the camera
		camera = new Camera(player);
		
		//adds trees ferns and grass
		TexturedModel treeModel = new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("lowPolyTree")), 
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel fernModel = new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("fern")), 
				new ModelTexture(loader.loadTexture("fern")));
		fernModel.getTexture().setHasTransparency(true);
		TexturedModel grassModel = new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("grassModel")), 
				new ModelTexture(loader.loadTexture("grassTexture")));
		grassModel.getTexture().setHasTransparency(true);
		grassModel.getTexture().setUseFakeLighting(true);
		
		for(int i = 0 ; i < 100 ; i++) {
			Entity tree = new Entity(treeModel, rand.nextInt(1600), 0, -rand.nextInt(800));
			entities.add(tree);
		}
		for(int i = 0 ; i < 250 ; i++) {
			Entity fern = new Entity(fernModel, rand.nextInt(1600), 0, -rand.nextInt(800));
			entities.add(fern);
		}
		for(int i = 0 ; i < 3500 ; i++) {
			Entity grass = new Entity(grassModel, rand.nextInt(1600), 0, -rand.nextInt(800));
			entities.add(grass);
		}
		
		//********TERRAINS STUFF**************
		TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain2 = new Terrain(1, -1, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);
		terrains.add(terrain2);
		
		//********LIGHT CAMERA N' STUFF*******
		sun = new Light(new Vector3f(2000, 20000, 20000), new Vector3f(1, 1, 1));
	}

	@Override
	public void render() {
		if(isActive) {
			camera.move();
			player.move();
			dragon.increaseRotation(0, 2 * DisplayManager.getDelta() * 60, 0);
		}
		
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

	@Override
	public void onPause() {
		isActive = false;
	}

	@Override
	public void onResume() {
		isActive = true;
	}

}
