package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import logic.Objectives;
import logic.Percepts;
import logic.WumpusWorld;

/**
 * AI agent that uses a BFS search approach, looking at the entire known map to make the safest moves.
 * @author Jacob
 *
 */
public class GlobalAI extends AI {

	//Graph representation of our knowledge of the map
	private HashMap<GraphNode, HashSet<GraphNode>> graph =  new HashMap<GraphNode, HashSet<GraphNode>>();
	
	//Gets the graphnode for a given tile-string, which defined in getTileString()
	private HashMap<String, GraphNode> tileStringsToNodes = new HashMap<String, GraphNode>();
	
	//Represents the tile of the graph the AI is currently on.
	private GraphNode currentNode;

    //Stores a reference to the ladder once found if haven't found gold yet
    private GraphNode ladder;
    
    private boolean debug = true;
    
    private boolean foundWumpus = false;
    
    //This AI will always be moving based on a path.
    // When the path is empty, the AI will search for the next best path to take
    private ArrayList<Point> path = new ArrayList<Point>();
    
    //Priority Queue of unvisited tiles
    // first to be removed is the most safe square to move to 
    protected PriorityQueue unvisitedNodesQueue = new PriorityQueue();


    public GlobalAI(WumpusWorld world) {
        super(world);
    }

    public boolean play(){

    	currentNode = new GraphNode(currentX, currentY);
    	addNodeToGraph(currentNode);

        while (!wumpusWorld.haveWon() && !wumpusWorld.isGameOver()) {
        	
        	makeMove();
        }
        
        return wumpusWorld.haveWon();
    }
    
    // returns true if won on this move
    public boolean makeMove(){
    	if(wumpusWorld.isGameOver() || wumpusWorld.haveWon()){
    		return false;
    	}
    	
    	// Analyze current tile probabilities
        consumeCurrentTile();

        int move = getNextMove();

        // Successfully make a move (didn't change orientation)
        if (wumpusWorld.move(move)) {
            //Update position
            currentX = wumpusWorld.getPlayerX();
            currentY = wumpusWorld.getPlayerY();
            
            if(debug)
            	System.out.println("New Location: [" + String.valueOf(currentX) + "," + String.valueOf(currentY) + "]-------");
            
            //Update currentNode:
            currentNode = tileStringsToNodes.get(getTileString(currentX, currentY));
            path.remove(0); 

        } 
        
        return wumpusWorld.haveWon();
    	
    }
    
    private void printGraph(){
    	for(String s : tileStringsToNodes.keySet()){
    		System.out.println(s);
    		for(GraphNode edge : graph.get(tileStringsToNodes.get(s))){
    			System.out.println("   " + getTileString(edge.x, edge.y));
    		}
    	}
    }
    
    private void printPath(){
    	System.out.println("Path: ");
    	for(Point p : path){
    		System.out.println("  " + p.x + " , " + p.y);
    	}
    }
    
    private void printQueue() {
    	System.out.println("Queue: ");
    	for(GraphNode node : unvisitedNodesQueue.queue){
    		System.out.println("  " + getTileString(node.x, node.y) + " , " + node.risk);
    	}
    }
    
    private void addNodeToGraph(GraphNode node){
    	tileStringsToNodes.put(getTileString(node.x, node.y), node);
    	graph.put(node, new HashSet<GraphNode>());
    }
    
    private String getTileString(int x, int y){
    	return new String(x + "#" + y);
    }
    
    private int getNextMove(){
    	if(path.isEmpty()) {
    		if(haveFoundGold && ladder != null) {
    			setPath(ladder);
    		}
    		else {
	    		GraphNode target = unvisitedNodesQueue.remove();
	    		setPath(target);
    		}
    		if(debug)
    			printPath();
    	}
    	
    	Point nextTile = path.get(0);
    	if(nextTile.x == currentNode.x && nextTile.y == currentNode.y){
    		path.remove(0);
    		if(path.isEmpty()){
    			System.out.println("Path empty");
    		}
    		nextTile = path.get(0);
    	}
    	
    	return getNextDirection(currentNode.getPoint(), nextTile);
    }
    
    private int getNextDirection(Point currentTile, Point nextTile){
    	if(nextTile.y > currentTile.y){
    		return DOWN;
    	}
    	if(nextTile.y < currentTile.y){
    		return UP;
    	}
    	if(nextTile.x > currentTile.x){
    		return RIGHT;
    	}
    	if(nextTile.x < currentTile.x){
    		return LEFT;
    	}
    	else{
    		System.out.println("getNextDirection error");
    		return -1;
    	}
    }
    
    //BFS on graph to find path to target
    private void setPath(GraphNode target){
    	
    	path.clear();
    	
    	ArrayList<GraphNode> queue = new ArrayList<GraphNode>();
    	for(GraphNode node : graph.keySet()){
    		node.visitedInSearch = false;
    		node.parent = null;
    	}
    	queue.add(currentNode);
    	
    	while(!queue.isEmpty()){
    		GraphNode previous = queue.remove(0);
    		previous.visitedInSearch = true;
    		for(GraphNode edge : graph.get(previous)){
    			if(!edge.visitedInSearch && edge.visited){
    				edge.parent = previous;
	        		queue.add(edge);
    			}
    			if(edge == target){
    				target.parent = previous;
    				GraphNode cur = target;
    				while(cur != null){
    					path.add(0, cur.getPoint());
    					cur = cur.parent;
    				}
    				return;
    			}
        	}
    		
    	}
    	
    	System.out.println("BFS failed...");
    }
    
    //Analyzes any new percepts, and expands graph if a new tile, and
    // updates risk probabilities for whole graph
    private void consumeCurrentTile() {
    	if(currentNode.visited){
    		return;
    	}

    	currentNode.visited = true;
    	addGraphEdges(currentNode);
    	
    	Objectives objective = wumpusWorld.getTile(wumpusWorld.getPlayerX(), wumpusWorld.getPlayerY()).getObjective();
    	switch(objective){
    	
	    	case Gold:
	    		haveFoundGold = true;
	    		System.out.println("Have found gold");
	    		break;
	    		
	    	case Ladder:
	    		ladder = currentNode;
	    		if(haveFoundGold){
	    			System.out.println("Global AI has won");
	    		}
	    		break;
	    		
	    	case Wumpus:
	    		//Should have already lost..
	    	case Pit:
	    		//Should have already lost..
	    		System.out.println("AI lost - Error");
	    		break;
	    		
	    	case Empty:
	    	default:
	    		
	    		break;
    		
    	}
    	
    	updateEdgeRisks(currentNode);
    	
    	updateRisks();
    	
    	unvisitedNodesQueue.update();
    	
    	if(debug){
	    	printGraph();
	    	System.out.println();
	    	printQueue();
	    	System.out.println();
	    	wumpusWorld.printWorldObjectives();
    	}
    	
    }
	    
    //Updates the risk-prob of the edges of the given node:
    private void updateEdgeRisks(GraphNode node) {
    	Collection<Percepts> percepts = wumpusWorld.getPerceptions(node.x, node.y);
    	ArrayList<GraphNode> unvisitedEdges = getUnvisitedUnknownEdges(node);
    	
    	int notPits = 0;
    	int notWumpuss = 0;
    	boolean otherIsWumpus = false;
    	
    	for(GraphNode edge : unvisitedEdges) {
	    	if(edge.notPit){
	    		notPits++;
	    	}
	    	if(edge.notWumpus){
	    		notWumpuss++;
	    	}
	    	if(edge.wumpus){
	    		otherIsWumpus = true;
	    	}
    	}
    	
    	for(GraphNode edge : unvisitedEdges) {
	    	edge.addPercepts(percepts, unvisitedEdges.size(), notPits, notWumpuss, otherIsWumpus);
    	}
    }
    
    //Constructs edges in the graph for the given node and it's neighbors
	private void addGraphEdges(GraphNode node){
		HashSet<GraphNode> edges = graph.get(node);
		
		//Up:
		if(node.y > 0){
			addGraphEdge(edges, node.x, node.y - 1);
		}
		
		//Down:
		if(node.y < wumpusWorld.getMapSize()-1){
			addGraphEdge(edges, node.x, node.y + 1);
		}
		
		//Left:
		if(node.x > 0){
			addGraphEdge(edges, node.x-1, node.y);
		}
		
		//Right:
		if(node.x < wumpusWorld.getMapSize()-1){
			addGraphEdge(edges, node.x+1, node.y);
		}
	}
	
	private void addGraphEdge(HashSet<GraphNode> edges, int x, int y){
		String tileString = getTileString(x, y);
		if(tileStringsToNodes.containsKey(tileString)) {
			edges.add(tileStringsToNodes.get(tileString));
		}
		else {
			GraphNode edge = new GraphNode(x, y);
			unvisitedNodesQueue.add(edge);
			addNodeToGraph(edge);
			edges.add(edge);
		}
	}
	
	private ArrayList<GraphNode> getUnvisitedUnknownEdges(GraphNode currentNode){
		ArrayList<GraphNode> edges = new ArrayList<GraphNode>();
		for(GraphNode edge : graph.get(currentNode)){
			if(edge.visited == false && edge.safe == false /*&& !edge.wumpus && !edge.pit*/) {
				edges.add(edge);
			}
		}
		
		return edges;
	}
	
	//As we gain more and more information about the board,
	// we will need to update previous risk probabilities 
	// e.g. if two squares initially each have a 50% chance
	// of being a pit, and we find out one is safe, the other 
	// must be marked with 1.0 probability
	private void updateRisks(){
		for(GraphNode node : graph.keySet()){
			node.risk = 0.0;
			node.visitedInSearch = false;
			node.stenches = 0;
			node.breezes = 0;
		}
		
		ArrayList<GraphNode> queue = new ArrayList<GraphNode>();
		queue.add(tileStringsToNodes.get(getTileString(0, 0)));
		
    	while(!queue.isEmpty()){
    		
    		GraphNode previous = queue.remove(0);
    		previous.visitedInSearch = true;
    		updateEdgeRisks(previous);
    		
    		for(GraphNode edge : graph.get(previous)){
    			if(!edge.visitedInSearch){
	        		queue.add(edge);
	        		edge.visitedInSearch = true;
    			}
        	}
    		
    	}
	}
	
	//Encapsulates information AI needs for a given tile in the map
	class GraphNode implements Comparable<GraphNode> {
		
		//Coordinates of tile on the board
		public int x, y;
		
		//Flag for whether the AI has visited the node yet or not
		public boolean visited;
		
		//Used while the square is unvisited to guide the search
		public int stenches = 0;
		public int breezes = 0;
		
		//These flags are turned to true only when that associated objective/percept is confirmed
		public boolean glitter = false;
		public boolean safe = false;
		public boolean wumpus = false;
		public boolean notWumpus = false;
		public boolean pit = false;
		public boolean notPit = false;
		
		//Probability of encountering a danger in this square (if not visited yet)
		public double risk = 0.0;
		
		// used in BFS to find path back to start node
		public GraphNode parent; 
		
		// used in BFS so that a node isn't visited multiple times to avoid cycles
		public boolean visitedInSearch; 
		
		public GraphNode(int x, int y){
			this.x = x;
			this.y = y;
			visited = false;
		}
		
		public void addPercepts(Collection<Percepts> percepts, int numNeighbors, int notPits, int notWumpuss, boolean otherIsWumpus){
			if(percepts.size() == 0){
				safe = true;
				risk = 0.0;
				return;
			}
			if(percepts.size() == 1 && percepts.contains(Percepts.Glitter)) {
				safe = true;
				risk = 0.0;
				glitter = true;
				return;
			}
			if(percepts.size() == 1 && percepts.contains(Percepts.Stench)) {
				notPit = true;
				if(numNeighbors == 1){
					risk = 10.0;
					foundWumpus = true;
					wumpus = true;
					System.out.println("Found wumpus");
					return;
				}
				
			}
			if(percepts.size() == 1 && percepts.contains(Percepts.Breeze)) {
				notWumpus = true;
				if(numNeighbors == 1){
					pit = true;
					risk = 10.0;
					return;
				}
			}
			
			for(Percepts percept : percepts){
				switch(percept){
				
					case Breeze:
						
						if(!notPit) {
							if(numNeighbors - notPits == 1){
								pit = true;
								risk = 10.0;
							}
							else {
								breezes++;
								risk += 1.0 / (double) numNeighbors;
							}
						}
						break;
						
					case Stench:
						if(otherIsWumpus){
							notWumpus = true;
							stenches = 0;
						}
						if(!notWumpus){
							if(!foundWumpus){
								if(numNeighbors - notWumpuss == 1){
									//confirms the wumpus is in this square
									risk = 10.0;
									foundWumpus = true;
									wumpus = true;
									System.out.println("Found wumpus");
									return;
								}
								stenches++;
								risk += 1.0 / (double) numNeighbors;
							}
							if(stenches == 3){
								//confirms the wumpus is in this square
								risk = 10.0;
								foundWumpus = true;
								wumpus = true;
								System.out.println("Found wumpus");
							}
						}
						
						break;
						
					case Glitter:
						glitter = true;
						break;
				}
			}
		}
		
		public Point getPoint() {
			return new Point(x, y);
		}
		
		@Override 
		public int hashCode(){
			return getTileString(x, y).hashCode();
		}

		@Override
		public int compareTo(GraphNode other) {
			if(this.wumpus){
				return 1;
			}
			if(other.wumpus){
				return -1;
			}
			
			
			if(this.risk < other.risk || (this.risk == this.risk && this.glitter && !other.glitter && !haveFoundGold))
				return -1;
			else
				return 1;
		}
	}
	
	
	class Point {
		public int x;
		public int y;
		
		public Point(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
}
