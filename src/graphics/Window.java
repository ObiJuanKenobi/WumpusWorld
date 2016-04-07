package graphics;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryUtil;

/**
 * Class for statically handling windows via GLFW
 * @author Team Bits Please
 *
 */
public class Window {
	
	// GLFW uses a numeric ID to keep track of its windows
	private static long window;
	
	// Permanent reference to callbacks to prevent them
	// 		from getting garbage collected
	private static Keyboard keyboard = new Keyboard();
	private static Mouse mouse = new Mouse();
	private static MousePos mousePos = new MousePos();
	
	// Holds the title, since GLFW doesn't seem to have a convenient way to get it
	private static String windowTitle;
	
	/**
	 * Creates a window using the LWJGL 3 system, GLFW
	 * @param width Width of the window
	 * @param height Height of the window
	 * @param title Title of the window
	 */
	public static void createWindow(int width, int height, String title) {
		windowTitle = title;
		
		// Intialize GLFW
		if(GLFW.glfwInit() != GL11.GL_TRUE) {
			System.out.println("Failed to init GLFW!");
			return;
		}
		
		// Set basic properties of out window
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		
		// Create the actual window, assigning it to the ID
		window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			System.err.println("Failed to init GLFW window!");
			return;
		}
		
		// This tells the window to open in the middle of the screen
		GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 4, (vidmode.height() - height) / 2);
		
		// Set input callbacks
		GLFW.glfwSetKeyCallback(window, keyboard);
		GLFW.glfwSetMouseButtonCallback(window, mouse);
		GLFW.glfwSetCursorPosCallback(window, mousePos);
		
		// Display the window, disable vsync, and create OpenGL capabilities
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(0);
		GLFW.glfwShowWindow(window);
		GL.createCapabilities();
		GL11.glDepthFunc(GL11.GL_NEVER);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
	}
	
	/**
	 * Render the window
	 */
	public static void render() {
		GLFW.glfwPollEvents();	
		GLFW.glfwSwapBuffers(window);
	}
	
	/**
	 * Sets clear color of the window
	 * @param red Red value 0 - 255
	 * @param green Green value 0 - 255
	 * @param blue Blue value 0 - 255
	 */
	public static void setClearColor(int red, int green, int blue) {
		GL11.glClearColor((float) (red / 255.0), (float) (green / 255.0), (float) (blue / 255.0), 1);
	}
	
	/**
	 * Clears the screen
	 */
	public static void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Check if window should be closed
	 * @return True if window should close, false otherwise
	 */
	public static boolean isCloseRequested() {
		 if (GLFW.glfwWindowShouldClose(window) == GL11.GL_TRUE) {
			 return true;
		 } else {
			 return false;
		 }
	}
	
	/**
	 * Destroys window and terminates GLFW
	 */
	public static void dispose() {
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}
	
	/**
	 * Gets the width of our window
	 * @return Width of our window
	 */
	public static int getWidth() {
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(window, widthBuffer, null);
		return widthBuffer.get(0);
	}
	
	/**
	 * Gets the height of our window
	 * @return Height of our window
	 */
	public static int getHeight() {
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(window, null, heightBuffer);
		return heightBuffer.get(0);
	}
	
	/**
	 * Gets the title of our window
	 * @return Title of our window
	 */
	public static String getTitle() {
		return windowTitle;
	}
	
	/**
	 * Gets OpenGL version string
	 * @return String containing OpenGL version and driver provider
	 */
	public static String getOpenGLVersion() {
		return "OpenGL: " + GL11.glGetString(GL11.GL_VERSION);
	}

}
