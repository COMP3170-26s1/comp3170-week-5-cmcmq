package comp3170.week5.sceneobjects;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;

import comp3170.SceneObject;
import comp3170.InputManager;

public class Camera extends SceneObject {

	private float zoom = 5.0f; // You'll need this when setting up your projection matrix...
	private float zoomSpeed = 5.0f;
	private float translateSpeed = 1.0f;
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private int width;
	private int height;
	
	public Camera() {
		
	}
	
	public void resize(int w, int h) {
		width = w;
		height = h;
		float aspect = (float) w / h;
		projectionMatrix.scaling(zoom * aspect, zoom, 1f);
	}
	
	private void recalculate() {
		resize(width, height);
	}
	
	public Matrix4f GetViewMatrix(Matrix4f dest) {
		viewMatrix = getMatrix();
		return viewMatrix.invert(dest);
	}
	
	public Matrix4f GetProjectionMatrix(Matrix4f dest) {
		return projectionMatrix.invert(dest);
	}
	
	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_UP)) {
			zoom -= zoomSpeed * deltaTime;
			zoom = Math.max(0.25f, zoom);
			recalculate();
		}
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			zoom += zoomSpeed * deltaTime;
			zoom = Math.min(20, zoom);
			recalculate();
		}
		if (input.isKeyDown(GLFW_KEY_LEFT)) {
			getMatrix().translate(-translateSpeed * deltaTime * zoom, 0, 0);
		}
		if (input.isKeyDown(GLFW_KEY_RIGHT)) {
			getMatrix().translate(translateSpeed * deltaTime * zoom, 0, 0);
		}
	}
}