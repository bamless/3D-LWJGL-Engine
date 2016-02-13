package com.sirbizio.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.sirbizio.renderEngine.DisplayManager;

public class Camera {

	public final static float CAMERA_VELOXITY = 2;
	
	private Vector3f position = new Vector3f(0, 10, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
			position.z -= CAMERA_VELOXITY * DisplayManager.getDelta() * 60;
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			position.x += CAMERA_VELOXITY * DisplayManager.getDelta() * 60;
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			position.x -= CAMERA_VELOXITY * DisplayManager.getDelta() * 60;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			position.z += CAMERA_VELOXITY * DisplayManager.getDelta() * 60;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
}
