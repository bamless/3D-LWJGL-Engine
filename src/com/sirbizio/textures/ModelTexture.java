package com.sirbizio.textures;

/**
 * Represent a texture in memory
 * @author Fabrizio
 *
 */
public class ModelTexture {
	
    private int TextureID;
    
    private float shineDamper = 1;
    private float reflectivity = 0;
    
    public ModelTexture(int id) {
	this.TextureID = id;
    }

    public int getID() {
        return TextureID;
    }

	public float getShineDamper() {
		return shineDamper;
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
