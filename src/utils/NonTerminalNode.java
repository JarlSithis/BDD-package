package utils;

public class NonTerminalNode implements Node {

	/**
	 * The index of the variable. NonTerminal Notes always have var = n+1, where
	 * n is the number of variables of the corresponding ROBDD.
	 */
	private int var;
	private Node high;
	private Node low;

	public int getVar() {
		return var;
	}

	public void setVar(int var) {
		this.var = var;
	}

	public Node getHigh() {
		return high;
	}

	public void setHigh(Node high) {
		this.high = high;
	}

	public Node getLow() {
		return low;
	}

	public void setLow(Node low) {
		this.low = low;
	}

	public NonTerminalNode(int var, Node high, Node low) {
		super();
		this.var = var;
		this.high = high;
		this.low = low;
	}

	// TODO:Draw
	public String draw() {
		return "(Node x" + var + "has low|high successors: " + low.draw() + "|" + high.draw() + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NonTerminalNode)) {
			return false;
		} else {
			return this.var == ((NonTerminalNode) o).getVar() && this.low.equals(((NonTerminalNode) o).getLow())
					&& this.high.equals(((NonTerminalNode) o).getHigh());
		}
	}
	
	@Override
	public int hashCode(){
		return 11 * var + 13 * low.hashCode() + 17 * high.hashCode();
	}
}
