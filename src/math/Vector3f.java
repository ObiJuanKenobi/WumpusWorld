package math;

public class Vector3f {

	public float x, y, z;

	public Vector3f() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String toString() {
		return "X: " + x + " Y: " + y + " Z: " + z;
	}

	public Vector3f add(Vector3f translation) {
		return new Vector3f(this.x + translation.x, this.y + translation.y, this.z + translation.z);
	}

}
