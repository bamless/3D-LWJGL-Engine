package com.sirbizio.models;

/**
 * A representation of a 3D model stored in memory
 * @author Fabrizio
 *
 */
public class RawModel {

    private int vaoID;
    
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount) {
	this.vaoID = vaoID;
	this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
