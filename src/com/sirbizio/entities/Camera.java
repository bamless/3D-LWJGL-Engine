package com.sirbizio.entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f();
	private float pitch;
	private float yaw;
	private float roll;
	
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateZoom();
		calculateAngleAroundPlayer();
		final float hDistance = calculateHDistance();
		final float vDistance = calculateVDistance();
		calculateCamPosition(hDistance, vDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	private void calculateCamPosition(float hDistance, float vDistance) {
		final float theta = player.getRotY() + angleAroundPlayer;
		final float offsetX = (float) (hDistance * Math.sin(Math.toRadians(theta)));
		final float offsetZ = (float) (hDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + vDistance;
	}
	
	private float calculateHDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		final float zoomLevel = Mouse.getDWheel() *0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			final float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(1)) {
			final float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
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
