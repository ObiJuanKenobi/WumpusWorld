import logic.WumpusWorld;

public class Test_Main {

	public static void main(String[] args) {
		WumpusWorld world = new WumpusWorld();
		for(int y = 0; y < 5; y++){
			for(int x = 0; x < 5; x++){
				System.out.print(world.getTile(x,y).getObjective().toString() + ' ');
			}
			System.out.println(' ');
		}
	}

}
