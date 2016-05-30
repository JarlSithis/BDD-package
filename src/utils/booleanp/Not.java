package utils.booleanp;

public class Not implements BooleanExpression {

	private BooleanExpression operand;
	
	public BooleanExpression getOperand() {
		return operand;
	}

	public void setOperand(BooleanExpression operand) {
		this.operand = operand;
	}

	public Not(BooleanExpression op) {
		operand = op;
	}

	@Override
	public BooleanExpression simplify() {
		if(operand instanceof Not){
			return ((Not) operand).operand.simplify();
		}
		return operand.simplify();
	}

	@Override
	public BooleanExpression substitute(int var, boolean value) {
		return operand.substitute(var, value);
	}

	@Override
	public boolean equals(BooleanExpression ex) {
		if(ex instanceof Not && ((Not) ex).operand.equals(this.operand)){
			return true;
		}
		return false;
	}

}
