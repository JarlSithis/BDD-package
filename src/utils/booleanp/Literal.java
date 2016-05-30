package utils.booleanp;

public class Literal implements BooleanExpression {

	private int var;
	
	public Literal(int var) {
		this.setVar(var);
	}

	@Override
	public BooleanExpression simplify() {
		return this;
	}

	@Override
	public BooleanExpression substitute(int var, boolean value) {
		if(var == this.var){
			return value ? new TrueConstant() : new Not(new TrueConstant());
		}
		return this;
	}

	@Override
	public boolean equals(BooleanExpression ex) {
		if(ex instanceof Literal && ((Literal) ex).getVar() == var){
			return true;
		}
		return false;
	}

	public int getVar() {
		return var;
	}

	public void setVar(int var) {
		this.var = var;
	}

}
