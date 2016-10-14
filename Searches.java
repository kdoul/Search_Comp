package lama;

import java.util.*;

public class Searches {
	public static int operations; //Memory operation counter	
	
	//Αναζητηση κατα βαθος
	public static Node DFS(Node start, Node goal){
		Stack<Node> stack = new Stack<Node>();
		List<Node> visited = new ArrayList<Node>(); //Λιστα με κομβους που εχουμε επισκευθει
		stack.push(start);
		while(true){
			operations++;
			Node n = stack.pop();
			if (n.name.equals(goal.name))
				return n;
			else{
			visited.add(n);
			for (Edge e : n.neighbours){
				if (!visited.contains(e.target)){
					e.target.cost = e.cost + n.cost; //Κραταμε παντα αθροισμα κοστους για να το εχουμε ως μετρο συγκρισης
					e.target.parent = n;
					stack.push(e.target);
					operations++;
					}
			}
			}
		}
	}
	
	public static Node BFS(Node start, Node goal){
		Queue<Node> queue = new LinkedList<Node>();
		List<Node> visited = new ArrayList<Node>();
		queue.add(start);
		while(true){
			operations++;
			Node n = queue.poll();
			if (n.name.equals(goal.name))
				return n;
			else{
				visited.add(n);
				for (Edge e : n.neighbours){
					if (!visited.contains(e.target)){
						e.target.cost = e.cost + n.cost;
						e.target.parent = n;
						queue.add(e.target);
						operations++;
					}
				}
			}
		}
	}
	
	public static Node UCS(Node start, Node goal){
		Comparator<Node> comparator = new UCSComparator();
		List<Node> visited = new ArrayList<Node>();
		PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);
		queue.add(start);
		while (true){
			operations++;
			Node n = queue.poll();
			if (n.name.equals(goal.name))
				return n;
			else{
				visited.add(n);
				for (Edge e : n.neighbours){
					if (!visited.contains(e.target)){
						e.target.cost = e.cost + n.cost;
						e.target.parent = n;
						queue.add(e.target);
						operations++;
					}
				}
			}
		}
	}
	
	public static Node HCS(Node start, Node goal){
		Comparator<Node> comparator = new HCSComparator();
		PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);
		queue.add(start);
		while (!queue.isEmpty()){
			operations++;
			Node n = queue.poll();
			if (n.name.equals(goal.name))
				return n;
			else
			{
				for (Edge e : n.neighbours){
					if (e.target.h_dist <= n.h_dist){
						e.target.cost = e.cost + n.cost;
						e.target.parent = n;
						queue.add(e.target);
						operations++;
					}
					
				}
			}
		}
		return null;
	}
	
	public static Node AStar(Node start, Node goal){
		Comparator<Node> comparator = new AStarComparator();
		List<Node> visited = new ArrayList<Node>();
		PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);
		queue.add(start);
		while(true){
			Node n = queue.poll();
			operations++;
			if (n.name.equals(goal.name))
				return n;
			else{
				visited.add(n);
				for (Edge e : n.neighbours){
					if (!visited.contains(e.target)){
						e.target.cost = e.cost + n.cost;
						e.target.total_cost = e.cost + e.target.h_dist + n.total_cost;
						e.target.parent = n;
						queue.add(e.target);
						operations++;
					}
				}
			}
		}
	}
}

//Αυτες ειναι οι απαραιτητες κλασεις για την δημιουργια Comperator των ουρων αναμονης βασει του αντιστοιχου κοστους
class UCSComparator implements Comparator<Node>{
	@Override
	public int compare(Node a, Node b){
		return a.cost - b.cost;
	}
}

class HCSComparator implements Comparator<Node>{
	@Override
	public int compare(Node a, Node b){
		return a.h_dist - b.h_dist;
	}
}

class AStarComparator implements Comparator<Node>{
	@Override
	public int compare(Node a, Node b){
		return a.total_cost - b.total_cost;
	}
}
