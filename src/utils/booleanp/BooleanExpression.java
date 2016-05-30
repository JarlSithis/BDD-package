package utils.booleanp;

public interface BooleanExpression {

	public BooleanExpression simplify();
	public BooleanExpression substitute(int var, boolean value);
	public boolean equals(BooleanExpression ex);
	
}
