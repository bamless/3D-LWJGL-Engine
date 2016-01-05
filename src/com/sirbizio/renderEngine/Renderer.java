package com.sirbizio.renderEngine;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.sirbizio.entities.Entity;
import com.sirbizio.models.RawModel;
import com.sirbizio.models.TexuredModel;
import com.sirbizio.shaders.StaticShader;
import com.sirbizio.toolbox.Maths;

/**
 * Class that renders models
 * @author Fabrizio
 *
 */
public class Renderer {
    
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    
    private Matrix4f projectionMatrix;
    
    public Renderer(StaticShader shader) {
	createProjectionMatrix();
	shader.start();
	shader.loadProjectionMatrix(projectionMatrix);
	shader.stop();
    }
    
    /**
     * Prepare OpenGL to render the model (cleans the screen)
     */
    public void prepare() {
	GL11.glEnable(GL11.GL_DEPTH_TEST);//enables the depth test
	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);//clears the buffers
	GL11.glClearColor(1, 0, 0, 1);//clears the screen
    }
     /**
      * Renders a {@link RawModel}
      * @param model the model to be rendered
      */
    public void render(Entity entity, StaticShader shader) {
	TexuredModel model = entity.getModel();
	RawModel rawModel = model.getRawModel();
	GL30.glBindVertexArray(rawModel.getVaoID());//binds the vao of the RawModel
	
	GL20.glEnableVertexAttribArray(0);//enables the line 0 of the attribute list
	GL20.glEnableVertexAttribArray(1);
	Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), 
		entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
	shader.loadTransformationMatrix(transformationMatrix);
	GL13.glActiveTexture(GL13.GL_TEXTURE0);
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	//renders the model
	GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	GL20.glDisableVertexAttribArray(0);//disables
	GL20.glDisableVertexAttribArray(1);
	
	GL30.glBindVertexArray(0);//unbids
    }
    
    private void createProjectionMatrix() {
	float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
	float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
	float x_scale = y_scale / aspectRatio;
	float frustum_length = FAR_PLANE - NEAR_PLANE;
	
	projectionMatrix = new Matrix4f();
	projectionMatrix.m00 = x_scale;
	projectionMatrix.m11 = y_scale;
	projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
	projectionMatrix.m23 = -1;
	projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
	projectionMatrix.m33 = 0;
    }
    
}
