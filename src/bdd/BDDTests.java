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

		// Computed by looking at the truth table:
		assertEquals(6, a.satCount(), 0.000001);
	}

	@Test
	public void testAnySat() throws NotSatisfiableException {
		BDD a = new BDD(5);
		Expression<String> parsA = RuleSet
				.simplify(ExprParser.parse("(x1 & !x2 & !x4 & !x5) | (!x3 & x4 & !x1 & x5) | (x2 & !x4 & x3 & x1)"));
		a.build(parsA);

		// List of fulfilling assignments for the expression, computed using the
		// truth table.
		List<String> validSats = Arrays.asList("x1 -> 0, x3 -> 0, x4 -> 1, x5 -> 1, ",
				"x1 -> 1, x2 -> 0, x4 -> 0, x5 -> 0, ", "x1 -> 0, x2 -> 1, x3 -> 1, x4 -> 0, x5 -> 0, ");
		assertTrue(validSats.contains(a.anySat()));
	}

	public static void build4Queens() {
		List<String> clauses = Arrays.asList("13 14 15 16 0", "9 10 11 12 0", "5 6 7 8 0", "1 2 3 4 0", "-12 -16 0",
				"-8 -16 0", "-4 -16 0", "-11 -15 0", "-7 -15 0", "-3 -15 0", "-10 -14 0", "-6 -14 0", "-2 -14 0",
				"-9 -13 0", "-5 -13 0", "-1 -13 0", "-8 -12 0", "-4 -12 0", "-7 -11 0", "-3 -11 0", "-6 -10 0",
				"-2 -10 0", "-5 -9 0", "-1 -9 0", "-4 -8 0", "-3 -7 0", "-2 -6 0", "-1 -5 0", "-15 -16 0", "-14 -16 0",
				"-13 -16 0", "-11 -12 0", "-10 -12 0", "-9 -12 0", "-7 -8 0", "-6 -8 0", "-5 -8 0", "-3 -4 0",
				"-2 -4 0", "-1 -4 0", "-14 -15 0", "-13 -15 0", "-10 -11 0", "-9 -11 0", "-6 -7 0", "-5 -7 0",
				"-2 -3 0", "-1 -3 0", "-13 -14 0", "-9 -10 0", "-5 -6 0", "-1 -2 0", "-11 -16 0", "-6 -16 0",
				"-1 -16 0", "-12 -15 0", "-10 -15 0", "-5 -15 0", "-11 -14 0", "-9 -14 0", "-8 -14 0", "-10 -13 0",
				"-7 -13 0", "-4 -13 0", "-7 -12 0", "-2 -12 0", "-8 -11 0", "-6 -11 0", "-1 -11 0", "-7 -10 0",
				"-5 -10 0", "-4 -10 0", "-6 -9 0", "-3 -9 0", "-3 -8 0", "-4 -7 0", "-2 -7 0", "-3 -6 0", "-1 -6 0",
				"-2 -5 0");
		String expr = "";
		for (String s : clauses) {
			expr += "(";
			String[] vars = s.split(" ");
			for (String x : vars) {
				if (!(x.equals("0"))) {
					if (x.startsWith("-")) {
						String k = x.substring(1);
						expr += " !x" + k + " |";
					} else {
						expr += " x" + x + " |";
					}
				}
			}
			if (expr.endsWith("|")) {
				expr = expr.substring(0, expr.length() - 2);
			}
			expr += ") &";
		}
		if (expr.endsWith("&")) {
			expr = expr.substring(0, expr.length() - 2);
		}
		Expression<String> fourQu = RuleSet.simplify(ExprParser.parse(expr));
		BDD fq = new BDD(16);
		fq.build(fourQu);
		fq.draw();
		System.out.println("Number of solutions: " + fq.satCount());
		System.out.println();
		try {
			System.out.println(
					"xi -> 1 means that a queen is placed on field i. "
					+ "\n For example, the first field in the second row is field 5, and x5 -> 1 would mean that a queen is placed on field 5 in the solution."
					+ "\n One solution is: "
					+ fq.anySat());
		} catch (NotSatisfiableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
