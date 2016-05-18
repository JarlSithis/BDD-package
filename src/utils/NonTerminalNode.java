package utils;

public class NonTerminalNode implements Node{

	/**
	 * The index of the variable. 
	 * -1 denotes that the node is the terminal node 0
	 * -2 denotes that the node is the terminal node 1
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
	
	
}
