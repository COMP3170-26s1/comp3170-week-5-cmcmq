package comp3170.week5;

import static org.lwjgl.opengl.GL41.*;

import org.joml.Matrix4f;
import org.joml.Vector2i;

import comp3170.OpenGLException;
import comp3170.IWindowListener;
import comp3170.ShaderLibrary;
import comp3170.Window;
import comp3170.week5.sceneobjects.Camera;
import comp3170.InputManager;

import java.io.File;
import java.io.IOException;


public class Week5 implements IWindowListener {
	
	public static final boolean wireframeView = false;
	
	private Window window;
	private int width = 800;
	private int height = 800;

	final private File DIRECTORY = new File("src/comp3170/week5/shaders/"); 

	private InputManager input;
	private long oldTime;
	
	private Scene scene;
	private Camera sceneCamera;

	public Week5()  throws OpenGLException {		
		
		window = new Window("Flower field", width, height, this);
		window.setResizable(true);
	    window.run();
	}

	public void init() {
		glClearColor(87.0f/255.0f, 60.0f/255.0f, 23.0f/255.0f, 1.0f); // Dirt brown
		
		input = new InputManager(window); // create an input manager to listen for keypresses and mouse events		
		oldTime = System.currentTimeMillis(); // initialise oldTime
		
		new ShaderLibrary(DIRECTORY);
		scene = new Scene();
		sceneCamera = scene.sceneCam();
		
		if(wireframeView) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}
	
	private Vector2i position = new Vector2i();
		
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;		
		if (input.wasMouseClicked()) {
			// TODO: Get the mouse position into NDC, and then into world space. (TASK 2)
			input.getCursorPos(position); // This will get the mouse position in screen space.

			// TODO: Add a new flower at the mouse position. (TASK 3)
		}
		
		input.clear(); // Run this to clear input before the next frame.
		scene.update(input, deltaTime); // Use update() for scene logic and draw() to...well, draw.
	}

	private Matrix4f viewMatrix  = new Matrix4f();
	private Matrix4f projectionMatrix  = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	public void draw() {
		sceneCamera = scene.sceneCam();
		sceneCamera.GetViewMatrix(viewMatrix);
		sceneCamera.GetProjectionMatrix(projectionMatrix);
		
		//basically doing: mvpMatrix = projectionMatrix * viewMatrix
		//but we need to use the functions and memory techniques we have
		projectionMatrix.mul(viewMatrix, mvpMatrix);
		
		update();
	
		glClear(GL_COLOR_BUFFER_BIT);		
		
		scene.draw(mvpMatrix);
			
	}

	@Override
	public void resize(int width, int height) {
		// record the new width and height
		this.width = width;
		this.height = height;
		glViewport(0,0,width,height);
		sceneCamera.resize(width, height);
	}

	@Override
	public void close() {
		// nothing to do
	}

	public static void main(String[] args) throws IOException, OpenGLException {
		new Week5();
	}

}
