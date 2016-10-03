package com.sirbizio.engineTester;

import java.util.ArrayList;
import java.util.Arrays;
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
	private Terrain terrain;
	private Light sun;
	
	private Loader loader;
	private MasterRenderer renderer;
	private Camera camera;
	
	private Random rand = new Random();
	private boolean isActive = true;
	
	private float stateTime;
	
	@Override
	public void onCreate() {
		//creates loader, renderer and camera
		loader = new Loader();
		
		player = new Player(new TexturedModel(loader.loadToVao(OBJFileLoader.loadOBJ("person")), 
				new ModelTexture(loader.loadTexture("playerTexture"))), 0, 0, 0);
		entities.add(player);
		//creates the camera
		camera = new Camera(player);
		
		renderer = new MasterRenderer(camera);
		
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
		
		//********TERRAINS STUFF**************
		TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		
		terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		
		
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
		
		for(int i = 0 ; i < 50 ; i++) {
			Entity tree = new Entity(treeModel, rand.nextInt(1600), 0, -rand.nextInt(800));
			tree.getPosition().y = terrain.getHeightOfTerrain(tree.getPosition().x, tree.getPosition().z);
			entities.add(tree);
		}
		for(int i = 0 ; i < 100 ; i++) {
			Entity fern = new Entity(fernModel, rand.nextInt(1600), 0, -rand.nextInt(800));
			fern.getPosition().y = terrain.getHeightOfTerrain(fern.getPosition().x, fern.getPosition().z);
			entities.add(fern);
		}
		for(int i = 0 ; i < 350 ; i++) {
			Entity grass = new Entity(grassModel, rand.nextInt(1600), 0, -rand.nextInt(800));
			grass.getPosition().y = terrain.getHeightOfTerrain(grass.getPosition().x, grass.getPosition().z);
			entities.add(grass);
		}
		dragon.getPosition().y = terrain.getHeightOfTerrain(dragon.getPosition().x, dragon.getPosition().z);
		
		//********LIGHT CAMERA N' STUFF*******
		sun = new Light(new Vector3f(0, 0, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
	}

	@Override
	public void render() {
		if(isActive) {
			stateTime += DisplayManager.getDelta();
			camera.move(terrain);
			player.move(terrain);
			dragon.increaseRotation(0, 2 * DisplayManager.getDelta() * 60, 0);
			
			Vector3f sunPos = sun.getPosition();
			sunPos.x = (100000 * (float) Math.cos(stateTime * 0.02)) + 10000;
			sunPos.y = 100000 * (float) Math.sin(stateTime * 0.02); 

			renderer.renderShadowMap(entities, Arrays.asList(new Terrain[] {terrain}), sun);

			for (Entity e : entities)
				renderer.processEntity(e);
			renderer.processTerrain(terrain);

			renderer.render(sun, camera);
		}
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
