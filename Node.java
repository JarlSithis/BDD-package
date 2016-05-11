package bdd;

public class Node {
	
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

}
