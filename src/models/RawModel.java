package models;

/**
 * @author Hieu
 *	Raw Model Class - base of a model containing vao, and number of vertixes
 */
public class RawModel {
	private int vao;
	private int numVertex;
	
	/**
	 * Constructor
	 * @param _vao - vertex array object id
	 * @param _numVertex - number of vertices of this object
	 */
	public RawModel(int _vao, int _numVertex) {
		this.vao = _vao;
		this.numVertex = _numVertex;
	}

	/* Getters */
	
	public int getVao() {
		return vao;
	}
	
	public int getNumVertex() {
		return numVertex;
	}

}
