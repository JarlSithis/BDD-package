package bdd;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

import utils.NotSatisfiableException;

public class BDDTests {

	@Test
	public void testApply() {
		BDD a = new BDD(5);
		BDD b = new BDD(5);
		BDD res = new BDD(5);
		BDD comparison = new BDD(5);

		Expression<String> parsA = RuleSet.simplify(ExprParser.parse("(x1 & !x2 & !x4 & !x5) | 1"));
		Expression<String> parsB = RuleSet.simplify(ExprParser.parse("(!x1 & x4 & !x5)"));
		a.build(parsA);
		b.build(parsB);
		res.apply("&", a, b);

		Expression<String> parsComp = RuleSet
				.simplify(ExprParser.parse("((x1 & !x2 & !x4 & !x5) | 1) & (!x1 & x4 & !x5)"));
		comparison.build(parsComp);

		assertEquals(comparison, res);
	}

	@Test
	public void testRestrict() {
		BDD a = new BDD(6);
		BDD b = new BDD(6);
		Expression<String> parsA = RuleSet
				.simplify(ExprParser.parse("(x1 & !x2 & !x4 & !x5) | (!x3 & x4 & !x1 & x5) | (x2 & !x4 & x3 & x1)"));
		a.build(parsA);
		Expression<String> parsB = RuleSet.simplify(
				ExprParser.parse("(x1 & !x2 & !x4 & !x5) | (!(true) & x4 & !x1 & x5) | (x2 & !x4 & true & x1)"));
		b.build(parsB);

		a.restrict(3, true);
		assertEquals(a, b);
		// a.draw();
		// b.draw();
	}

	@Test
	public void testSatCount() {
		BDD a = new BDD(5);
		Expression<String> parsA = RuleSet
				.simplify(ExprParser.parse("(x1 & !x2 & !x4 & !x5) | (!x3 & x4 & !x1 & x5) | (x2 & !x4 & x3 & x1)"));
		a.build(parsA);
		
		//Computed by looking at the truth table:
		assertEquals(6, a.satCount(), 0.000001);
	}

	@Test
	public void testAnySat() throws NotSatisfiableException {
		BDD a = new BDD(5);
		Expression<String> parsA = RuleSet
				.simplify(ExprParser.parse("(x1 & !x2 & !x4 & !x5) | (!x3 & x4 & !x1 & x5) | (x2 & !x4 & x3 & x1)"));
		a.build(parsA);
		
		//List of fulfilling assignments for the expression, computed using the truth table.
		List<String> validSats = Arrays.asList("x1 -> 0, x3 -> 0, x4 -> 1, x5 -> 1, ", "x1 -> 1, x2 -> 0, x4 -> 0, x5 -> 0, ", "x1 -> 0, x2 -> 1, x3 -> 1, x4 -> 0, x5 -> 0, " );
		assertTrue(validSats.contains(a.anySat()));
	}

//	public static void nQueens(int n) {
//		String expr = "";
//		// x_i,j will be represented as x_(i + j*n+1)
//		for (int i = 1; i <= n; i++) {
//			for (int j = 1; j <= n; j++) {
//				expr += ("(!(x" + (i + (n + 1) * j) + ") | (");
//				for (int l = 1; l < n; l++) {
//					if (l == j) {
//						continue;
//					}
//					expr += "!x" + (i + (n + 1) * l) + " & ";
//				}
//				expr += "!x" + (i + (n + 1) * n) + "))";
//
//				expr += " & ((!x" + (i + (n + 1) * j) + ")|(";
//				for (int k = 1; k < n; k++) {
//					if (k == i) {
//						continue;
//					}
//					expr += "!x" + (k + (n + 1) * j) + " & ";
//				}
//				expr += "!x" + (n + (n + 1) * j) + "))";
//
//				expr += " & ((!x" + (i + (n + 1) * j) + ")|(";
//				for (int k = 1; k < n && 1 <= j + i - k & j + 1 - k <= n; k++) {
//
//				}
//			}
//		}
//
//		Expression<String> parsQu = RuleSet.simplify(ExprParser.parse(expr));
//		BDD qu = new BDD((n + 1 * n * n));
//		qu.build(parsQu);
//		qu.draw();
//		try {
//			System.out.println(qu.anySat(qu.getRoot()));
//		} catch (NotSatisfiableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
