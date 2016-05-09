package com.sirbizio.textures;

/**
 * Represent a texture in memory
 * @author Fabrizio
 *
 */
public class ModelTexture {
	
	/**The ID of the texture*/
    private int TextureID;
    
    /**The shine damper texture's attribute*/
    private float shineDamper = 1;
    /**The reflectivity of the texture*/
    private float reflectivity = 0;
    
    /**Whether the texture has transparent parts*/
    private boolean hasTransparency = false;
    /**Whether the texture should use a fake light ignoring the sun*/
    private boolean useFakeLighting = false;
    
    public ModelTexture(int id) {
    	this.TextureID = id;
    }
    
    public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
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
