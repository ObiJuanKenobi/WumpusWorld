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
	
	public interface GLViewOnClickListener {
		void onClick();
	}
	
	protected float mHeight;
	protected float mWidth;

	protected String mText;
	
	protected float depth;

	// (xpos, ypos) is lower left corner of view
	protected Vector3f mPosition;

	// RGBA color of view
	protected Vector4f mColor;

	protected GLViewOnClickListener mOnClickListener;
	
	protected VertexArray vertexArray;
	
	protected Texture texture;

	public GLView(float width, float height) {
		this.mWidth = width;
		this.mHeight = height;

		this.mPosition = new Vector3f(0.0f, 0.0f, depth);

		this.mColor = new Vector4f();
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

	public void SetColor(Vector4f color) {
		this.mColor = color;
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

	public void SetClickListener(GLViewOnClickListener listener) {
		this.mOnClickListener = listener;
	}

	public void OnClick(Vector2f clickPt) {
		this.mOnClickListener.onClick();
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
		
		float[] tcs = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };
		
		vertexArray = new VertexArray(vertices, indices, tcs);
		
	}
	
	public void Deallocate(){
		vertexArray.deleteBuffers();
	}

	public void Draw() {
		
		Shader.PLAYER.enable();
		Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix4f.identity());
		texture.bind();
		vertexArray.render();
		Shader.PLAYER.disable();
	}


}
