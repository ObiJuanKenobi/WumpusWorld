import AI.GreedyAI;
import logic.WumpusWorld;

public class Test_Main {

	public static void main(String[] args) {
		WumpusWorld world = new WumpusWorld(WumpusWorld.Difficulty.easy);
		GreedyAI ai = new GreedyAI(world);
		ai.play();
	}

}
