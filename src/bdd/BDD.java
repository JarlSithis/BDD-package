package bdd;

import java.util.Collections;
import java.util.Hashtable;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Literal;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

import utils.FalseTerminalNode;
import utils.GraphPainter;
import utils.Node;
import utils.NonTerminalNode;
import utils.NotSatisfiableException;
import utils.TerminalNode;
import utils.Triple;
import utils.TrueTerminalNode;

/**
 * @author philip
 */
public class BDD {

	/**
	 * A lookup table that maps triples of (var, low, high) to {@Link Node}s n
	 */
	private Hashtable<Triple<Integer, Node, Node>, Node> lookupTable;

	/**
	 * The "real" bdd.
	 */
	private Node root;

	// global references to the terminal nodes, to reduce redundancy
	private TrueTerminalNode terminalTrue;

	public TrueTerminalNode getTerminalTrue() {
		return terminalTrue;
	}

	public FalseTerminalNode getTerminalFalse() {
		return terminalFalse;
	}

	private FalseTerminalNode terminalFalse;

	/**
	 * Used to denote the number of variables the model for the given boolean
	 * expression has
	 */
	private int n;

	/**
	 * Initializes the BDD to work over a given set of variables
	 * 
	 * @param variableCount
	 *            The number of variables that will be used, denoted with the
	 *            integers 1 to n.
	 */
	public BDD(int variableCount) {
		n = variableCount;

		// assume that as many nodes as we have variables is a good estimate
		lookupTable = new Hashtable<Triple<Integer, Node, Node>, Node>(n * n);

		terminalTrue = new TrueTerminalNode(n);
		terminalFalse = new FalseTerminalNode(n);
	}

	public Node getRoot() {
		return root;
	}

	public Hashtable<Triple<Integer, Node, Node>, Node> getLookupTable() {
		return lookupTable;
	}

	/**
	 * Builds a node, using the values from the triple.
	 * 
	 * @param variables
	 *            The variable number of the node, it's low successor, and then
	 *            it's high successor
	 * @return A new node u, if the triple was not yet in the lookup table, or
	 *         the existing node which the triple is mapped to in the table.
	 */
	public Node mk(Triple<Integer, Node, Node> variables) {

		int var = variables.getFirst();
		Node low = variables.getSecond();
		Node high = variables.getThird();

		if (low.equals(high)) {
			return low;
		}
		Node exist = lookupTable.get(variables);

		if (exist == null) {
			Node u = new NonTerminalNode(var, low, high);
			lookupTable.put(variables, u);
			return u;
		}

		return exist;

	}

	/**
	 * Builds a ROBDD, which can then be accessed via the getRoot() command.
	 * 
	 * @param t
	 *            The string representation of the boolean expression the ROBDD
	 */

	public void build(Expression<String> t) {
		root = buildH(t, 1);
	}

	private Node buildH(Expression<String> t, int i) {
		if (i > n) {
			if (RuleSet.simplify(t).equals(Literal.getFalse())) {
				return terminalFalse;
			}
			return terminalTrue;
		}

		Node l = buildH(RuleSet.assign(t, Collections.singletonMap("x" + i, false)), i + 1);
		Node h = buildH(RuleSet.assign(t, Collections.singletonMap("x" + i, true)), i + 1);
		return mk(new Triple<Integer, Node, Node>(i, l, h));
	}

	/**
	 * Apply a binary operator to two ROBDDs of the same variables. This method
	 * has to be invoked on a fresh BDD instance, which will store the result.
	 * Warning: Both ROBDDs need to have the same variable count, otherwise this
	 * method may exhibit unspecified behavior.
	 * 
	 * @param op
	 *            A string representation of the operation to perform. Supports
	 *            &, |, ->, xor, <->.
	 * @param u1
	 *            One of the ROBDDs the operation will be applied to.
	 * @param u2
	 *            One of the ROBDDs the operation will be applied to.
	 */
	public void apply(String op, BDD u1, BDD u2) {

		// Stores previously computed values of applying op on the two nodes.
		// Might save computing time.
		Hashtable<Triple<String, Node, Node>, Node> computed = new Hashtable<Triple<String, Node, Node>, Node>();

		root = applyH(op, u1.getRoot(), u2.getRoot(), computed);

	}

	private Node applyH(String op, Node u1, Node u2, Hashtable<Triple<String, Node, Node>, Node> computed) {

		// Define the shortcut input
		Triple<String, Node, Node> input = new Triple<String, Node, Node>(op, u1, u2);
		Node u;

		if (computed.containsKey(input)) {
			return computed.get(input);
		}

		if (u1 instanceof TerminalNode && u2 instanceof TerminalNode) {
			u = app(op, (TerminalNode) u1, (TerminalNode) u2);

			computed.put(input, u);
			return u;
		}
		if (u1.getVar() == u2.getVar()) {
			Node low = applyH(op, ((NonTerminalNode) u1).getLow(), ((NonTerminalNode) u2).getLow(), computed);
			Node high = applyH(op, ((NonTerminalNode) u1).getHigh(), ((NonTerminalNode) u2).getHigh(), computed);
			u = mk(new Triple<Integer, Node, Node>(((NonTerminalNode) u1).getVar(), low, high));

			computed.put(input, u);
			return u;
		}
		if (u1.getVar() < u2.getVar()) {
			u = mk(new Triple<Integer, Node, Node>(((NonTerminalNode) u1).getVar(),
					applyH(op, ((NonTerminalNode) u1).getLow(), u2, computed),
					applyH(op, ((NonTerminalNode) u1).getHigh(), u2, computed)));

			computed.put(input, u);
			return u;
		}
		if (u1.getVar() > u2.getVar()) {
			u = mk(new Triple<Integer, Node, Node>(((NonTerminalNode) u2).getVar(),
					applyH(op, u1, ((NonTerminalNode) u2).getLow(), computed),
					applyH(op, u1, ((NonTerminalNode) u2).getHigh(), computed)));

			computed.put(input, u);
			return u;
		}

		return null;
	}

	/**
	 * assigns a variable a value in a given ROBDD
	 * 
	 * @param u
	 *            The ROBDD over which to replace the variable
	 * @param var
	 *            The variable to replace
	 * @param value
	 *            The boolean value true or false with which to replace it
	 * @return
	 */
	public void restrict(int var, boolean value) {

		root = restrictH(root, var, value);

		// clean the lookup-table from references to nodes no longer in the tree
		// and rebuild it
		lookupTable.clear();
		root.cleanUp(lookupTable);
	}

	private Node restrictH(Node u, int var, boolean value) {
		if (u.getVar() > var) {
			return u;
		}
		if (u.getVar() < var) {
			Node low = restrictH(((NonTerminalNode) u).getLow(), var, value);
			Node high = restrictH(((NonTerminalNode) u).getHigh(), var, value);
			return mk(new Triple<Integer, Node, Node>(u.getVar(), low, high));
		}
		// u.getVar() = j, u is the variable that we want to replace
		if (value) {
			return restrictH(((NonTerminalNode) u).getHigh(), var, value);
		} else {
			return restrictH(((NonTerminalNode) u).getLow(), var, value);
		}

	}

	/**
	 * Counts the number of satisfying truth assignments of the ROBDD this is called on.
	 * 
	 * @param u
	 *            The root of the ROBDD.
	 * @return The number of satisfying truth assignments.
	 */
	public double satCount() {
		return Math.pow(2, root.getVar() - 1) * satCountH(root);
	}

	private double satCountH(Node u) {
		if (u instanceof TrueTerminalNode) {
			return 1;
		} else if (u instanceof FalseTerminalNode) {
			return 0;
		} else { // u isn't terminal
			return Math.pow(2, ((NonTerminalNode) u).getLow().getVar() - u.getVar() - 1)
					* satCountH(((NonTerminalNode) u).getLow())
					+ Math.pow(2, ((NonTerminalNode) u).getHigh().getVar() - u.getVar() - 1)
							* satCountH(((NonTerminalNode) u).getHigh());
		}
	}

	/**
	 * Finds a satisfying truth assignment of the ROBDD it is called on.
	 * @return A satisfying truth assignment, given as a string of "xi -> 0|1"
	 * @throws NotSatisfiableException
	 */
	public String anySat() throws NotSatisfiableException{
		return anySatH(root);
	}
	
	private String anySatH(Node u) throws NotSatisfiableException {
		if (u instanceof FalseTerminalNode) {
			throw new NotSatisfiableException();
		}
		if (u instanceof TrueTerminalNode) {
			return "";
		}
		if (((NonTerminalNode) u).getLow() instanceof FalseTerminalNode) {
			return "x" + u.getVar() + " -> 1, " + anySatH(((NonTerminalNode) u).getHigh());
		}
		return "x" + u.getVar() + " -> 0, " + anySatH(((NonTerminalNode) u).getLow());
	}

	public void draw() {
		GraphPainter.draw(this);
	}

	/**
	 * applies an operator to two terminal nodes, so the result is also a
	 * terminal node.
	 * 
	 * @param op
	 *            string representation of the operator. supports &, |, ->, xor,
	 *            <->.
	 * @param u1
	 *            Terminal node over which to compute the operator.
	 * @param u2
	 *            Terminal node over which to compute the operator.
	 * @return A terminal node of the result of the operation.
	 */
	private TerminalNode app(String op, TerminalNode u1, TerminalNode u2) {
		switch (op) {
		case "&":
			if (u1 instanceof TrueTerminalNode && u2 instanceof TrueTerminalNode) {
				return terminalTrue;
			} else {
				return terminalFalse;
			}
		case "|":
			if (u1 instanceof TrueTerminalNode || u2 instanceof TrueTerminalNode) {
				return terminalTrue;
			} else {
				return terminalFalse;
			}
		case "->":
			if (!(u1 instanceof TrueTerminalNode && !(u2 instanceof TrueTerminalNode))) {
				return terminalTrue;
			} else {
				return terminalFalse;
			}
		case "<->":
			if ((u1 instanceof TrueTerminalNode && u2 instanceof TrueTerminalNode)
					|| (u1 instanceof FalseTerminalNode && u2 instanceof FalseTerminalNode)) {
				return terminalTrue;
			} else {
				return terminalFalse;
			}
		case "xor":
			if ((u1 instanceof TrueTerminalNode && u2 instanceof FalseTerminalNode)
					|| (u1 instanceof FalseTerminalNode && u2 instanceof TrueTerminalNode)) {
				return terminalTrue;
			} else {
				return terminalFalse;
			}
		default:
			return terminalFalse;
		}
	}

	public boolean equals(Object o) {
		if (!(o instanceof BDD)) {
			return false;
		}
		return root.equals(((BDD) o).getRoot());
	}

	public int hashCode() {
		return 37 * root.hashCode() + 49 * n;
	}
}
