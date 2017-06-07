package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class EntityRenderer {
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader _shader, Matrix4f projectionMatrix) {
		this.shader = _shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TextureModel, List<Entity>> entities) {
		for(TextureModel model : entities.keySet()) {
			prepareTextureModel(model);
			List<Entity> batch = entities.get(model);
			
			for(Entity entity : batch) {
				prepareInstance(entity);
				// draw elements using triangles
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getNumVertex(), GL11.GL_UNSIGNED_INT, 0);
			}
		}
		
		unbindTextureModel();
	}
	
	public void prepareTextureModel(TextureModel model) {
		// Get raw model from texture model
		RawModel rawModel = model.getRawModel();
		
		// bind model's vao
		GL30.glBindVertexArray(rawModel.getVao());
		
		// enable attributes
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		// get model texture information
		ModelTexture texture = model.getTexture();
				
		// load texture information to the shader
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		shader.loadAmbient(texture.getAmbient());
				
		// tell opengl know which texture to render 
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
		
	}
	
	private void unbindTextureModel() {
		// disable attributes
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		// unbind vao
		GL30.glBindVertexArray(0);		
	}
	
	private void prepareInstance(Entity entity) {
		 
		// get the transformations of the object
		Matrix4f tranformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		
		// pass the transformation to the shader
		shader.loadTransformationMatrix(tranformationMatrix);
	}
}
