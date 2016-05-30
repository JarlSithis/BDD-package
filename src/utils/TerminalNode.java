package utils;

/**
 * A node with no successors. var is n+1, where n is the number of variables in the tree.
 * @author philip
 *
 */
public abstract class TerminalNode implements Node{

	public int var;
	
	public int getVar(){
		return var;
	}
	
	public TerminalNode(int varcount){
		var = varcount+1;
	}
	
	public String draw(){
		return this.getClass().getSimpleName();
	}
}
