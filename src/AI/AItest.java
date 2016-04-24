package AI;

import logic.WumpusWorld;
import logic.WumpusWorld.Difficulty;

public class AItest {

	public static void main(String[] args) {
		WumpusWorld world = new WumpusWorld(Difficulty.easy);
		AI ai = new GlobalDfsAI(world);
		
		if(ai.play())
			System.out.println("AI won!"); 
		else 
			System.out.println("AI lost");

	}

}
