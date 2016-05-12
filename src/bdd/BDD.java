package bdd;

import java.util.Hashtable;
import com.bpodgursky.jbool_expressions.*;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

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
	
	//TODO Write javadoc for mk
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
	
	//TODO Javadoc
	public void build(Expression<String> t){
		
	}
	
	//TODO Javadoc, find method of substituting boolean expressions
	private Node buildH(Expression<String> t, int i){
		if(i > n){
			if(RuleSet.simplify(t).equals(Literal.getFalse())){
				return mk(new Triple<Integer, Node, Node>(-1, null, null));
			}
			return mk(new Triple<Integer, Node, Node>(-1, null, null));
		}
		return mk(i, build(RuleSet.assign(t, /*set xi to 0*/), i+1), build(RuleSet.assign(t, /*set xi to 1*/), i+1))));
	}
	
}
