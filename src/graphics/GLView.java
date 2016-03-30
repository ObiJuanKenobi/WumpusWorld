package graphics;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import math.Vector2f;
import math.Vector4f;

public class GLView {
	
	public interface GLViewOnClickListener {
		void onClick();
	}
	
	protected float mHeight;
	protected float mWidth;

	protected String mText;

	// (xpos, ypos) is lower left corner of view
	//   May want a vec3 for z coords/layering
	protected Vector2f mPosition;

	// RGBA color of view
	protected Vector4f mColor;

	protected GLViewOnClickListener mOnClickListener;

	//TODO margin & padding, styling

	static GLViewShader mShader;
	
	//GLint mObjectColorLoc = glGetUniformLocation(mShader.Program, "objectColor");

	public GLView(float width, float height) {
		this.mWidth = width;
		this.mHeight = height;

		this.mPosition = new Vector2f(0.0f, 0.0f);

		this.mColor = new Vector4f();
		
		//May not be best way of going about it,
		// but I'm trying to have just one shader for all of these views:
		if(mShader == null){
			mShader = new GLViewShader();
		}
	}

	public void SetText(String text) {
		this.mText = text;
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

	public Vector2f GetPosition() {
		return this.mPosition;
	}

	//Sets views position to this point
	public void Translate(Vector2f translation) {
		this.mPosition = translation;
	}

	//Sets views position to this point
	public void UpdateTranslate(Vector2f translation) {
		this.mPosition = this.mPosition.add(translation);
	}

	//Assumes clickPt is in NDC, i.e. x,y,z are in interval (-1.0f, 1.0f)
	public boolean CheckClicked(Vector2f clickPt) {
		return (clickPt.getX() >= this.mPosition.getX() && clickPt.getX() <= this.mPosition.getX() + this.mWidth)
				&& (clickPt.getY() >= this.mPosition.getY() && clickPt.getY() <= this.mPosition.getY() + this.mHeight);
	}

	public void SetClickListener(GLViewOnClickListener listener) {
		this.mOnClickListener = listener;
	}

	public void OnClick(Vector2f clickPt) {
		this.mOnClickListener.onClick();
	}

	
	//Probably a better way of doing this, since most views will have fixed dimensions/positions
	public void Draw() {

		/* TODO
		 * this section does not need to be repeated for most views we will use
		 * probably want to move it to an init function or something...
		 */
		float[] vertices = new float[12];

		Vector2f lowerLeft = new Vector2f(this.mPosition.getX(), this.mPosition.getY());
		Vector2f lowerRight = new Vector2f(this.mPosition.getX() + this.mWidth, this.mPosition.getY());
		Vector2f upperLeft = new Vector2f(this.mPosition.getX(), this.mPosition.getY() + this.mHeight);
		Vector2f upperRight = new Vector2f(this.mPosition.getX() + this.mWidth, this.mPosition.getY() + this.mHeight);

		vertices[0] = (upperLeft.getX());
		vertices[1] = (upperLeft.getY());

		vertices[2] = (lowerLeft.getX());
		vertices[3] = (lowerLeft.getY());

		vertices[4] = (upperRight.getX());
		vertices[5] = (upperRight.getY());

		vertices[6] = (upperRight.getX());
		vertices[7] = (upperRight.getY());

		vertices[8] = (lowerLeft.getX());
		vertices[9] = (lowerLeft.getY());

		vertices[10] = (lowerRight.getX());
		vertices[11] = (lowerRight.getY());		

		FloatBuffer DataBuffer = BufferUtils.createFloatBuffer(vertices.length);//position at 0.
        DataBuffer.put(vertices);
        //put all the data in the buffer, position at the end of the data
        DataBuffer.flip();
        //set the limit at the position=end of the data(ie no effect right now),and sets the position at 0 again 
        
        int buffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, DataBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
        GL20.glBindAttribLocation(mShader.programId, 0, "position");
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
        /* End of init section */
        
        
        //Actual drawing code (need to add binding the correct vertext array):
        mShader.Use();
        
        GL20.glUniform4f(mShader.objectColorLoc, this.mColor.x, this.mColor.y,
        		this.mColor.z, this.mColor.w);
        
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

        GL20.glDisableVertexAttribArray(0);
        GL20.glUseProgram(0);
        
        //TODO need a deconstructor type method for deallocating all the VAOs
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        mShader.disable();
	}


}
