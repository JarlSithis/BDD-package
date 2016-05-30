package utils.booleanp;

import java.util.TreeSet;

public class And extends EnaryOperator {

	public And(TreeSet<BooleanExpression> operands) {
		super(operands);
	}

	@Override
	public BooleanExpression simplify() {
		for(BooleanExpression expr: operands){
			expr = expr.simplify();
		}
		for(BooleanExpression expr1 : operands){
			for(BooleanExpression expr2 : operands){
				if(new Not(expr1).equals(expr2)){
					return new Not(new TrueConstant());
				}
			}
		}
		for(BooleanExpression expr : operands){
			if(expr.equals(new Not(new TrueConstant()))){
				return new Not(new TrueConstant());
			}
			if(expr.equals(new TrueConstant())){
				operands.remove(expr);
			}
		}
		return this;
	}

	@Override
	public BooleanExpression substitute(int var, boolean value) {
		for(BooleanExpression expr : operands){
			expr = expr.substitute(var, value);
		}
		simplify();
		return this;
	}

	@Override
	public boolean equals(BooleanExpression ex) {
		if(ex instanceof And){
			if(((And) ex).operands.equals(operands))
		}
	}

}
