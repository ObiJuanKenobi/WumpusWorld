package graphics;

import java.util.ArrayList;

import math.Vector2f;
import math.Vector3f;

//A container that manages the drawing and translation of
// all subviews
public class GLPanel extends GLView {

	// First subview added to the panel will go in upper left corner
	//  all views added after that will stack in the direction 
	//  specified here (like a linearlayout in Android)
	public enum Orientation { vertical, horizontal };
		
	//Add icons to this list, don't draw them directly
	// This object will take care of 
	// drawing all subviews
	private ArrayList<GLView> mSubviews = new ArrayList<GLView>();
	private Orientation mOrientation;
	private float mPaddingX;
	private float mPaddingY;
	private float mSpacing;
	
	//Stores the grid coordinates of the panel
	// As of now, bottom left panel should be 0, 0
	// and top right should be N, N
	public int xIndex;
	public int yIndex;
	
	private boolean isDiscovered = false;
	
	private static Texture undiscovered = new Texture("res/sprites/fog.jpg");
	private static Texture discovered = new Texture("res/sprites/dirt.jpg");
	

	public GLPanel(float width, float height, Orientation orientation, 
			float xPadding, float yPadding, float viewSpacing) {
		
		super(width, height);
		
		this.mOrientation = orientation;
		this.mPaddingX = xPadding;
		this.mPaddingY = yPadding;
		this.mSpacing = viewSpacing;
		
		isPanel = true;
	}
	
	public boolean CheckClicked(Vector2f clickPt){
		for(GLView subview : mSubviews){
			if(subview.CheckClicked(clickPt)){
				subview.OnClick();
				return true;
			}
		}
		return super.CheckClicked(clickPt);
	}
	
	public void OnClick(){
		
	}
	
	public GLPanel(float width, float height, Orientation orientation, 
			float xPadding, float yPadding, float viewSpacing, int xIndex, int yIndex) {
		
		this(width, height, orientation, xPadding, yPadding, viewSpacing);
		this.xIndex = xIndex;
		this.yIndex = yIndex;
		
		this.texture = undiscovered;
	}

	public void AddView(GLView view) {
		Vector3f lowerLeft = new Vector3f(0f, 0f, 0f);

		if (this.mSubviews.size() == 0) {
			Vector3f upperLeft = this.mPosition.add(new Vector3f(this.mPaddingX, this.mHeight - this.mPaddingY, .0f));
			lowerLeft = upperLeft.add(new Vector3f(0.0f, -view.GetHeight(), 0.0f));
		}
		else {
			GLView prevView = this.mSubviews.get(this.mSubviews.size() - 1);
			
			if (this.mOrientation == Orientation.vertical) {
				Vector3f spacingUpdate = new Vector3f(0.0f, -this.mSpacing, 0.0f);
				Vector3f upperLeft = prevView.GetPosition().add(spacingUpdate);
				lowerLeft = upperLeft.add(new Vector3f(0.0f, -view.GetHeight(), .0f));

			}
			else {
				 float x = prevView.GetPosition().x + prevView.GetWidth() + this.mSpacing;
				 float y = this.mPosition.y + this.mHeight - this.mPaddingY - view.GetHeight();
				 lowerLeft = new Vector3f(x, y, .0f);
			}
		}
		
		//lowerLeft.z = zOffset; //0.0f;
		view.Translate(lowerLeft);
		view.InitBuffers();
		this.mSubviews.add(view);
	}
	
	public void Translate(Vector3f trans){
		super.Translate(trans);
		for (GLView subview : this.mSubviews) {
			subview.Translate(trans);
		}
	}
	
	public void UpdateTranslate(Vector3f trans){
		super.UpdateTranslate(trans);
		for (GLView subview : this.mSubviews) {
			subview.UpdateTranslate(trans);
		}
	}
	
	public boolean isDiscovered(){
		return isDiscovered;
	}
	
	public void discover(){
		if(!isDiscovered){
			this.texture = discovered;
			//this.vertexArray.deleteBuffers();
			
			isPanel = false;
			//this.InitBuffers();
			isDiscovered = true;
			
		}
	}

	public void Draw() {
		
		//Draw the panel:
		super.Draw();
		
		//Draw all subviews:
		for (GLView subview : this.mSubviews) {
			subview.Draw();
		}
		
	}
	
	public void Deallocate(){
		for(GLView view : mSubviews){
			view.Deallocate();
		}
		super.Deallocate();
	}
}
