package bdd;

import java.util.Hashtable;
import utils.*;

/**
 * @author Offtermatt
 */
public class BDD {

	/**
	 * A lookup table that maps triples of (var, low, high) to {@Link Node}s n
	 */
	private Hashtable<Triple<Integer, Node, Node>, Node> H;

	/**
	 * The "real" bdd.
	 */
	private Node root;

	/**
	 * Used to denote the number of variables the model for the given boolean
	 * expression has
	 */
	private int n;

	/**
	 * initialises the BDD to work over a given set of variables
	 * 
	 * @param variableCount
	 *            The number of variables that will be used, denoted with the
	 *            integers 1 to n.
	 */
	public BDD(int variableCount) {
		n = variableCount;
		
		//assume that as many nodes as we have variables is a good estimate
		H = new Hashtable<Triple<Integer, Node, Node>, Node>(n);
	}
	
	public Node mk(Triple<Integer, Node, Node> variables){
	
	        int var = variables.getFirst();
	        Node low = variables.getSecond();
	        Node high = variables.getThird();
	        
	        if(low == high){
	        	return low;
	        }
	        
	        Node exist = H.get(variables);
	        
	        if(exist == null){
	        	Node u = new Node(var, low, high);
	        	H.put(variables, u);
	        	return u;
	        }
	        return exist;
	}
	
	
	
}
