package utils;

import java.util.Hashtable;

/**
 * A node that represents the constant TRUE.
 * @author philip
 *
 */
public class TrueTerminalNode extends TerminalNode{

	public TrueTerminalNode(int varcount) {
		super(varcount);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof TrueTerminalNode;
	}
	
	@Override
	public int hashCode(){
		return 43;
	}

	@Override
	public void cleanUp(Hashtable<Triple<Integer, Node, Node>, Node> lookupTable) {
		return;		
	}

}
