package math;

/**
 * Basic vector class to hold 2 ints
 * @author Team Bits Please
 *
 */
public class Vector2i {
	
	public int x, y;
	
	/**
	 * Constructs a vector with default values
	 */
	public Vector2i() {
		x = 0;
		y = 0;
	}
	
	/**
	 * Constructs a vector from given points
	 * @param x X value of vector
	 * @param y Y value of vector
	 */
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
