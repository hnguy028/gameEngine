package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

/**
 * @author Karl - "ThinMatrix"
 * @link https://www.youtube.com/user/ThinMatrix/about
 */
public class OBJLoader {
	public static RawModel loadObjModel(String filename, Loader loader) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(new File("res/" + filename + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load .obj file: " + filename);
			e.printStackTrace();
		}

		BufferedReader reader = new BufferedReader(fileReader);

		String line;

		// Lists to hold the data from the obj file
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();

		// arrays to hold floats that will be loader
		float[] verticesArr = null;
		float[] normalsArr = null;
		float[] texturesArr = null;
		int[] indicesArr = null;

		try {
			while (true) {
				line = reader.readLine();
				String[] data = line.split(" ");

				if (line.startsWith("v ")) { // if line is a vertex
					// parse
					Vector3f vertex = new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]),
							Float.parseFloat(data[3]));
					// add to vertices list
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) { // if line is a texture
					// parse
					Vector2f texture = new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2]));
					// add to textures list
					textures.add(texture);
				} else if (line.startsWith("vn ")) { // if line is a normal
					// parse
					Vector3f normal = new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]),
							Float.parseFloat(data[3]));
					// add to normal list
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					texturesArr = new float[vertices.size() * 2];
					normalsArr = new float[vertices.size() * 3];
					break;
				}
			}
			
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				
				String[] currLine = line.split(" ");
				String[] vert1 = currLine[1].split("/");
				String[] vert2 = currLine[2].split("/");
				String[] vert3 = currLine[3].split("/");
				
				processVertex(vert1, indices, textures, normals, texturesArr, normalsArr);
				processVertex(vert2, indices, textures, normals, texturesArr, normalsArr);
				processVertex(vert3, indices, textures, normals, texturesArr, normalsArr);
				
				line = reader.readLine();
			}
			
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		verticesArr = new float[vertices.size() * 3];
		indicesArr = new int[indices.size()];
		
		int vertexPointer = 0;
		
		for(Vector3f vertex : vertices) {
			verticesArr[vertexPointer++] = vertex.x;
			verticesArr[vertexPointer++] = vertex.y;
			verticesArr[vertexPointer++] = vertex.z;
		}
		
		for(int i = 0; i < indices.size(); i++) {
			indicesArr[i] = indices.get(i);
		}
		
		return loader.loadtoVAO(verticesArr, texturesArr, normalsArr, indicesArr);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currVertPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currVertPointer);
		
		Vector2f currTexture;
		
		// obj files may not have the texture added
		if(vertexData[1].isEmpty()) {
			currTexture = new Vector2f(0.5f, 0.5f);
		} else {
			currTexture = textures.get(Integer.parseInt(vertexData[1])-1);
		}
		
		textureArray[currVertPointer * 2] = currTexture.x;
		textureArray[currVertPointer * 2 + 1] = 1 - currTexture.y;
		
		Vector3f currNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currVertPointer * 3] = currNorm.x;
		normalsArray[currVertPointer * 3 + 1] = currNorm.y;
		normalsArray[currVertPointer * 3 + 2] = currNorm.z;
	}
}
