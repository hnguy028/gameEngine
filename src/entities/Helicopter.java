package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TextureModel;
import renderEngine.DisplayManager;

public class Helicopter extends Entity {
	
	private static final float FLIGHT_SPEED = 40;
	private static final float TURN_SPEED = 160;
	
	private static final float GRAVITY = 20;
	private static final float ASCEND_SPEED = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	private static final float HEIGHT_OFFSET = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardSpeed = 0;
	
	public Helicopter(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	/**
	 * move() - moves the player by checking for keyboard inputs
	 */
	public void move() {
		// check for keyboard inputs
		checkInputs();
		
		// rotate the player by currentSpeed * frame time
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		// get the distance that the player traveled in that time
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		
		super.increasePosition(dx, 0, dz);
		
		upwardSpeed += Math.min(-GRAVITY * DisplayManager.getFrameTimeSeconds(),10.0f);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) { upwardSpeed = 0;}
		
		super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		if(super.getPosition().y < TERRAIN_HEIGHT + HEIGHT_OFFSET) {
			upwardSpeed = 0;
			super.getPosition().y = TERRAIN_HEIGHT + HEIGHT_OFFSET;
		}
	}
	
	public boolean isInMotion() { return Math.abs(currentSpeed) > 0 || Math.abs(upwardSpeed) > 0; }
	
	public boolean isMotionUp() { return upwardSpeed > 0; }
	public float getVerticalSpeed() { return upwardSpeed; }
	
	public boolean isMotionForward() { return currentSpeed > 0; }
	public float getForwardSpeed() { return currentSpeed; }
	
	private void jump() {
		this.upwardSpeed = ASCEND_SPEED;
	}
	
	/**
	 * checkInputs() - helper method to check for keyboard inputs, in order to move the player
	 */
	private void checkInputs() {
		// check if player is to be moved forwards/backwards
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = FLIGHT_SPEED;
			
			//if(super.getPosition().y <= TERRAIN_HEIGHT + HEIGHT_OFFSET) {this.currentSpeed = 0;}
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -FLIGHT_SPEED;
			
			//if(super.getPosition().y <= TERRAIN_HEIGHT + HEIGHT_OFFSET) {this.currentSpeed = 0;}
		} else {
			this.currentSpeed = 0;
		}
		
		// check if player is to be turned
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
			
			//if(super.getPosition().y <= TERRAIN_HEIGHT + HEIGHT_OFFSET) {this.currentTurnSpeed = 0;}
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
			
			//if(super.getPosition().y <= TERRAIN_HEIGHT + HEIGHT_OFFSET) {this.currentTurnSpeed = 0;}
		} else {
			this.currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
}
