package com.sirbizio.engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.entities.Camera;
import com.sirbizio.entities.Entity;
import com.sirbizio.models.RawModel;
import com.sirbizio.models.TexuredModel;
import com.sirbizio.renderEngine.DisplayManager;
import com.sirbizio.renderEngine.Loader;
import com.sirbizio.renderEngine.OBJLoader;
import com.sirbizio.renderEngine.Renderer;
import com.sirbizio.shaders.StaticShader;
import com.sirbizio.textures.ModelTexture;

public class MainGameLoop {
    public static void main(String[] args) {
	DisplayManager.createDisplay();
	Loader loader = new Loader();
	StaticShader shader = new StaticShader();
	Renderer renderer = new Renderer(shader);
	
	RawModel model = OBJLoader.loadObjModel("dragon", loader);
	ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
	TexuredModel texturedmodel = new TexuredModel(model, texture);
	
	Entity entity = new Entity(texturedmodel, new Vector3f(100f,0,-50f),0,0,0,2);
	
	Camera camera = new Camera();
	
	while(!Display.isCloseRequested()) {
	    entity.increaseRotation(0, 1, 0);
	    camera.move();
	    
	    //render
	    renderer.prepare();
	    shader.start();
	    shader.loadViewMatrix(camera);
	    renderer.render(entity, shader);
	    shader.stop();
	    
	    DisplayManager.updateDisplay();
	}
	
	shader.cleanUp();
	loader.cleanUp();
	DisplayManager.closeDisplay();
    }
}
