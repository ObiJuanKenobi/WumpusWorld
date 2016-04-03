package graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL13;

import utils.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

/**
 * Wrapper class for handling OpenGL textures
 * @author Team Bits Please
 *
 */
public class Texture {
	
	private int width, height;
	private int texture;
	
	/**
	 * Constructs a new texture from a given file path
	 * @param path Path to desired texture
	 */
	public Texture(String path) {
		texture = load(path);
	}
	
	/**
	 * Loads this texture into OpenGL
	 * @param path Path to desired texture
	 * @return OpenGL ID for this texture
	 */
	private int load(String path) {
		int[] pixels = null;
		
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] data = new int[width * height];
		
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		glBindTexture(GL_TEXTURE_2D, 0);
		return result;
	}
	
	/**
	 * Binds this texture
	 */
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	/**
	 * Unbinds this texture
	 */
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

}
