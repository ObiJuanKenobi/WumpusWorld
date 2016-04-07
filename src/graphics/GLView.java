package graphics;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import math.Matrix4f;
import math.Vector2f;
import math.Vector3f;
import math.Vector4f;

public class GLView {
	
	public interface ClickListener {
		void OnClick();
	}
	
	protected float mHeight;
	protected float mWidth;
	
	protected boolean isPanel = false;

	protected String mText;
	
	protected float depth;

	// (xpos, ypos) is lower left corner of view
	protected Vector3f mPosition;
	
	protected VertexArray vertexArray;
	
	protected Texture texture;
	
	protected ClickListener listener;

	public GLView(float width, float height) {
		this.mWidth = width;
		this.mHeight = height;

		this.mPosition = new Vector3f(0.0f, 0.0f, depth);
	}

	public void SetText(String text) {
		this.mText = text;
	}

	public void SetTexture(String path){
		texture = new Texture(path);
	}
	
	public void SetWidth(float width) {
		this.mWidth = width;
	}

	public void SetHeight(float height) {
		this.mHeight = height;
	}

	public float GetWidth() {
		return this.mWidth;
	}

	public float GetHeight() {
		return this.mHeight;
	}

	public Vector3f GetPosition() {
		return this.mPosition;
	}

	//Sets views position to this point
	public void Translate(Vector3f translation) {
		this.mPosition = translation;
	}

	//Sets views position to this point
	public void UpdateTranslate(Vector3f translation) {
		this.mPosition = this.mPosition.add(translation);
	}

	//Assumes clickPt is in NDC, i.e. x,y,z are in interval (-1.0f, 1.0f)
	public boolean CheckClicked(Vector2f clickPt) {
		return (clickPt.getX() >= this.mPosition.x && clickPt.x <= this.mPosition.x + this.mWidth)
				&& (clickPt.y >= this.mPosition.y && clickPt.y <= this.mPosition.y + this.mHeight);
	}
	
	public void OnClick(){
		if(listener != null){
			listener.OnClick();
		}
	}
	
	public void SetListener(ClickListener listener){
		this.listener = listener;
	}
	
	public void InitBuffers(){
		float[] vertices = new float[12];

		Vector2f lowerLeft = new Vector2f(this.mPosition.x, this.mPosition.y);
		Vector2f lowerRight = new Vector2f(this.mPosition.x + this.mWidth, this.mPosition.y);
		Vector2f upperLeft = new Vector2f(this.mPosition.x, this.mPosition.y + this.mHeight);
		Vector2f upperRight = new Vector2f(this.mPosition.x + this.mWidth, this.mPosition.y + this.mHeight);		
		
		vertices[0] = (upperLeft.getX());
		vertices[1] = (upperLeft.getY());
		vertices[2] = depth;
		
		vertices[3] = (lowerLeft.getX());
		vertices[4] = (lowerLeft.getY());
		vertices[5] = depth;

		vertices[6] = (lowerRight.getX());
		vertices[7] = (lowerRight.getY());
		vertices[8] = depth;

		vertices[9] = (upperRight.getX());
		vertices[10] = (upperRight.getY());
		vertices[11] = depth;
		
		byte[] indices = {0, 1, 2, 2, 3, 0};
		
		if(!isPanel){
			float[] tcs = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };
			vertexArray = new VertexArray(vertices, indices, tcs);
		
		}
		else {
			//Have fog image overlay entire map, not each tile
			float[] tcs = new float[] { (upperLeft.x + 1f) / 2f, -1f *(upperLeft.y - 1f) / 2f, 
				(upperLeft.x + 1f) / 2f, -1f * (lowerLeft.y - 1f) / 2f, 
				(lowerRight.x + 1f) / 2f, -1f * (lowerRight.y - 1f) / 2f, 
				(upperRight.x + 1f) /2f, -1f * (upperRight.y - 1f) / 2f };
			vertexArray = new VertexArray(vertices, indices, tcs);
		}
		
	}
	
	public void Deallocate(){
		vertexArray.deleteBuffers();
	}

	public void Draw() {
		
		Shader.PLAYER.enable();
		Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix4f.identity());
		texture.bind();
		vertexArray.render();
		texture.unbind();
		Shader.PLAYER.disable();
	}


}
