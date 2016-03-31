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
	private VertexArray mesh;
	private Texture texture;

	/**
	 * Creates a new player at given location
	 * @param x Starting X coord of player
	 * @param y Starting Y coord of player
	 */
	public Player(int x, int y) {
		float[] vertices = new float[] { -SIZE / 2.0f, -SIZE / 2.0f, depth, 
										 -SIZE / 2.0f, SIZE / 2.0f,  depth, 
										  SIZE / 2.0f, SIZE / 2.0f,  depth, 
										  SIZE / 2.0f, -SIZE / 2.0f, depth };

		byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };

		float[] tcs = new float[] { 0, 1, 0, 0, 1, 0, 1, 1 };

		mesh = new VertexArray(vertices, indices, tcs);
		texture = new Texture("res/sprites/brendan.png");
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
		System.out.println(targetPos);
		System.out.println(getGLCoords());
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
	public void update() {
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
		mesh.render();
		Shader.PLAYER.disable();
	}

}
