package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Utility (static) class to handle converting entire arrays to buffers
 * @author Team Bits Please
 *
 */
public class BufferUtils {

	/**
	 * Inactive constructor
	 */
	private BufferUtils() {}

	/**
	 * Creates a byte buffer from a given byte array
	 * @param array Byte array to be converted
	 * @return Byte array as a single buffer
	 */
	public static ByteBuffer createByteBuffer(byte[] array) {
		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		result.put(array).flip();
		return result;
	}
	
	/**
	 * Creates a float buffer from a given float array
	 * @param array Float array to be converted
	 * @return Float array as a single buffer
	 */
	public static FloatBuffer createFloatBuffer(float[] array) {
		FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.put(array).flip();
		return result;
	}
	
	/**
	 * Creates an int buffer from a given int array
	 * @param array Int array to be converted
	 * @return Int array as a single buffer
	 */
	public static IntBuffer createIntBuffer(int[] array) {
		IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
		result.put(array).flip();
		return result;
	}

}
