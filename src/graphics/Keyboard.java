package graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard extends GLFWKeyCallback {

	private static final int NUM_KEYCODES = 512;
	public static boolean[] keys = new boolean[NUM_KEYCODES];
	public static boolean[] keysDown = new boolean[NUM_KEYCODES];

	public void invoke(long window, int key, int scancode, int action, int mods) {
		if(action == GLFW.GLFW_RELEASE) {
			keys[key] = false;
		} else {
			keys[key] = true;
		}
	}
	
	public static boolean getKey(int keyCode) {
		return keys[keyCode];
	}

}
