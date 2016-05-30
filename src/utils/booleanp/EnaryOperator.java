package utils.booleanp;

import java.util.TreeSet;

public abstract class EnaryOperator implements BooleanExpression {

	TreeSet<BooleanExpression> operands;

	public EnaryOperator(TreeSet<BooleanExpression> operands) {
		this.operands = operands;
	}

}
