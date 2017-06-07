package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

public class TerrainRenderer {
	
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader _shader, Matrix4f projectionMatrix) {
		this.shader = _shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<Terrain> terrains) {
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			// draw elements using triangles
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getNumVertex(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindTextureModel();
		}
	}
	
	public void prepareTerrain(Terrain terrain) {
		// Get raw model from texture model
		RawModel rawModel = terrain.getModel();
		
		// bind model's vao
		GL30.glBindVertexArray(rawModel.getVao());
		
		// enable attributes
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		// get model texture information
		ModelTexture texture = terrain.getTexture();
				
		// load texture information to the shader
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		shader.loadAmbient(texture.getAmbient());
				
		// tell opengl know which texture to render 
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());

	}
	
	private void unbindTextureModel() {
		// disable attributes
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		// unbind vao
		GL30.glBindVertexArray(0);		
	}
	
	private void loadModelMatrix(Terrain terrain) {
		// get the transformations of the object
		Matrix4f tranformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX(), terrain.getY(), terrain.getZ()), 0, 0, 0, 1);
				
		// pass the transformation to the shader
		shader.loadTransformationMatrix(tranformationMatrix);
	}
}
