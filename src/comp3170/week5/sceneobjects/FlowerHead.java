package comp3170.week5.sceneobjects;

import static org.lwjgl.opengl.GL15.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;

import static comp3170.Math.TAU;

public class FlowerHead extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private Shader shader;

	private Vector3f petalColour = new Vector3f(1.0f,1.0f,1.0f);
	private float outerRadius = 0.5f;
	private float innerRadius = 0.3f;

	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;

	public FlowerHead(int nPetals, Vector3f colour) {
		//Compile the shader
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
	
		//Save the colour
		petalColour = colour;
	
		//Raise petal count to at least 3
		nPetals = Math.max(nPetals, 3);
		
		//Generate the vertices
		vertices = new Vector4f[nPetals * 2 + 1]; //Two points per petal, plus a centre
		vertices[0] = new Vector4f(0, 0, 0, 1); //First vertex right in the centre
		
		Vector4f controlVertex = new Vector4f(0, 1f, 0, 1); //Generate a vertex on the unit circle
		Matrix4f rotationStep = new Matrix4f().rotationZ(TAU / (nPetals * 2)); //Create a matrix that applies a rotation
		Matrix4f outerScaling = new Matrix4f().scaling(outerRadius); //Generate two matrices for various scaling
		Matrix4f innerScaling = new Matrix4f().scaling(innerRadius);
		for (int i = 1; i < vertices.length; i++) {
			//Assign each vertex to a copy of the newly rotated control vertex and then scale the copy by some factor
			vertices[i] = new Vector4f(controlVertex.mul(rotationStep)).mul(i%2==0?outerScaling:innerScaling);
		}
		
		//Calculate the indices
	    indices = new int[nPetals * 2 * 3]; //2 tris per petal, 3 indices per tri
	    for (int i = 0; i < indices.length; i++) {
	    	indices[i] = i%3==0?0:i/3+i%3; //See commented version below that is not a dense mess
	    }
	    
	    /* Does the same as the loop above, just clearer
	    int indexPointer = 0;
	    for (int i = 0; i < nPetals * 2; i++) { //Iterate by petal count to simplify maths
	    	indices[indexPointer++] = 0;
	    	indices[indexPointer++] = i + 1;
	    	indices[indexPointer++] = i + 2;
	    }
	    */
	    
	    indices[indices.length - 1] = 1; //Overide the last index to be back to the original vertex
		
	    //Create Buffers
	    vertexBuffer = GLBuffers.createBuffer(vertices);
		indexBuffer = GLBuffers.createIndexBuffer(indices);
	}

	public void update(float dt) {
		// TODO: Make the flower head rotate. (TASK 5)
	}

	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
	    shader.setAttribute("a_position", vertexBuffer);
	    shader.setUniform("u_colour", petalColour);

	    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
	    glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
