package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by paulgerlich on 4/9/16.
 */
public class AI {
    private static final int DOWN = 0, RIGHT = 1, UP = 2, LEFT = 3;

    private WumpusWorld wumpusWorld;
    private boolean haveFoundGold = false;
    private Tile ladder;
    private HashMap<String, Tile> knownLocations = new HashMap<>();
    private int currentX = 0;
    private int currentY = 0;
    private ArrayList<Integer> pathToLadder;

    public AI(WumpusWorld world, WumpusWorld.Difficulty difficulty){
        if ( difficulty == null ) {
            difficulty = WumpusWorld.Difficulty.easy;
        }

        if ( world == null ) {
            world = new WumpusWorld(difficulty);
        }

        wumpusWorld = world;
    }

    public void play(){

        while ( !wumpusWorld.haveWon() && !wumpusWorld.isGameOver() ) {
            consumeCurrentTile();
            wumpusWorld.move(getNextMove());
        }
    }

    /**
     * Add the tiles perceptions to our perceptions.
     */
    private void consumeCurrentTile(){
        Tile tile = new Tile(currentX, currentY);
        tile.setPercepts(new ArrayList<>(wumpusWorld.getPerceptions()));

        switch ( tile.getObjective() ) {
            case Gold:
                haveFoundGold = true;
                break;
            case Ladder:
                ladder = tile;
                break;
            default:
                break;
        }

        knownLocations.put(String.valueOf(currentX) + '+' + String.valueOf(currentY), tile);
    }

    /**
     * Niavely assess a tile for its probability of containing a wumpus or pit
     * @param x
     * @param y
     * @return
     */
    private DangerProbabilities getTileDangerProbability(int x, int y){
        DangerProbabilities dp = new DangerProbabilities();


        String[] keys = {String.valueOf(x) + '+' + String.valueOf(y-1), String.valueOf(x) + '+' + String.valueOf(y+1),
                String.valueOf(x+1) + '+' + String.valueOf(y), String.valueOf(x-1) + '+' + String.valueOf(y)};

        for(int i = 0; i < keys.length; i++ ) {
            //Check above
            if ( knownLocations.containsKey(keys[i]) ) {
                ArrayList<Percepts> curPercepts = new ArrayList<>(Arrays.asList(knownLocations.get(keys[i]).getPercepts()));

                if ( curPercepts.size() == 0 ) {
                    dp.pitProbability = 0;
                    dp.wumpusProbability = 0;
                    return dp;
                }

                if ( curPercepts.contains(Percepts.Breeze) ) {
                    dp.pitProbability += 0.25;
                }

                if ( curPercepts.contains(Percepts.Stench) ) {
                    dp.wumpusProbability += 0.25;
                }
            }
        }

        return dp;
    }

    /**
     * Determine which move to take (Greedy, local)
     * @return Next move direction
     */
    private int getNextMove(){
        int move = DOWN;

        ArrayList<Integer> availableMoveOptions = getAvailableMoveOptions();
        ArrayList<DangerProbabilities> dp = new ArrayList<>();

        // Get danger probabilities for the available moves
        if ( availableMoveOptions.contains(UP) ) {
            dp.add(getTileDangerProbability(currentX, currentY - 1));
        }

        if ( availableMoveOptions.contains(DOWN) ) {
            dp.add(getTileDangerProbability(currentX, currentY + 1));
        }

        if ( availableMoveOptions.contains(RIGHT) ) {
            dp.add(getTileDangerProbability(currentX + 1, currentY));
        }

        if ( availableMoveOptions.contains(LEFT) ) {
            dp.add(getTileDangerProbability(currentX - 1, currentY));
        }

        //TODO: Sort by probability but maintain a pairing between the direction and probability

        if ( haveFoundGold && ladder != null ) {
            if ( pathToLadder == null ) {
                pathToLadder = getPathToLadder();
            } else {
                move = pathToLadder.get(0);
                pathToLadder.remove(0);
            }
        } else {
           for(int i = 0; i < availableMoveOptions.size(); i++ ) {
                //If we haven't seen it, choose this option
           }
        }

        return move;
    }

    private ArrayList<Integer> getPathToLadder(){
        return null;
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

    class DangerProbabilities {
        double pitProbability = 0.0;
        double wumpusProbability = 0.0;
    }

}
