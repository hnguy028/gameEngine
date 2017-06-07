package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import textures.TextureData;

/**
 * @author Hieu
 * 	Loader class - handles the loading, storing and cleaning up of vaos, vbos and textures
 */
public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	/**
	 * loadtoVAO() - load position vertexes, texture coordinates and indices to a vao
	 * @param positions
	 * @param textureCoords
	 * @param indices
	 * @return
	 */
	public RawModel loadtoVAO(float[] positions,float[] textureCoords, float[] normals, int[] indices) {
		// vao id
		int vao = createVAO();
		
		bindIndicesBuffer(indices);
		
		// add buffers to the vao
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		
		//unbind
		unbindVAO();
		
		// return model for the object
		return new RawModel(vao, indices.length);
	}
	
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoId = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoId, positions.length/dimensions);
	}
	
	
	/**
	 * loadTextures() -  loads image texture into a texture object (to be used to read in sprite/billboards)
	 * @param filename - filename of the png
	 * @return
	 */
	public int loadTexture(String filename) {
		Texture texture = null;
		try {
			// read in texture
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + filename + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// create and store a texture buffer object
		int textureID = texture.getTextureID();
		textures.add(textureID);
		
		return textureID;
		
	}
	
	public int loadCubeMap(String[] textureFiles) {
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texId);
		
		for(int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/" + textureFiles[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		textures.add(texId);
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		return texId;
	}
	
	public TextureData decodeTextureFile(String filename) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		
		try {
			FileInputStream in = new FileInputStream(filename);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Error: unable to load texture " + filename + "");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	
	/**
	 * cleanUp() - delete all vaos, vbos, and textures 
	 */
	public void cleanUp() {
		for(int vao : vaos) { GL30.glDeleteVertexArrays(vao); }
		
		for(int vbo : vbos) { GL15.glDeleteBuffers(vbo); }
		
		for(int texture : textures) { GL11.glDeleteTextures(texture);; }
	}
	
	/**
	 * createVAO() - creates, stores and binds a vertex array object
	 * @return
	 */
	private int createVAO() {
		int vao = GL30.glGenVertexArrays();
		vaos.add(vao);
		GL30.glBindVertexArray(vao);
		
		return vao;
	}
	
	/**
	 * storeDAtaInAttributeList() - 
	 * @param attributeNumber
	 * @param coordinateSize
	 * @param data
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data) {
		// create a vbo
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		
		//bind buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		
		// load data into a float buffer
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		
		// let opengl know that we're done changing the buffer
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
		
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	
	/**
	 * unbindVAO() - 
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * bindIndicesBuffer() - 
	 * @param indices
	 */
	private void bindIndicesBuffer(int[] indices) {
		// generate and store buffer object
		int vbo = GL15.glGenBuffers();
		vbos.add(vbo);
		
		// bind buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,  vbo);
		
		// load indices into a int buffer
		IntBuffer buffer = storeDataInIntBuffer(indices);
		
		// let opengl we're done changing the buffer
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	
	/**
	 * storeDataInIntBuffer() - takes the given data array and loads it into a buffer of same type 
	 * @param data
	 * @return
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		// init a buffer the size of data
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		
		// put data into buffer
		buffer.put(data);
		
		// flip buffer to be read
		buffer.flip();
		
		return buffer;
	}
	
	/**
	 * storeDataInFloatBuffer() - takes the given data array and loads it into a buffer of same type
	 * @param data
	 * @return
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		
		buffer.put(data);
		buffer.flip(); // done writing
		
		return buffer;
	}
}
