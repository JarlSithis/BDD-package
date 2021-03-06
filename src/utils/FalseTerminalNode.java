package utils;

import java.util.Hashtable;

/**
 * A node that represents the constant FALSE.
 * 
 * @author philip
 *
 */
public class FalseTerminalNode extends TerminalNode {

	public FalseTerminalNode(int varcount) {
		super(varcount);
	}
	
	@Override
	public boolean equals(Object o){
		return o instanceof FalseTerminalNode;
	}

	@Override
	public int hashCode(){
		return 719;
	}

	@Override
	public void cleanUp(Hashtable<Triple<Integer, Node, Node>, Node> lookupTable) {
		return;
	}
}
