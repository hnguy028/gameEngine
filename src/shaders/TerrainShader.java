package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "src/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_SHADER = "src/shaders/terrainFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	private int location_lightPosition;
	private int location_lightColor;
	
	private int location_shineDamper;
	private int location_reflectivity;
	
	private int location_ambient;
	
	public TerrainShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		
		location_ambient = super.getUniformLocation("ambient");
		/* TODO : add other material rendering */
	}
	
	public void loadAmbient(float ambient) {
		super.loadFloat(location_ambient, ambient);
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	public void loadTransformationMatrix(Matrix4f mat) {
		super.loadMatrix(location_transformationMatrix, mat);
	}
	
	public void loadLight(Light light) {
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColor, light.getColor());
		/* TODO : add other material rendering */
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f mat = Maths.createViewMatrix(camera);
		
		super.loadMatrix(location_viewMatrix, mat);
	}
	
	public void loadProjectionMatrix(Matrix4f mat) {
		super.loadMatrix(location_projectionMatrix, mat);
	}
}

