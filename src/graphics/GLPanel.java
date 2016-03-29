package graphics;

import java.util.ArrayList;

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

	public GLPanel(float width, float height, Orientation orientation, 
			float xPadding, float yPadding, float viewSpacing) {
		
		super(width, height);
		
		this.mOrientation = orientation;
		this.mPaddingX = xPadding;
		this.mPaddingY = yPadding;
		this.mSpacing = viewSpacing;
	}
	
	public GLPanel(float width, float height, Orientation orientation, 
			float xPadding, float yPadding, float viewSpacing, int xIndex, int yIndex) {
		
		this(width, height, orientation, xPadding, yPadding, viewSpacing);
		this.xIndex = xIndex;
		this.yIndex = yIndex;
	}

	public void AddView(GLView view) {
		Vector2f lowerLeft = new Vector2f(0f, 0f);

		if (this.mSubviews.size() == 0) {
			Vector2f upperLeft = this.mPosition.add(new Vector2f(this.mPaddingX, this.mHeight - this.mPaddingY));
			lowerLeft = upperLeft.sub(new Vector2f(0.0f, view.GetHeight()));
		}
		else {
			GLView prevView = this.mSubviews.get(this.mSubviews.size() - 1);
			
			if (this.mOrientation == Orientation.vertical) {
				Vector2f spacingUpdate = new Vector2f(0.0f, -this.mSpacing);
				Vector2f upperLeft = prevView.GetPosition().add(spacingUpdate);
				lowerLeft = upperLeft.sub(new Vector2f(0.0f, view.GetHeight()));

			}
			else {
				 float x = prevView.GetPosition().getX() + prevView.GetWidth() + this.mSpacing;
				 float y = this.mPosition.getY() + this.mHeight - this.mPaddingY - view.GetHeight();
				 lowerLeft = new Vector2f(x, y);
			}
		}
		
		//lowerLeft.z = zOffset; //0.0f;
		view.Translate(lowerLeft);
		this.mSubviews.add(view);
	}
	
	public void Translate(Vector2f trans){
		super.Translate(trans);
		for (GLView subview : this.mSubviews) {
			subview.Translate(trans);
		}
	}
	
	public void UpdateTranslate(Vector2f trans){
		super.UpdateTranslate(trans);
		for (GLView subview : this.mSubviews) {
			subview.UpdateTranslate(trans);
		}
	}

	public void OnClick(Vector2f clickPt) {
		//In case there is a general onClick callback for whole panel:
		super.OnClick(clickPt); 

		//For this project I think we just need to know if the panel is clicked, 
		// not any subviews
		
		//Check if subviews were clicked:
//		for (std::vector<GLView*>::iterator it = this->mSubviews.begin(); it != this->mSubviews.end(); it++) {
//			if ((*it)->CheckClicked(clickPt)) {
//				(*it)->OnClick(clickPt);
//			}
//		}
	}

	public void Draw() {
		//Draw all subviews:
		for (GLView subview : this.mSubviews) {
			subview.Draw();
		}

		//Draw the panel:
		super.Draw();
	}
}
