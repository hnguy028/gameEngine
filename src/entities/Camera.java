package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Hieu
 *	Camera class handles the camera movement and holds camera information
 */
public class Camera {

	private float distanceFromPlayer = 20;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	//private float movementSpeed = 0.1f;
	//private float yawSpeed = 1.0f;
	
	private Helicopter player;
	
	/**
	 * Constructor
	 */
	public Camera(Helicopter _player) {
		this.player = _player;
	}

	/** 
	 * move() : handles keyboard input in order to move the camera
	 */
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}
	
	/* Getter Methods */
	
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
	
	private void calculateCameraPosition(float hDist, float vDist) {
		float theta = player.getRotY() + angleAroundPlayer;
		
		float offsetX = (float)(hDist * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float)(hDist * Math.cos(Math.toRadians(theta)));
		
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + vDist;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			//float pitchChange = Mouse.getDY() * 0.1f;
			//pitch -= pitchChange;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
}
