package models;

import textures.ModelTexture;

public class TextureModel {
	
	private RawModel rawModel;
	private ModelTexture texture;
	
	public TextureModel(RawModel _rawModel, ModelTexture _texture) {
		this.rawModel = _rawModel;
		this.texture = _texture;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}
	
	
}
