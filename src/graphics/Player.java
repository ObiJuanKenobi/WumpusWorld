package graphics;

import graphics.Texture;
import graphics.VertexArray;
import math.Matrix4f;
import math.Vector3f;

/**
 * Represents the daring explorer of Wumpus World
 * @author Team Bits Please
 *
 */
public class Player {

	private float SIZE = 1.0f;
	private float depth = -1.0f;
	private float moveSpeed = 0.001f;
	
	private Vector3f position;
	private Vector3f targetPos;
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
		position = new Vector3f(-0.8f, 0.8f, 0);
		targetPos = new Vector3f(-0.8f, -0.8f, 0);
		setPosition(x, y);
	}
	
	/**
	 * Sets the player's target position to a specified coord
	 * @param x Desired X coord
	 * @param y Desired Y coord
	 */
	public void setPosition(int x, int y) {
		targetPos.x = (float) ((x / 2.0) - 1) * 0.8f;
		targetPos.y = -(float) ((y / 2.0) - 1) * 0.8f;
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
	}
	
	/**
	 * Renders the player into the window
	 */
	public void render() {
		Shader.PLAYER.enable();
		Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.scale(scale)));
		texture.bind();
		mesh.render();
		Shader.PLAYER.disable();
	}

}
