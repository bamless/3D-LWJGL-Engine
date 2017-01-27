package com.sirbizio.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.models.TexturedModel;
import com.sirbizio.renderEngine.DisplayManager;
import com.sirbizio.terrains.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	public static final float GRAVITY = -90;
	private static final float JUMP_POWER = 43;
	
	private float currentSpeed;
	private float currentTurnSpeed;
	private float upwardSpeed;
	
	private boolean isInAir;
	
	public Player(TexturedModel model, float x, float y, float z) {
		super(model, new Vector3f(x, y, z), 0, 0, 0, 1);
	}

	public void move(Terrain terrain) {
		chkeckInputs();
		increaseRotation(0, currentTurnSpeed * DisplayManager.getDelta(), 0);
		final float distance = currentSpeed * DisplayManager.getDelta();
		final float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
		final float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
		increasePosition(dx, 0, dz);
		upwardSpeed += GRAVITY * DisplayManager.getDelta();
		increasePosition(0, upwardSpeed * DisplayManager.getDelta(), 0);
		final float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
		if(getPosition().y < terrainHeight) {
			upwardSpeed = 0;
			getPosition().y = terrainHeight;
			isInAir = false;
		}
	}
	
	private void chkeckInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			currentSpeed = RUN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			currentSpeed = -RUN_SPEED;
		} else {
			currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			currentTurnSpeed = -TURN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			currentTurnSpeed = TURN_SPEED;
		} else {
			currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !isInAir) {
			jump();
			isInAir = true; 
		}
	}
	
	private void jump() {
		upwardSpeed = JUMP_POWER;
	}
	
}
