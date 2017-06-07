package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TextureModel;

/**
 * @author Hieu
 *	Entity class
 */
public class Entity {
	private TextureModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;

	private Vector3f up = new Vector3f(0,0,0);
	private Vector3f eye;
	private Vector3f center = new Vector3f(0,0,0);
	
	
	/**
	 * Constructor
	 * @param model
	 * @param position
	 * @param rotX
	 * @param rotY
	 * @param rotZ
	 * @param scale
	 */
	public Entity(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
		
		this.eye = position;
	}
	
	/*public void lookAt(Vector3f vec, Vector3f up) {
		this.up = up;
		
		// dot product
		float angle = Vector3f.angle(vec, up);
		
		// cross between where we want to look, and where we are currently looking
		Vector3f rotationAxis = Vector3f.cross(vec, center, null);
	}*/
	
	/**
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void increasePosition(float dx, float dy, float dz) {
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
	}

	/**
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX+=dx;
		this.rotY+=dy;
		this.rotZ+=dz;
	}
	
	
	
	/* Getter and Setter Methods */
	
	public TextureModel getModel() {
		return model;
	}

	public void setModel(TextureModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
}
