package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import models.TextureModel;

/**
 * @author Hieu
 *
 */
public class Sprite extends Entity {

	private Vector3f eye = new Vector3f(0, 0, 0);
	float offsetX;
	float offsetY;
	float offsetZ;
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch = 0;
	private float yaw = 90;
	private float roll = 0;

	private Helicopter heli;
	
	private Vector3f normal;
	private Vector3f center;
	
	private Vector3f toFace;

	/**
	 * @param model
	 * @param position
	 * @param rotX
	 * @param rotY
	 * @param rotZ
	 * @param scale
	 * @param _player
	 */
	public Sprite(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,
			Helicopter heli) {
		// super(model, position, 0, 0, 0, scale); // since we'll be calculating
		// this anyways
		super(model, position, rotX, rotY, rotZ, scale);
		
		// eye
		this.eye = position;
		this.position = position;

		offsetX = rotX;
		offsetY = rotY;
		offsetZ = rotZ;
		
		// center
		this.normal = Vector3f.sub(heli.getPosition(), position, null); // the sprite should initially look at the helicopter
		this.center = Vector3f.sub(heli.getPosition(), position, null);
		
		this.heli = heli;
	}

	public void updateMove() {
		
		lookAt(heli.getPosition());
		
		pitch = (float) Math.toDegrees(Vector3f.angle(new Vector3f(0, center.y, center.z), new Vector3f(0, toFace.y, toFace.z)));
		yaw = (float) Math.toDegrees(Vector3f.angle(new Vector3f(center.x, 0, center.z), new Vector3f(toFace.x, 0, toFace.z)));
		//roll = (float) Math.toDegrees(Vector3f.angle(new Vector3f(center.x, center.y, 0), new Vector3f(toFace.x, toFace.y, 0)));
		
		super.setRotX(-pitch);
		//super.setRotY(yaw);
		//super.setRotZ(roll);
		
		//super.increaseRotation(pitch, 0, 0);
		System.out.println(pitch + " - " + yaw);
		//System.out.println(center.toString() + " -- " + eye.toString());
		
		eye = new Vector3f(toFace);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
			super.setRotX(0);
			super.setRotY(90);
			super.setRotZ(0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_O)) {
			super.setRotX(180);
			super.setRotY(180);
			super.setRotZ(0);
		}
	}

	private void lookAt(Vector3f vector) {
		// get the vector to look at the helicopter
		toFace = new Vector3f(vector.x - position.x, vector.y - position.y,
				vector.z - position.z);
	}

	private void calculateAngleToHelicopter() {
		float deltaY = toFace.y - normal.y;
		int sign = deltaY > 0 ? -1 : 1;
		
		pitch = Vector3f.angle(center, center);
				/*dotProduct(
				new Vector3f(0, normal.y, normal.z),
				new Vector3f(0, toFace.y, toFace.z)
				) * sign;*/
	}
	
	
	private float dotProduct(Vector3f v1, Vector3f v2) {
		return (float) Math.acos(((v2.x * v1.x) + (v2.y + v1.y) + (v2.z + v1.z)) / (magnitude(v2) * magnitude(v1))); 
	}
	
	private float magnitude(Vector3f v) {
		return (float) Math.sqrt(Math.pow(v.x, 2)
				+ Math.pow(v.y, 2) + Math.pow(v.z, 2));
	}
}
