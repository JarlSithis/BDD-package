package utils;

public abstract class TerminalNode implements Node{

	public int n;
	
	public int getVar(){
		return n+1;
	}
	
	public TerminalNode(int varcount){
		n = varcount;
	}
}
