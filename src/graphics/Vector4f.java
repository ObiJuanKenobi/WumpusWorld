package graphics;

//Just wanted to add something quick to
// store RGBA colors, we can expand this later
public class Vector4f {

	public float x, y, z, w;
	
	
	public Vector4f(){
		this.x = 0.0f;
		this.y = 0.0f;
		this.w = 0.0f;
		this.z = 0.0f;
	}
	
	public Vector4f(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.w = z;
		this.z = w;
	}
}
