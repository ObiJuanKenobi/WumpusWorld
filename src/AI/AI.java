package AI;

import logic.WumpusWorld;

public abstract class AI {
	
	public enum AiDifficulty {none, easy, medium, hard};
	
	protected static final int DOWN = 0, RIGHT = 1, UP = 2, LEFT = 3;
	
	protected WumpusWorld wumpusWorld;
	
	protected int currentX = 0;
	protected int currentY = 0;

	protected boolean haveFoundGold = false;
	
	
	public AI(WumpusWorld world){
		wumpusWorld = world;
	}
	
	//Return true for win, false for loss
	public abstract boolean play();
	
	// Returns true if won on this move
	// Used so AI can take turns with players
	public abstract boolean makeMove();

}
