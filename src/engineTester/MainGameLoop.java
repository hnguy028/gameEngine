package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Helicopter;
import entities.Light;
import entities.Player;
import entities.Sprite;
import models.RawModel;
import models.TextureModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

/**
 * @author Hieu
 *	
 */
public class MainGameLoop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// main display window
		DisplayManager.createDisplay("Game Engine");
		
		// class to load in textures, and obj files
		Loader loader = new Loader(); 
		
		// create texture and sprite objects from the png images
		ModelTexture texture = new ModelTexture(loader.loadTexture("crate01"));
		ModelTexture spriteTexture = new ModelTexture(loader.loadTexture("blinkey"));
		
		// array of models
		RawModel modelArr[] = {
				OBJLoader.loadObjModel("helicopter_main", loader), 
				OBJLoader.loadObjModel("main_rotor", loader),
				OBJLoader.loadObjModel("tail_rotor", loader), // not used
				OBJLoader.loadObjModel("sprite", loader)
		};
		
		// texture arrays
		TextureModel textureModel[] = {
				new TextureModel(modelArr[0], texture), // helicopter main
				new TextureModel(modelArr[1], texture), // main rotor
				new TextureModel(modelArr[2], texture), // tail rotor, not used
				new TextureModel(modelArr[3], texture) // sprite
				};
		
		// set material information
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		// entity wrapper for the models
		Entity entity[] = {
				new Entity(textureModel[0], new Vector3f(0,0,0), 0, 0, 0, 1),
				new Entity(textureModel[1], new Vector3f(0,0,0), 0, 0, 0, 1),
				new Entity(textureModel[2], new Vector3f(0,0,0), 0, 0, 90.0f, 2)
		};
		
		// our sun - light origin
		Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1)); 
		
		// load terrain - basic 2d image loaded in the X-Z plane at Y=0
		Terrain terrain = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("dirt")));
		Terrain terrain2 = new Terrain(1, 1, loader, new ModelTexture(loader.loadTexture("dirt")));
		
		// helicopter rotation speeds
		float rotSpeed = 10.0f;
		float maxRotSpeed = 1000.0f;
		
		// Handles all rendering (objects, terrain)
		MasterRenderer renderer = new MasterRenderer(loader);
		
		// Our actual objects to be displayed holding taking their respective models, location, and transformations
		Helicopter heli = new Helicopter(textureModel[0], new Vector3f(200,0,250), 0, 180, 0, 1);
		Sprite sprite = new Sprite(textureModel[3], new Vector3f(200,0,150), 0, 0, 0, 50, heli);
		
		Camera camera = new Camera(heli);
		
		// main game loop - loop until close button is clicked
		while(!Display.isCloseRequested()) {
			
			// pass terrain onto the renderer
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			
			// check for camera movement
			camera.move();
			
			// check for helicopter movement
			heli.move();
			
			// update the sprites facing location
			sprite.updateMove();
			
			// heli position to move rotor relative to it
			// should all be included in helicopter class - but works for now
			float heliX = heli.getPosition().x;
			float heliY = heli.getPosition().y;
			float heliZ = heli.getPosition().z;
			
			// main rotor
			entity[1].setPosition(new Vector3f(heliX, heliY+2.0f, heliZ));
			
			// tail rotor - not implemented, would need to handle rotation
			// entity[2].setPosition(new Vector3f(heliX, heliY, heliZ));
			
			// calculate main rotor speed
			if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				rotSpeed = Math.min(rotSpeed + 1.0f, maxRotSpeed);
			} else {
				rotSpeed = Math.max(Math.abs(rotSpeed - 0.01f), 0.0f);
			}
			
			// update speed
			entity[1].increaseRotation(0, rotSpeed, 0);
			
			// square face
			renderer.processEntity(entity[1]);
			
			// pass light and camera onto the renderer
			renderer.render(light, camera);

			// update sprites and helicopter
			// need for loop for all sprites
			renderer.processEntity(sprite);
			
			renderer.processEntity(heli);

			// update display window
			DisplayManager.updateDisplay();
		}
		
		// clean up our toys
		renderer.cleanUp();
		loader.cleanUp();
		
		// close the window
		DisplayManager.closeDisplay();
	}

}
