package com.sirbizio.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	public final static float CAMERA_VELOXITY = 2;
	
	private Vector3f position = new Vector3f(0, 10, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			position.z -= CAMERA_VELOXITY;
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			position.x += CAMERA_VELOXITY;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			position.x -= CAMERA_VELOXITY;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			position.z += CAMERA_VELOXITY;
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
			position.y += CAMERA_VELOXITY;
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			position.y -= CAMERA_VELOXITY;
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			yaw -= CAMERA_VELOXITY;
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			yaw += CAMERA_VELOXITY;
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
