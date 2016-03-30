package graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import math.Matrix4f;
import math.Vector3f;
import utils.ShaderUtils;

/**
 * Wrapper class for interfacing with shaders
 * @author Team Bits Please
 *
 */
public class Shader {

	public static final int VERTEX_ATTRIB = 1;
	public static final int TCOORD_ATTRIB = 2;

	public static Shader PLAYER;

	private boolean enabled = false;

	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();

	/**
	 * Constructs a new shader program from given shader file paths
	 * @param vertex Path to vertex shader
	 * @param fragment Path to fragment shader
	 */
	public Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}

	/**
	 * Initialization method to load all required shaders
	 */
	public static void loadAll() {
		PLAYER = new Shader("res/shaders/player.vert", "res/shaders/player.frag");
		System.out.println("All shaders successfully loaded, compiled, and linked!");
	}

	/**
	 * Gets uniform location of a specified uniform
	 * @param name Name of desired uniform
	 * @return OpenGL location for the uniform
	 */
	public int getUniform(String name) {
		if (locationCache.containsKey(name)) {
			return locationCache.get(name);
		}

		int result = glGetUniformLocation(ID, name);

		if (result == -1) {
			System.err.println("Could not find uniform variable '" + name + "'!");
		} else {
			locationCache.put(name, result);
		}

		return result;
	}

	/**
	 * Set value for a specified 1 integer uniform
	 * @param name Name of uniform
	 * @param value Int value to pass to shader
	 */
	public void setUniform1i(String name, int value) {
		if (!enabled) {
			enable();
		}
		glUniform1i(getUniform(name), value);
	}

	/**
	 * Set value for a specified 1 float uniform
	 * @param name Name of uniform
	 * @param value Float value to pass to shader
	 */
	public void setUniform1f(String name, float value) {
		if (!enabled) {
			enable();
		}
		glUniform1f(getUniform(name), value);
	}

	/**
	 * Set values for a specified 2 float uniform
	 * @param name Name of uniform
	 * @param x First value
	 * @param y Second value
	 */
	public void setUniform2f(String name, float x, float y) {
		if (!enabled) {
			enable();
		}
		glUniform2f(getUniform(name), x, y);
	}

	/**
	 * Set values for a specified 3 float uniform
	 * @param name Name of uniform
	 * @param vector Float vector containing uniform values
	 */
	public void setUniform3f(String name, Vector3f vector) {
		if (!enabled) {
			enable();
		}
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}

	/**
	 * Set values for a specified 4 float uniform
	 * @param name Name of uniform
	 * @param matrix Float matrix containing uniform values
	 */
	public void setUniformMat4f(String name, Matrix4f matrix) {
		if (!enabled) {
			enable();
		}
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}

	/**
	 * Enables this shader
	 */
	public void enable() {
		glUseProgram(ID);
		enabled = true;
	}

	/**
	 * Disables this shader
	 */
	public void disable() {
		glUseProgram(0);
		enabled = false;
	}

}
