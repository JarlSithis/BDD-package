package bdd;

import java.util.Scanner;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

import utils.NotSatisfiableException;

public class IOHandler {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		if (args.length == 0) {
			System.out.println("Usage: Options:"
					+ "\n - Input a boolean expression in string form, followed by the number of variables as integer."
					+ "\n Only & and | are supported."
					+ "\n Variables have to be written as xi, with i being a number. Keep in mind that parentheses need to match, "
					+ "\n and every sub-part of the expression needs to be in parentheses."
					+ "\n For example, \"x1 & x2 | x3\"needs to be written as \"(x1 & x2) | x3\"."
					+ "\n Example for correct command: ./bdd-package.jar \"(x1 & !x3 & x2) | (!x1 & x2)\" 3" + "\n"
					+ "\n" + "\n Run the predefined test by writing \"Test\" as the only parameter."
					+ "\n Example: ./bdd-package.jar \"Test\""
					+ "\n To see the example for a BDD for the 4-queens problem, use \"Test_4queens\" as the only parameter.");
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("Test")) {
			System.out.println("In this test, we will look at two BDDs"
					+ "\n The first will be built from the expression ((x1 & x2 & !x3 & x4) | (!x1 & x3 & !x5) | (!x2 & x3))"
					+ "\n The second will be built from the expression ((x5 & x2 & x3) | (!x1 & !x2) | (!x3 & x4))"
					+ "\n To continue, press enter.");
			sc.next();
			BDD a = new BDD(5);
			BDD b = new BDD(5);
			Expression<String> aex = RuleSet
					.simplify(ExprParser.parse("((x1 & x2 & !x3 & x4) | (!x1 & x3 & !x5) | (!x2 & x3))"));
			Expression<String> bex = RuleSet.simplify(ExprParser.parse("((x5 & x2 & x3) | (!x1 & !x2) | (!x3 & x4))"));
			a.build(aex);
			b.build(bex);
			a.draw();
			b.draw();
			System.out.println("Press enter to continue.");
			sc.next();
			System.out.println("Now, we will apply the and operator to the two expressions. "
					+ "We expect that the resulting tree looks like the one built "
					+ "from the expression \n((x1 & x2 & !x3 & x4) | (!x1 & x3 & !x5)"
					+ "| (!x2 & x3)) & ((x5 & x2 & x3) | (!x1 & !x2) | (!x3 & x4))" + "\n Press enter to continue.");
			sc.next();
			BDD res = new BDD(5);
			res.apply("&", a, b);
			res.draw();
			BDD comp = new BDD(5);
			Expression<String> compex = RuleSet
					.simplify(ExprParser.parse("((x1 & x2 & !x3 & x4) | (!x1 & x3 & !x5) | (!x2 & x3)) &" + "((x5 &"
							+ "x2 & x3) | (!x1 & !x2) | (!x3 & x4))"));
			comp.build(compex);
			comp.draw();
		}
		if(args.length == 1 && args[0].equalsIgnoreCase("Test_4queens")){
			BDDTests.build4Queens();
		}
		if (args.length == 2) {
			Expression<String> ex = RuleSet.simplify(ExprParser.parse(args[0]));
			int n = Integer.parseInt(args[1]);
			BDD a = new BDD(n);
			a.build(ex);
			a.draw();
			try {
				System.out.println("A satisfying truth assignment for the expression is: " + a.anySat());
			} catch (NotSatisfiableException e) {
				System.out.println("There is no satisfying truth assignment for the expression.");
			}
		}
		sc.close();
	}

}
