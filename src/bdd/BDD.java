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

	// global references to the terminal nodes, to reduce redundance
	private TrueTerminalNode terminalTrue;
	private FalseTerminalNode terminalFalse;

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

		// assume that as many nodes as we have variables is a good estimate
		H = new Hashtable<Triple<Integer, Node, Node>, Node>(n);
	}

	// TODO Write javadoc for mk
	public Node mk(Triple<Integer, Node, Node> variables) {

		int var = variables.getFirst();
		Node low = variables.getSecond();
		Node high = variables.getThird();

		if (low == high) {
			return low;
		}

		Node exist = H.get(variables);

		if (exist == null) {
			Node u = new NonTerminalNode(var, low, high);
			H.put(variables, u);
			return u;
		}
		return exist;
	}

	// TODO Javadoc
	public void build(String t) {
		root = buildH(t, 0);
	}

	// TODO Javadoc, find method of substituting boolean expressions
	private Node buildH(String t, int i) {
		if (i > n) {
			// TODO: Replace dummySimplify
			if (dummySimplify(t) == "0") {
				return terminalFalse;
			}
			return terminalTrue;
		}

		// TODO: Find replace dummySub
		return mk(new Triple<Integer, Node, Node>(i, buildH(dummySub(t, i, false), i + 1),
				buildH(dummySub(t, i, true), i + 1)));
	}

	private String dummySub(String t, int var, boolean value) {

		int v = value ? 1 : 0;
		return t.replace("x" + var, "" + v);

	}

	private String dummySimplify(String t) {

		return t;

	}

	/**
	 * Apply a binary operator to two trees.
	 * @param op A string representation of the operation to perform. Supports &, |, ->, xor, <->.
	 * @param u1 One of the trees the operation will be applied to.
	 * @param u2 One of the trees the operation will be applied to.
	 * @return The tree that results from applying op to the two trees u1, u2.
	 */
	public Node apply(String op, Node u1, Node u2) {

		// Stores previously computed values of applying op on the two nodes.
		// Might save computing time.
		Hashtable<Triple<String, Node, Node>, Node> computed = new Hashtable<Triple<String, Node, Node>, Node>();
		
		return applyH(op, u1, u2, computed);

	}

	/**
	 * @param op The operation to perform.
	 * @param u1 One of the trees over which the action should be performed.
	 * @param u2 One of the trees over which the action should be performed.
	 * @param computed A table of previously computed results.
	 * @return The tree that results from applying op to the two trees u1, u2.
	 */
	public Node applyH(String op, Node u1, Node u2, Hashtable<Triple<String, Node, Node>, Node> computed) {

		//Define the shortcut input to avoid more huge if conditions
		Triple<String, Node, Node> input = new Triple<String, Node, Node>(op, u1, u2);
		Node u;

		if (computed.containsKey(input)) {
			return computed.get(input);
		}
		// -1 denotes the terminal node 0
		if (u1 instanceof TerminalNode && u2 instanceof TerminalNode) {
			u = app(op, (TerminalNode) u1, (TerminalNode) u2);
		} else {
			if (((NonTerminalNode) u1).getVar() == ((NonTerminalNode) u2).getVar()) {
				u = mk(new Triple<Integer, Node, Node>(((NonTerminalNode) u1).getVar(),
						applyH(op, ((NonTerminalNode) u1).getLow(), ((NonTerminalNode) u2).getLow(), computed),
						applyH(op, ((NonTerminalNode) u1).getHigh(), ((NonTerminalNode) u2).getHigh(), computed)));
			} else {
				if (((NonTerminalNode) u1).getVar() < ((NonTerminalNode) u2).getVar()) {
					u = mk(new Triple<Integer, Node, Node>(((NonTerminalNode) u1).getVar(),
							applyH(op, ((NonTerminalNode) u1).getLow(), u2, computed),
							applyH(op, ((NonTerminalNode) u1).getHigh(), u2, computed)));
				} else{
					u = mk(new Triple<Integer, Node, Node>(((NonTerminalNode) u1).getVar(),
							applyH(op, u1, ((NonTerminalNode) u2).getLow(), computed),
							applyH(op, u1, ((NonTerminalNode) u2).getHigh(), computed)));
				}
			}
		}
		computed.put(input, u);
		return u;
	}
	
	/**
	 * assigns a variable a value in a given tree
	 * @param u The tree over which to replace the variable
	 * @param var The variable to replace
	 * @param value The boolean value true or false with which to replace it
	 * @return
	 */
	public Node restrict(Node u, int var, boolean value){
		
	}

	/**
	 * applies an operator to two terminal nodes, so the result is also a terminal node.
	 * @param op string representation of the operator. supports &, |, ->, xor, <->.
	 * @param u1 Terminal node over which to compute the operator.
	 * @param u2 Terminal node over which to compute the operator.
	 * @return A terminal node of the result of the operation.
	 */
	public TerminalNode app(String op, TerminalNode u1, TerminalNode u2) {
		switch (op) {
		case "&":
			if (u1 instanceof TrueTerminalNode && u2 instanceof TrueTerminalNode) {
				return new TrueTerminalNode(n);
			} else {
				return new FalseTerminalNode(n);
			}
		case "|":
			if (u1 instanceof TrueTerminalNode || u2 instanceof TrueTerminalNode) {
				return new TrueTerminalNode(n);
			} else {
				return new FalseTerminalNode(n);
			}
		case "->":
			if (!(u1 instanceof TrueTerminalNode && !(u2 instanceof TrueTerminalNode))) {
				return new TrueTerminalNode(n);
			} else {
				return new FalseTerminalNode(n);
			}
		case "<->":
			if ((u1 instanceof TrueTerminalNode && u2 instanceof TrueTerminalNode)
					|| (u1 instanceof FalseTerminalNode && u2 instanceof FalseTerminalNode)) {
				return new TrueTerminalNode(n);
			} else {
				return new FalseTerminalNode(n);
			}
		case "xor":
			if ((u1 instanceof TrueTerminalNode && u2 instanceof FalseTerminalNode)
					|| (u1 instanceof FalseTerminalNode && u2 instanceof TrueTerminalNode)) {
				return new TrueTerminalNode(n);
			} else {
				return new FalseTerminalNode(n);
			}
		default:
			return new FalseTerminalNode(n);
		}
	}

}
