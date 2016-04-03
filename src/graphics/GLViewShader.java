package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

public class GLViewShader {
	
	public int programId;
	//public int objectColorLoc;
	
	static String vertexShaderSource = "#version 330 core\n" +		
		"layout (location = 1) in vec3 position;\n" +
		"layout (location = 2) in vec2 texCoords;\n" +
		"out vec2 texOut;\n" +
		"void main()\n" +
		"{\n" +
		"texOut = texCoords;\n" +
		"gl_Position = vec4(position.x, position.y, 0.1f, 1.0f);\n" +
		"}\0";

	static String fragmentShaderSource = "#version 330 core\n" +
		"out vec4 color;\n" +
		"in vec2 texOut;\n" +
		"uniform sampler2D tex;\n" + 
		"void main()\n" +
		"{\n" +
		"color = texture(tex, texOut);\n" +
		"}\n\0";
	
	//vec4(texOut.x, texOut.y, 0.0, 0.0);
	
	public GLViewShader() {
		int[] shaders = new int[2];
		shaders[0] = createShader(GL20.GL_VERTEX_SHADER, vertexShaderSource);
		shaders[1] = createShader(GL20.GL_FRAGMENT_SHADER, fragmentShaderSource);
		programId = createShaderProgram(shaders);
		//objectColorLoc = GL20.glGetUniformLocation(programId, "objectColor");
	}
	
	static int createShader(int shadertype, String shaderString){
        int shader = GL20.glCreateShader(shadertype);
        GL20.glShaderSource(shader, shaderString);
        GL20.glCompileShader(shader);
        int status = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (status == GL11.GL_FALSE){
            
            String error=GL20.glGetShaderInfoLog(shader);
            
            String ShaderTypeString = null;
            switch(shadertype){
            case GL20.GL_VERTEX_SHADER: ShaderTypeString = "vertex"; break;
            case GL32.GL_GEOMETRY_SHADER: ShaderTypeString = "geometry"; break;
            case GL20.GL_FRAGMENT_SHADER: ShaderTypeString = "fragment"; break;
            }
            
            System.err.println( "Compile failure in " + ShaderTypeString + " shader:\n" + error);
        }
        return shader;
    }

	public static int createShaderProgram(int[] shaders){
        int program = GL20.glCreateProgram();
        for (int i = 0; i < shaders.length; i++) {
            GL20.glAttachShader(program, shaders[i]);
        }
        GL20.glLinkProgram(program);
        
        int status = GL20.glGetShaderi(program, GL20.GL_LINK_STATUS);
        if (status == GL11.GL_FALSE){
            String error = GL20.glGetProgramInfoLog(program);
            System.err.println( "Linker failure: "+ error);
        }
        for (int i = 0; i < shaders.length; i++) {
            GL20.glDetachShader(program, shaders[i]);
        }
        return program;
    }
	
	public void Use(){
		GL20.glUseProgram(this.programId);
	}
	
	public void disable(){
		GL20.glUseProgram(0);
	}
}
