package utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Utility (static) class to handle loading, compiling, and linking shaders
 * @author Team Bits Please
 *
 */
public class ShaderUtils {
	
	/**
	 * Inactive constructor
	 */
	private ShaderUtils() {}
	
	/**
	 * Loads shader source code from given file paths
	 * @param vertPath Path of vertex shader
	 * @param fragPath Path of fragment shader
	 * @return OpenGL program ID for this shader program
	 */
	public static int load(String vertPath, String fragPath) {
		String vert = FileUtils.loadAsString(vertPath);
		String frag = FileUtils.loadAsString(fragPath);
		return create(vert, frag);
	}
	
	/**
	 * Compiles and links shaders
	 * @param vert Vertex shader source code
	 * @param frag Fragment shader source code
	 * @return OpenGL program ID for this shader program
	 */
	private static int create(String vert, String frag) {
		int program = GL20.glCreateProgram();
		int vertID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(vertID, vert);
		GL20.glShaderSource(fragID, frag);
		
		GL20.glCompileShader(vertID);
		if (GL20.glGetShaderi(vertID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to compile vertex shader!");
			System.err.println(GL20.glGetShaderInfoLog(vertID));
			return -1;
		}
		
		GL20.glCompileShader(fragID);
		if (GL20.glGetShaderi(fragID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to compile fragment shader!");
			System.err.println(GL20.glGetShaderInfoLog(fragID));
			return -1;
		}
		
		GL20.glAttachShader(program, vertID);
		GL20.glAttachShader(program, fragID);
		
		GL20.glLinkProgram(program);
		if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to link shader program!");
			System.err.println(GL20.glGetProgramInfoLog(program));
			return -1;
		}
		
		GL20.glValidateProgram(program);
		if (GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to validate shader program!");
			System.err.println(GL20.glGetProgramInfoLog(program));
			return -1;
		}
		
		GL20.glDeleteShader(vertID);
		GL20.glDeleteShader(fragID);
		
		return program;
	}

}
