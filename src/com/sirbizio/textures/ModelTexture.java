package com.sirbizio.textures;

/**
 * Represent a texture in memory
 * @author Fabrizio
 *
 */
public class ModelTexture {
    private int TextureID;
    
    public ModelTexture(int id) {
	this.TextureID = id;
    }

    public int getID() {
        return TextureID;
    }
    
    
}
