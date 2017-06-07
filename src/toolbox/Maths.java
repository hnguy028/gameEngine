package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f mat = new Matrix4f();
		
		mat.setIdentity();
		
		Matrix4f.translate(translation, mat, mat);
		
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), mat, mat);
		
		
		Matrix4f.scale(new Vector3f(scale,scale,scale), mat, mat);
		
		return mat;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f view = new Matrix4f();
		
		view.setIdentity();
		
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), view, view);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), view, view);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0,0,1), view, view);
		
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		
		Matrix4f.translate(negativeCameraPos, view, view);
		
		return view;
	}
	
	public static Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {
		Matrix4f mat = new Matrix4f();
		
		mat.setIdentity();
		
		Vector3f f = Vector3f.sub(center, eye, null).normalise(null);
		Vector3f u = new Vector3f(0,1,0);//up.normalise(null);
		Vector3f s = Vector3f.cross(f, u, null).normalise(null);
		
		mat.m00 = s.x;
		mat.m10 = s.y;
		mat.m20 = s.z;
		mat.m01 = u.x;
		mat.m11 = u.y;
		mat.m21 = u.z;
		mat.m02 = -f.x;
		mat.m12 = -f.y;
		mat.m22 = -f.z;
		
		return mat;
	}
}
