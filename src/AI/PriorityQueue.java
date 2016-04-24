package AI;

import java.util.ArrayList;
import java.util.Arrays;

import AI.GlobalAI.GraphNode;

//Used for keeping unvisited nodes sorted by lowest danger probability
public class PriorityQueue {
	
	ArrayList<GraphNode> queue = new ArrayList<GraphNode>();
	
	public void add(GraphNode node){
		int index = queue.size();
		while(index > 0 && node.compareTo(queue.get(index - 1)) < 0) {
			index--;
		}
		queue.add(index, node);
	}
	
	public void update(){
		GraphNode[] nodes = new GraphNode[queue.size()];
		Arrays.sort(queue.toArray(nodes));
		queue.clear();
		for(int i=0; i<nodes.length; i++){
			queue.add(nodes[i]);
		}
	}
	
	public GraphNode remove(){
		if(queue.size() == 0){
			throw new IllegalStateException("No elements in queue to be removed");
		}
		return queue.remove(0);
	}
	
	public boolean isEmpty(){
		return queue.isEmpty();
	}
}

//Used for DFS (Essentially inserts at the front of the queue now)
class PriorityStack extends PriorityQueue {
	
	@Override
	public void add(GraphNode node){
		
		int index = 0;
		if(index < queue.size() && queue.get(index).compareTo(node) < 0) {
			index++;
		}
		queue.add(index, node);
	}

}
