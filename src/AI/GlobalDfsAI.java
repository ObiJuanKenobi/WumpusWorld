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
 * AI agent that uses a DFS search approach, looking at the entire known map to make the safest moves.
 * Identical to GlobalAI, except uses a stack instead of queue for picking unvisited nodes, which
 * leads to less backtracking
 * @author Jacob
 *
 */
public class GlobalDfsAI extends GlobalAI {

    public GlobalDfsAI(WumpusWorld world) {
        super(world);
        unvisitedNodesQueue = new PriorityStack();
    }

}
