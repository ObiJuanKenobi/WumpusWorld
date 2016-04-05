package graphics;

import math.Matrix4f;
import math.Vector2i;
import math.Vector3f;

/**
 * Represents the daring explorer of Wumpus World
 * @author Team Bits Please
 *
 */
public class Player {

	private float SIZE = 1.0f;
	private float depth = -1.0f;
	private float moveSpeed = 0.1f;
	
	private Vector2i targetPos;
	
	private Vector3f position;
	private Vector3f scale;
	
	private VertexArray meshN;
	private VertexArray meshS;
	private VertexArray meshE;
	private VertexArray meshW;
	
	private VertexArray curMesh;
	
	private Texture texture = new Texture("res/sprites/character_sprites.png");
	
	private float[] vertices = new float[] { -SIZE / 2.0f, -SIZE / 2.0f, depth, 
			 -SIZE / 2.0f, SIZE / 2.0f,  depth, 
			  SIZE / 2.0f, SIZE / 2.0f,  depth, 
			  SIZE / 2.0f, -SIZE / 2.0f, depth };

	private byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
	
	private char orientation = 'e';

	/**
	 * Creates a new player at given location
	 * @param x Starting X coord of player
	 * @param y Starting Y coord of player
	 */
	public Player(int x, int y) {
		
		initOrientations();
		updateOrientation();

		scale = new Vector3f(0.25f, 0.25f, 1);
		position = new Vector3f(0, 0, 0);
		targetPos = new Vector2i(0, 0);
		setPosition(x, y);
	}
	
	/**
	 * Sets the player's target position to a specified coord
	 * @param x Desired X coord
	 * @param y Desired Y coord
	 */
	public void setPosition(int x, int y) {
		targetPos.x = x;
		targetPos.y = y;
	}
	
	public Vector3f getGLCoords() {
		Vector3f result = new Vector3f();
		result.x = ((float)((position.x / 2.0) - 1) * 0.8f);
		result.y = -((float)((position.y / 2.0) - 1) * 0.8f);
		result.z = 0;
		return result;
	}
	
	/**
	 * Moves the player closer to it's desired position
	 */
	public void update(char orientation) {
		
		if(orientation != this.orientation){
			this.orientation = orientation;
			updateOrientation();
		}
		
		if (position.x < targetPos.x) {
			position.x += moveSpeed;
		}
		
		if (position.x > targetPos.x) {
			position.x -= moveSpeed;
		}
		
		if (position.y < targetPos.y) {
			position.y += moveSpeed;
		}
		
		if (position.y > targetPos.y) {
			position.y -= moveSpeed;
		}
		
		// these two checks accommodate for precision error
		// when the moveSpeed is high enough
		if (Math.abs(targetPos.x - position.x) < moveSpeed) {
			position.x = targetPos.x;
		}
		
		if (Math.abs(targetPos.y - position.y) < moveSpeed) {
			position.y = targetPos.y;
		}
	}
	
	/**
	 * Renders the player into the window
	 */
	public void render() {
		Shader.PLAYER.enable();
		Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix4f.translate(getGLCoords()).multiply(Matrix4f.scale(scale)));
		texture.bind();
		curMesh.render();
		Shader.PLAYER.disable();
	}
	
	private void updateOrientation(){
		switch(this.orientation){
		case 'n':
			curMesh = meshN;
			break;
			
		case 's':
			curMesh = meshS;
			break;
			
		case 'e':
			curMesh = meshE;
			break;
			
		case 'w':
			curMesh = meshW;
			break;
			
		default:
			curMesh = meshE;
			System.out.println("Default player mesh -- should not happen");
		}
	}
	
	
	private void initOrientations(){
		float[] tcs;
		tcs = new float[] { .333f, 1f, .333f, .75f, .666f, .75f, .666f, 1f };
		meshN = new VertexArray(vertices, indices, tcs);
			
		tcs = new float[] { .333f, .25f, .333f, 0f, .666f, 0f, .666f, .25f };
		meshS = new VertexArray(vertices, indices, tcs);
			
		tcs = new float[] { .333f, .75f, .333f, .5f, .666f, .5f, .666f, .75f };
		meshE = new VertexArray(vertices, indices, tcs);
			
		tcs = new float[] { .333f, .5f, .333f, .25f, .666f, .25f, .666f, .5f };
		meshW = new VertexArray(vertices, indices, tcs);
	}
	

}
