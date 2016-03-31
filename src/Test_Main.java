import logic.WumpusWorld;

public class Test_Main {

	public static void main(String[] args) {
		WumpusWorld world = new WumpusWorld();
		System.out.println(world.getPlayerPosition());
		
		world.move(0);
		world.move(0);
		System.out.println(world.getPlayerPosition());
	}

}
