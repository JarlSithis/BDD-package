package utils.booleanp;

public class TrueConstant implements BooleanExpression {


	public TrueConstant(){
		
	}

	@Override
	public BooleanExpression simplify() {
		return this;
	}

	@Override
	public BooleanExpression substitute(int var, boolean value) {
		return this;
	}

	@Override
	public boolean equals(BooleanExpression ex) {
		if(ex instanceof TrueConstant){
			return true;
		}
		return false;
	}

}
