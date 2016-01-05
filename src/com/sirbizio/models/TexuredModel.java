package com.sirbizio.models;

import com.sirbizio.textures.ModelTexture;

public class TexuredModel {
    private RawModel rawModel;
    
    private ModelTexture texture;
    
    public TexuredModel(RawModel model, ModelTexture texture) {
	this.rawModel = model;
	this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }
}
