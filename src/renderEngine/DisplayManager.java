package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * @author Hieu
 *
 */
public class DisplayManager {

	private static final int WIDTH = 480;//1280;
	private static final int HEIGHT = 480;//620;
	private static final int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	
	/**
	 * createDisplay() - initializes the window for display 
	 * @param _title - title of window
	 */
	public static void createDisplay(String _title) {
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		try {
			// Create window at specified size
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			
			// specify to the system how we are using the display ie using pixels 1:1 ratio
			Display.create(new PixelFormat(), attribs);

			// set title of window
			Display.setTitle(_title);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		// set viewport (the affine transformation from view to the display window)
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	
	/**
	 * updateDisplay() - update the display window
	 */
	public static void updateDisplay() {
		// limit the fps
		Display.sync(FPS_CAP);
		
		Display.update();
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	/**
	 * closeDispay() - destroy window 
	 */
	public static void closeDisplay() {
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution(); 
	}
	
}
