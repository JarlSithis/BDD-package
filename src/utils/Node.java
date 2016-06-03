package utils;

import java.util.Hashtable;

/**
 * A simple implementation of a node, used in the representation of ROBDDs as graphs.
 * 
 * @author philip
 *
 */
public interface Node {

	/**
	 * 
	 * @return The number used to identify the variable associated with this
	 *         node. Values range from 1 to |number of variables| + 1, with the
	 *         highest number representing terminal, constant nodes.
	 */
	public int getVar();
	
	public String draw();
	
	@Override
	public int hashCode();

	public void cleanUp(Hashtable<Triple<Integer, Node, Node>, Node> lookupTable);

}
