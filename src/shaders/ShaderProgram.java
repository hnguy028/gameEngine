package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * Constructor
	 * @param vertexFile
	 * @param fragmentFile
	 */
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		
		programID = GL20.glCreateProgram();
		
		// connect the program to the vertex/fragment shaders
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		// bind attribs
		bindAttributes();
		
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	/**
	 * getAllUniformLocations() - child classes must implement a way to obtain all uniforms of the program
	 */
	protected abstract void getAllUniformLocations();
	
	
	/**
	 * getUniformLocations() - returns the program's uniform location
	 * @param uniformName
	 * @return
	 */
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	/**
	 * start()
	 */
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	/**
	 * stop()
	 */
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	/**
	 * cleanUp() - detaches and deletes all shaders and then the program
	 */
	public void cleanUp() {
		stop();
		
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		
		GL20.glDeleteProgram(vertexShaderID);
		GL20.glDeleteProgram(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * bindAttributes() - subclasses must implement a way to bind all attributes it uses
	 */
	protected abstract void bindAttributes();
	
	/**
	 * bindAttribute() - bind a single attribute to the program
	 * @param attribute
	 * @param variableName
	 */
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * loadFloat()
	 * @param location
	 * @param value
	 */
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * loadVector()
	 * @param location
	 * @param vector
	 */
	protected void loadVector(int location, Vector3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**
	 * loadBoolean()
	 * @param location
	 * @param value
	 */
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if(value) {
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	/**
	 * loadMatrix()
	 * @param location
	 * @param mat
	 */
	protected void loadMatrix(int location, Matrix4f mat) {
		mat.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	/**
	 * loadShader() - COPIED FROM "THINMATRIX"
	 * @param filename
	 * @param type
	 * @return
	 */
	private static int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			
			while(((line = reader.readLine()) != null)) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch(IOException e){
			System.out.println("Error: Could not read " + filename);
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderId = GL20.glCreateShader(type);
		
		GL20.glShaderSource(shaderId, shaderSource);
		GL20.glCompileShader(shaderId);
		
		if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderId, 500));
			System.out.println("Error: Could not compile shader.");
			System.exit(-1);
		}
		
		return shaderId;
	}
}
