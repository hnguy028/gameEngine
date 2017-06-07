package textures;

public class ModelTexture {
	
	private int textureId;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	private float ambient = 0.1f;
	
	public ModelTexture(int _textureId) {
		this.textureId = _textureId;
	}
	
	public int getId() {
		return this.textureId;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public float getAmbient() {
		return ambient;
	}

	public void setAmbient(float ambient) {
		this.ambient = ambient;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
}
