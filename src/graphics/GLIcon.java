package graphics;

import logic.Objectives;
import logic.Percepts;

public class GLIcon extends GLView {
	
	static Texture breeze = new Texture("res/sprites/new_wind.jpg");
	static Texture stench = new Texture("res/sprites/nose.jpg");
	static Texture glitter = new Texture("res/sprites/glitter.jpg");
	static Texture wumpus = new Texture("res/sprites/wumpus.jpg");
	static Texture gold = new Texture("res/sprites/gold.png");
	//static Texture ladder = new Texture("res/sprites/ladder.png");
	static Texture pit = new Texture("res/sprites/new_hole.jpg");

	public GLIcon(float width, float height, Percepts percept) {
		super(width, height);
		this.texture = textureForPercept(percept);
	}
	
	public GLIcon(float width, float height, Objectives objective) {
		super(width, height);
		this.texture = textureForObjective(objective);
	}
	
	public void Draw(){
		super.Draw();
	}
	
	
	private Texture textureForPercept(Percepts percept){
		switch(percept){
		case Breeze:
			return breeze;
		case Stench:
			return stench;
		case Glitter:
			return glitter;
		default:
			return null;
		}
	}
	
	private Texture textureForObjective(Objectives objective){
		switch(objective){
		case Wumpus:
			return wumpus;
		case Gold:
			return gold;
		case Pit:
			return pit;
		//case Ladder:
			//return ladder;
		default:
			return null;
		}
	}

	
}
