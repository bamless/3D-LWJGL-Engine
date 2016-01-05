package com.sirbizio.engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.entities.Camera;
import com.sirbizio.entities.Entity;
import com.sirbizio.models.RawModel;
import com.sirbizio.models.TexuredModel;
import com.sirbizio.renderEngine.DisplayManager;
import com.sirbizio.renderEngine.Loader;
import com.sirbizio.renderEngine.Renderer;
import com.sirbizio.shaders.StaticShader;
import com.sirbizio.textures.ModelTexture;

public class MainGameLoop {
    public static void main(String[] args) {
	DisplayManager.createDisplay();
	
	float[] vertices = {			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,0.5f,-0.5f,		
			
			-0.5f,0.5f,0.5f,	
			-0.5f,-0.5f,0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			0.5f,0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			-0.5f,-0.5f,0.5f,	
			-0.5f,0.5f,0.5f,
			
			-0.5f,0.5f,0.5f,
			-0.5f,0.5f,-0.5f,
			0.5f,0.5f,-0.5f,
			0.5f,0.5f,0.5f,
			
			-0.5f,-0.5f,0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f
			
	};
	
	float[] textureCoords = {	
			0,0,
			0,1,
			1,1,
			1,0,			
			0,0,
			0,1,
			1,1,
			1,0,			
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0

			
	};
	
	int[] indices = {
			0,1,3,	
			3,1,2,	
			4,5,7,
			7,5,6,
			8,9,11,
			11,9,10,
			12,13,15,
			15,13,14,	
			16,17,19,
			19,17,18,
			20,21,23,
			23,21,22
	};
	
	Loader loader = new Loader();
	StaticShader shader = new StaticShader();
	Renderer renderer = new Renderer(shader);
	
	RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
	ModelTexture texture = new ModelTexture(loader.loadTexture("image"));
	TexuredModel texturedmodel = new TexuredModel(model, texture);
	
	Entity entity = new Entity(texturedmodel, new Vector3f(0f,0,-5f),0,0,0,2);
	
	Camera camera = new Camera();
	
	while(!Display.isCloseRequested()) {
	    entity.increaseRotation(03f, 1.5f, 1);
	    camera.move();
	    renderer.prepare();
	    //render
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
