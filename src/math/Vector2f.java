package math;

/**
 * Basic vector class to hold 2 floats
 * @author Team Bits Please
 *
 */
public class Vector2f {
	
	private float x, y;
	
	/**
	 * Constructs a vector from given points
	 * @param x X value of vector
	 * @param y Y value of vector
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Calculates length of vector
	 * @return Length of vector
	 */
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	/**
	 * Calculate dot product of two vectors
	 * @param r Other vector to be multiplied against
	 * @return Dot product of the two vectors
	 */
	public float dot(Vector2f r) {
		return x * r.x + y * r.y;
	}
	
	/**
	 * Provides a normalized version of this vector
	 * @return Normalized vector
	 */
	public Vector2f normalize() {
		float length = length();
		
		x /= length;
		y /= length;
		
		return this;
	}
	
	/**
	 * Rotates this vector with respect to a given angle
	 * @param angle Angle amount to be rotated
	 * @return Rotated vector
	 */
	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);
		return new Vector2f((float)(x * cos - y * sin), (float)(x * sin + y * cos));
	}
	
	public Vector2f add(Vector2f r) {
		return new Vector2f(x + r.x, y + r.y);
	}
	
	public Vector2f add(float r) {
		return new Vector2f(x + r, y + r);
	}
	
	public Vector2f sub(Vector2f r) {
		return new Vector2f(x - r.x, y - r.y);
	}
	
	public Vector2f sub(float r) {
		return new Vector2f(x - r, y - r);
	}
	
	public Vector2f mul(Vector2f r) {
		return new Vector2f(x * r.x, y * r.y);
	}
	
	public Vector2f mul(float r) {
		return new Vector2f(x * r, y * r);
	}
	
	public Vector2f div(Vector2f r) {
		return new Vector2f(x / r.x, y / r.y);
	}
	
	public Vector2f div(float r) {
		return new Vector2f(x / r, y / r);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}

}
