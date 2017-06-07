package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TextureModel;
import renderEngine.DisplayManager;

public class Player extends Entity {
	
	private static final float WALK_SPEED = 20;
	private static final float RUN_SPEED = 40;
	private static final float TURN_SPEED = 160;
	
	private static final float GRAVITY = 50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardSpeed = 0;
	
	public Player(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
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
		
		upwardSpeed += -GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		if(super.getPosition().y < TERRAIN_HEIGHT) {
			upwardSpeed = 0;
			super.getPosition().y = TERRAIN_HEIGHT;
		}
	}
	
	private void jump() {
		this.upwardSpeed = JUMP_POWER;
	}
	
	/**
	 * checkInputs() - helper method to check for keyboard inputs, in order to move the player
	 */
	private void checkInputs() {
		// check if player is to be moved forwards/backwards
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = WALK_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -WALK_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		
		// check if player is to be turned
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
}
