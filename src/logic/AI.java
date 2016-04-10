package logic;

import java.util.ArrayList;

/**
 * Created by paulgerlich on 4/9/16.
 */
public class AI {
    static final int DOWN = 0, RIGHT = 1, UP = 2, LEFT = 3;

    WumpusWorld wumpusWorld;
    boolean haveFoundGold = false;
    ArrayList<ArrayList<Tile>> map = new ArrayList<>();
    private int x = 0;
    private int y = 0;

    public AI(WumpusWorld world, WumpusWorld.Difficulty difficulty){
        if ( difficulty == null ) {
            difficulty = WumpusWorld.Difficulty.hard;
        }

        if ( world == null ) {
            world = new WumpusWorld(difficulty);
        }

        wumpusWorld = world;
    }

    public void play(){
        boolean playing = true;

        while ( playing ) {
            // Analyze current tile, add to memory
            // Choose & execute next move
        }
    }

    /**
     * Add the
     */
    private void consumeCurrentTile(){
        Tile tile = new Tile(x, y);
        tile.setPercepts(new ArrayList<>(wumpusWorld.getPerceptions()));

        map.get(y).add(x, wumpusWorld.getTile(x,y));
    }

    private void assessMapInformation(){
        //Proccess the new perceptions we recieved in map[y][x]
        // - Can we deduct something exists above me?
        // - Can we deduct something exists to the right of me?
        // - Below me?
        // - Left of me?
    }

    /**
     * Determine which move to take
     * @return Next move direction
     */
    private int getNextMove(){
        Tile currentTile = getCurrentTile();
        ArrayList<Integer> availableMoveOptions = getAvailableMoveOptions();




        return DOWN;
    }


    /**
     * The current tile the user is on
     * @return Current player position tile
     */
    private Tile getCurrentTile(){
        return map.get(y).get(x);
    }

    /**
     * A current list of valid moves from our position
     * @return List of valid moves
     */
    private ArrayList<Integer> getAvailableMoveOptions(){
        ArrayList<Integer> options = new ArrayList<>();

        if ( wumpusWorld.isValidMove(UP) ) {
            options.add(UP);
        }

        if ( wumpusWorld.isValidMove(RIGHT)) {
            options.add(RIGHT);
        }

        if ( wumpusWorld.isValidMove(DOWN)) {
            options.add(DOWN);
        }

        if ( wumpusWorld.isValidMove(LEFT)) {
            options.add(LEFT);
        }

        return options;
    }
}
