package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import logic.Percepts;
import logic.Tile;
import logic.WumpusWorld;

/**
 * Created by paulgerlich on 4/9/16.
 */
public class GreedyAI extends AI {

    private Tile ladder;
    private ArrayList<Integer> pathToLadder;
    private HashMap<String, Tile> knownLocations = new HashMap<>();

    private int lastMoveAttempt = -1;
    
    private boolean debug = true;

    public GreedyAI(WumpusWorld world){
        super(world);
    }

    public boolean play(){

        while ( !wumpusWorld.haveWon() && !wumpusWorld.isGameOver() ) {
        	makeMove();
        }
        
        return wumpusWorld.haveWon();
    }
    
    public boolean makeMove(){
    	if(wumpusWorld.isGameOver() || wumpusWorld.haveWon()){
    		return false;
    	}
    	
    	// Analyze current tile probabilities
        consumeCurrentTile();

        int move = getNextMove();

        // Successfully make a move (didn't change orientation)
        if ( wumpusWorld.move(move) ) {
            //Update position
            currentX = wumpusWorld.getPlayerX();
            currentY = wumpusWorld.getPlayerY();

            debug("New Location: [" + String.valueOf(currentX) + "," + String.valueOf(currentY) + "]-------", true);

            lastMoveAttempt = -1;
        } else {
            //debug("Changed orientation.");
            lastMoveAttempt = move;
        }
        
        return wumpusWorld.haveWon();
    }

    /**
     * Analyze tile and store information about it
     */
    private void consumeCurrentTile(){
        Tile tile = wumpusWorld.getTile(currentX, currentY);
        tile.setPercepts(new ArrayList<>(wumpusWorld.getPerceptions(currentX, currentY)));

        switch ( tile.getObjective() ) {
            case Gold:
                haveFoundGold = true;
                debug("Found gold", !haveFoundGold);
                break;
            case Ladder:
                ladder = tile;
                debug("Found ladder", ladder == null);
                break;
            default:
                break;
        }

        knownLocations.put(String.valueOf(currentX) + '+' + String.valueOf(currentY), tile);
    }

    /**
     * Assess a tile for its probability of containing a Wumpus or pit
     * @param x
     * @param y
     * @return
     */
    private double getTileDangerProbability(int x, int y){
        // Validate tile if we have it
        String tileKey = String.valueOf(x) + "+" + String.valueOf(y);

        if ( knownLocations.containsKey(tileKey) ) {
            if ( knownLocations.get(tileKey).getPercepts().length == 0 ) {
                return 0;
            }
        }

        // use surrounding tiles
        double pitProbability = 0.0;
        double wumpusProbability = 0.0;

        String[] keys = {String.valueOf(x) + '+' + String.valueOf(y-1), String.valueOf(x) + '+' + String.valueOf(y+1),
                String.valueOf(x+1) + '+' + String.valueOf(y), String.valueOf(x-1) + '+' + String.valueOf(y)};

        for(int i = 0; i < keys.length; i++ ) {
            //Check above
            if ( knownLocations.containsKey(keys[i]) ) {
                ArrayList<Percepts> curPercepts = new ArrayList<>(Arrays.asList(knownLocations.get(keys[i]).getPercepts()));

                if ( curPercepts.size() == 0 ) {
                    return 0;
                }

                if ( curPercepts.contains(Percepts.Breeze) ) {
                    pitProbability += 0.25;
                }

                if ( curPercepts.contains(Percepts.Stench) ) {
                    wumpusProbability += 0.25;
                }
            }
        }

        return Math.max(pitProbability, wumpusProbability);
    }

    /**
     * Determine which move to take (Greedy, local)
     * @return Next move direction
     */
    private int getNextMove(){
        int move = DOWN;

        ArrayList<MoveOption> availableMoveOptions = getAvailableMoveOptions();

        // Assign danger probabilities
        for(int i = 0; i < availableMoveOptions.size(); i++ ) {
            MoveOption cur = availableMoveOptions.get(i);

            switch ( cur.direction ) {
                case UP:
                    cur.dangerProbablity = getTileDangerProbability(currentX, currentY - 1);
                    debug("UP danger probability is " + String.valueOf(cur.dangerProbablity), lastMoveAttempt == -1);
                    break;
                case DOWN:
                    cur.dangerProbablity = getTileDangerProbability(currentX, currentY + 1);
                    debug("DOWN danger probability is " + String.valueOf(cur.dangerProbablity), lastMoveAttempt == -1);
                    break;
                case RIGHT:
                    cur.dangerProbablity =  getTileDangerProbability(currentX + 1, currentY);
                    debug("RIGHT danger probability is " + String.valueOf(cur.dangerProbablity), lastMoveAttempt == -1);
                    break;
                case LEFT:
                    cur.dangerProbablity = getTileDangerProbability(currentX - 1, currentY);
                    debug("LEFT danger probability is " + String.valueOf(cur.dangerProbablity), lastMoveAttempt == -1);
                    break;
                default:
                    break;
            }
        }

        // Buckets for sorting danger
        DangerBuckets dangerBuckets = new DangerBuckets();

        // Bucket sort danger probabilities
        for(int i = 0; i < availableMoveOptions.size(); i++) {
            MoveOption currentMoveOption = availableMoveOptions.get(i);

            switch ((int) (currentMoveOption.dangerProbablity * 4)) {
                case 0:
                    dangerBuckets.zero.add(currentMoveOption);
                    break;
                case 1:
                    dangerBuckets.quarter.add(currentMoveOption);
                    break;
                case 2:
                    dangerBuckets.half.add(currentMoveOption);
                    break;
                case 3:
                    dangerBuckets.three.add(currentMoveOption);
                    break;
                case 4:
                    dangerBuckets.full.add(currentMoveOption);
                    break;
                default:
                    throw new IllegalStateException("Invalid danger probability for tile.");
            }
        }

        // If we found the ladder and the gold, let's GTFO - else keep moving son
        if ( haveFoundGold && ladder != null ) {
            if ( pathToLadder == null ) {
                pathToLadder = getPathToLadder();
            }

            if ( pathToLadder != null ){
                move = pathToLadder.get(0);
            }
        } else {
            // Prevent switching orientation a bunch due to random move choices
            move = lastMoveAttempt == -1 ? dangerBuckets.getBestMove() : lastMoveAttempt;
        }

        //debug("Moving to: " + String.valueOf(move));

        return move;
    }

    /**
     * Easily add / remove print statements
     * @param s
     */
    private void debug(String s, Boolean condition){
        if ( debug && condition) {
            System.out.println(s);
        }
    }

    /**
     * Returns the shortest path over known locations to the ladder from our current location as an array of directions
     * (i.e, up, up, right, right, up.)
     * @return Shortest path to ladder
     */
    private ArrayList<Integer> getPathToLadder() {
        ArrayList<Tile> queue = new ArrayList<>();
        HashMap<Tile, Boolean> visited = new HashMap<>();
        HashMap<Tile, Tile> ancestors = new HashMap<>();

        Tile curTile = wumpusWorld.getTile(currentX, currentY);

        queue.add(curTile);
        visited.put(curTile, true);

        // Find ladder
        while ( !queue.isEmpty() ) {
            curTile = queue.get(0);

            // Found ladder, break from search
            if ( curTile.getX() == ladder.getX() && curTile.getY() == ladder.getY() ) {
                break;
            }

            // Wonky ass keys for this sketchy hash map of traversed locations (use a graph? Nahhhh)
            String upKey = String.valueOf(curTile.getX()) + "+" + String.valueOf(curTile.getY() - 1);
            String downKey = String.valueOf(curTile.getX()) + "+" + String.valueOf(curTile.getY() + 1);
            String rightKey = String.valueOf(curTile.getX() + 1) + "+" + String.valueOf(curTile.getY());
            String leftKey = String.valueOf(curTile.getX() - 1) + "+" + String.valueOf(curTile.getY());

            if ( knownLocations.containsKey(upKey) ) {
                Tile up = knownLocations.get(upKey);
                visited.put(up, true);
                ancestors.put(up, curTile);
            }

            if ( knownLocations.containsKey(downKey) ) {
                Tile down = knownLocations.get(downKey);
                visited.put(down, true);
                ancestors.put(down, curTile);
            }

            if ( knownLocations.containsKey(rightKey) ) {
                Tile right = knownLocations.get(rightKey);
                visited.put(right, true);
                ancestors.put(right, curTile);
            }

            if ( knownLocations.containsKey(leftKey) ) {
                Tile left = knownLocations.get(leftKey);
                visited.put(left, true);
                ancestors.put(left, curTile);
            }

            queue.remove(0);
        }

        ArrayList<Tile> pathFromLadderToCurrentPosition = new ArrayList<>();
        
        boolean curTileIsLadderSupposedly = true;

        // Generate path from ancestors
        while ( curTile != wumpusWorld.getTile(currentX, currentY) ) {
            pathFromLadderToCurrentPosition.add(curTile);
            curTile = ancestors.get(curTile);
            curTileIsLadderSupposedly = false;
        }
        if(curTileIsLadderSupposedly){
        	System.out.println(" curTileIsLadderSupposedly ");
        }

        // Reverse path
        Collections.reverse(pathFromLadderToCurrentPosition);

        ArrayList<Integer> directionalPathToLadder = new ArrayList<>();
        int curX = currentX, curY = currentY;

        // Convert to directional path (up, down, yada)
        for(int i = 0; i < pathFromLadderToCurrentPosition.size(); i++ ) {
            Tile nextTile = pathFromLadderToCurrentPosition.get(i);

            if ( nextTile.getX() > curX ) {
                directionalPathToLadder.add(RIGHT);
            } else if ( nextTile.getX() < curX ) {
                directionalPathToLadder.add(LEFT);
            } else if ( nextTile.getY() < curY ) {
                directionalPathToLadder.add(UP);
            } else if ( nextTile.getY() > curY ) {
                directionalPathToLadder.add(DOWN);
            }

            curX = nextTile.getX();
            curY = nextTile.getY();
        }
        
        if(directionalPathToLadder.size() == 0){
        	System.out.println("Path to ladder is empty...");
        }

        return directionalPathToLadder;
    }

    /**
     * A current list of valid moves from our position
     * @return List of valid moves
     */
    private ArrayList<MoveOption> getAvailableMoveOptions(){
        ArrayList<MoveOption> options = new ArrayList<>();

        if ( wumpusWorld.isValidMove(UP) ) {
            MoveOption upOption = new MoveOption();
            upOption.dangerProbablity = 0.0;
            upOption.direction = UP;
            upOption.x = currentX;
            upOption.y = currentY - 1;
            options.add(upOption);
        }

        if ( wumpusWorld.isValidMove(RIGHT)) {
            MoveOption rightOption = new MoveOption();
            rightOption.dangerProbablity = 0.0;
            rightOption.direction = RIGHT;
            rightOption.x = currentX + 1;
            rightOption.y = currentY;
            options.add(rightOption);
        }

        if ( wumpusWorld.isValidMove(DOWN)) {
            MoveOption downOption = new MoveOption();
            downOption.dangerProbablity = 0.0;
            downOption.direction = DOWN;
            downOption.x = currentX;
            downOption.y = currentY + 1;
            options.add(downOption);
        }

        if ( wumpusWorld.isValidMove(LEFT)) {
            MoveOption leftOption = new MoveOption();
            leftOption.dangerProbablity = 0.0;
            leftOption.direction = LEFT;
            leftOption.x = currentX - 1;
            leftOption.y = currentY;
            options.add(leftOption);
        }

        return options;
    }

    /**
     * Represents move option from our current location and the max(probability pit, probability wumpus)
     */
    class MoveOption {
        int direction;
        double dangerProbablity;
        int x;
        int y;
    }

    /**
     * Class for sorting moves by danger probability and returning the best local move option
     */
    class DangerBuckets {
        ArrayList<MoveOption> zero = new ArrayList<>();
        ArrayList<MoveOption> quarter = new ArrayList<>();
        ArrayList<MoveOption> half = new ArrayList<>();
        ArrayList<MoveOption> three = new ArrayList<>();
        ArrayList<MoveOption> full = new ArrayList<>();

        /**
         * Get a random move from the smallest probability bucket
         * I.e, from the smallest not empty bucket, get a random move from it (to avoid a directional bias)
         * @return
         */
        int getBestMove(){
            MoveOption currentMoveOption = null;
            boolean foundOption = false;

            // Try and find an unvisited safe option first and foremost
            if ( !zero.isEmpty() ) {
               currentMoveOption = getRandomMoveOption(zero);
               String currentKey = String.valueOf(currentMoveOption.x) + "+" + String.valueOf(currentMoveOption.y);

               // Only look for an unvisited safe spot if there's more than one available.
               if ( knownLocations.containsKey(currentKey) && zero.size() > 1) {
                   int optionIndex = zero.indexOf(currentMoveOption);

                   for(int i = optionIndex + 1; i < optionIndex + zero.size() - 1; i++ ) {
                       currentMoveOption = zero.get(i % zero.size());
                       currentKey = String.valueOf(currentMoveOption.x) + "+" + String.valueOf(currentMoveOption.y);

                       if ( !knownLocations.containsKey(currentKey) ) {
                           foundOption = true;
                           break;
                       }
                   }

               } else {
                   // If there's only one option it will always choose it..
                   // How to avoid loops? Maybe track number of times we have visited a node?
                   foundOption = true;
               }
            }

            // If you can't find an unvisited zero option, take a chance
            if ( !foundOption ) {
                if ( !quarter.isEmpty() ) {
                    currentMoveOption = getRandomMoveOption(quarter);
                } else if ( !half.isEmpty() ) {
                    currentMoveOption = getRandomMoveOption(half);
                } else if ( !three.isEmpty() ) {
                    currentMoveOption = getRandomMoveOption(three);
                } if ( !full.isEmpty() ) {
                    currentMoveOption = getRandomMoveOption(full);
                }
            }


            return currentMoveOption.direction;
        }

        /**
         * Return a pseudo random element from the given arraylist
         * @param bucket
         * @return
         */
        MoveOption getRandomMoveOption(ArrayList<MoveOption> bucket){
            int randomIndex = ((int) (Math.random() * 100)) % bucket.size();
            return bucket.get(randomIndex);
        }
    }

}
