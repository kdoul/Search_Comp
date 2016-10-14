/**
 * 
 */
package lama;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Konstantinos Douloudis, Maria Siapera, Michail Maria
 * AM E12047, E12109, E12159
 * Πρώτη Εργασία στην Τεχνητή Νοημοσύνη
 */
public class AI_Proj {

	static List<Node> graph; 
	static int totalNodes;
	static String sourceName, goalName;
	static Node goal, source;

	
	public static void main(String[] args) {

		/*
		 * Το αρχείο που γίνεται επεξεργασία είναι το graph.txt
		 * Η μορφή του αρχείου είναι αυτή που δευκρινίστηκε μέσω e-mail
		 * 1st line number of nodes
		 * 2nd line fist_node last_node
		 * 3rd line node_a node_b travel_cost
		 * ...
		 */
		graph = new ArrayList<Node>();
		try(BufferedReader br = new BufferedReader(new FileReader("graph.txt"))) {	        
			String line = br.readLine();
	        for (int i=0 ; line != null ; i++) {	        	
	        	 //Χρήση του ι μέσα στη for για να ξέρουμε σε ποια γραμμή βρισκόμαστε	        	 
	        	if (i==0)
	        		totalNodes = Integer.parseInt(line);
	        	else if (i==1){
	        		String[] splitted = line.split("\\s+");
	        		sourceName = splitted[0];
	        		goalName = splitted[1];
	        	}
	        	else{
	        		String[] splitted = line.split("\\s+");
	        		String firstNode = splitted[0];
	        		String secondNode = splitted[1];
	        		int cost = Integer.parseInt(splitted[2]);	// Σπάσιμο των γραμμών στα κενά 	        			        		 
	        		boolean foundFirst = false, foundSecond = false;
	        		Node  pointerToSecond = null, pointerToFirst = null;
	        		for (Node node : graph){ 
	        			/*
	        			 * αναζήτηση μεσα στον γράφο για το αν υπαρχει κάποιος απο τους κομβους
	        			 * έτσι αν βρουμε ιδιο κομβο κραταμε ενα δεικτη σε αυτον 
	        			 * και αποφευγουμε να εισαγουμε διπλοτυπα
	        			 */
	        			if (node.name.equals(firstNode))
	        				{ pointerToFirst = node; foundFirst = true;}
	        			else if (node.name.equals(secondNode))
	        				{ pointerToSecond = node; foundSecond = true;}
	        			if (foundFirst&&foundSecond)
	        				break;
	        		}
	        		
	        		if(!foundFirst)
	        			{ pointerToFirst = new Node(firstNode); graph.add(pointerToFirst); }
	        		if(!foundSecond)
	        			{ pointerToSecond = new Node(secondNode); graph.add(pointerToSecond); }
	        		Edge edgeToFirst = new Edge(pointerToSecond, cost);
	        		Edge edgeToSecond = new Edge(pointerToFirst, cost);
	        		boolean foundFirstTarget = false, foundSecondTarget = false;
	        		//Ομοιως για τις ακμες
	        		for (Edge e : pointerToFirst.neighbours){
	        			if (e.target.equals(edgeToFirst.target))
	        				{foundFirstTarget = true; break;}
	        		}
	        		for (Edge e : pointerToSecond.neighbours){
	        			if (e.target.equals(edgeToSecond.target))
	        				{foundSecondTarget = true; break;}	        	
	        		}
	        		if (!foundFirstTarget)
	        			pointerToFirst.neighbours.add(edgeToFirst);
	        		if (!foundSecondTarget)
	        			pointerToSecond.neighbours.add(edgeToSecond);	        		
	        	}	        		
	            line = br.readLine();
	        }
	    }	        
		catch (IOException e){
			System.out.println("Could not read file.\n"+e.getMessage()+"\nExiting...");
			System.exit(1);
		}		
		for (Node node : graph){
			if (node.name.equals(sourceName))
				source = node;
			else if (node.name.equals(goalName))
				goal = node;
		}
		h_function(goal);	
		/*
		 * Συνοπτικες πληροφορίες για τον γραφο + αποτελεσματα αναζήτησης
		 */	
		System.out.println("Graph.txt loaded successfully.Search algorithms will run now.");
		System.out.println("Number of nodes:"+totalNodes+" Source is: "+ source + " Goal is: " + goal);
		Node dfsRes = Searches.DFS(source, goal);	
		System.out.println("Depth First Search results:");
		System.out.print("Path from goal to start: " );
		Organise_Res(dfsRes);
		System.out.println("\nTotal Cost: "+dfsRes.cost);
		System.out.println("Total number of memory operations: "+Searches.operations);
		Searches.operations=0; 
		/*Επαναφορα του μετρητη ενεργειων στη μνήμη 
		* πριν απο κάθε εκτελεση καθε αλγοριθμου
		*/
		Node bfsRes = Searches.BFS(source, goal);	
		System.out.println("Breadth First Search results:");
		System.out.print("Path from goal to start: " );
		Organise_Res(bfsRes);
		System.out.println("\nTotal Cost: "+bfsRes.cost);
		System.out.println("Total number of memory operations: "+Searches.operations);
		Searches.operations=0;
		Node ucsRes = Searches.UCS(source, goal);	
		System.out.println("Uniform Cost Search results:");
		System.out.print("Path from goal to start: " );
		Organise_Res(ucsRes);
		System.out.println("\nTotal Cost: "+ucsRes.cost);
		System.out.println("Total number of memory operations: "+Searches.operations);
		Searches.operations=0;
		Node hcsRes = Searches.HCS(source, goal);	
		System.out.println("Hill Climbing Search results:");
		if (hcsRes == null)
			System.out.println("Hill climbing Search has failed.");
		else {
			System.out.print("Path from goal to start: " );
			Organise_Res(hcsRes);
			System.out.println("\nTotal Cost: "+hcsRes.cost);
			System.out.println("Total number of memory operations: "+Searches.operations);
		}
		Searches.operations=0;
		Node asRes = Searches.DFS(source, goal);	
		System.out.println("A* Search results:");
		System.out.print("Path from goal to start: " );
		Organise_Res(asRes);
		System.out.println("\nTotal Cost: "+asRes.cost);
		System.out.println("Total number of memory operations: "+Searches.operations);
		
	}
	/*
	 * η μέθοδος αυτή εκτυπώνει τη διαδρομη απο τον στοχο στον αρχικο κομβο 
	 * ψαχνωντας τον καθε κομβο που ειναι γονιος του τελικου
	 */
	static void Organise_Res(Node result){
		if (result != null)
		{
			System.out.print(result+ " ");
			Organise_Res(result.parent);
		}
	}
	
	/*
	 * Ευρετικη συναρτηση. Ξεκινόντας απο τον στοχο αφαιρουμε 1 απο την πραγματικη αποσταση
	 * και σην συνεχεια προσθετουμε την πραγματικη
	 * Ουσιαστικα η ευρετικη θα ειναι τελεια ευρετικη - 1
	 */
	static void h_function(Node goal){		
		Stack<Node> stack = new Stack<Node>();
		List<Node> visited = new ArrayList<Node>();
		visited.add(goal);
		goal.h_dist = 0;		
		for (Edge e : goal.neighbours)
		{
			e.target.h_dist = e.cost - 1;
			stack.push(e.target);
		}
		while(!stack.empty()){
			Node n = stack.pop();
			visited.add(n);
			for (Edge e : n.neighbours)
			{
				if (!visited.contains(e.target))
				{e.target.h_dist = n.h_dist + e.cost;
				stack.push(e.target);
				}
			}
		}		
	}

}

class Node{
	 
    public final String name; //ονομα κομβου
    public int cost; //κοστος ως αυτον
    public int total_cost; //κοστος μαζι με ευρετικη αποσταση (Α*)
    public int h_dist; //ευρετικη αποσταση
    public List<Edge> neighbours; //ακμες προς γειτονες
    public Node parent; //προηγουμενος κομβος στη διαδρομη που εχουμε ακολουθησει

    public Node(String name){
    	neighbours = new ArrayList<Edge>();
        this.name = name;
    }

    public String toString(){
            return name;
    }

}

class Edge{ 
    public final int cost; //κοστος ακμης
    public final Node target; //δεικτης προς τον γειτονικο κομβο

    public Edge(Node target, int cost){
            this.target = target;
            this.cost = cost;
    }
}