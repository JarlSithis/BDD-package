package bdd;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

public class BDDTests {

	@Test
	public void testBuild() {
		fail("Not yet implemented");
	}

	@Test
	public void testApply(){
		BDD a = new BDD(5);
		BDD b = new BDD(5);
		BDD res = new BDD(5);
		BDD comparison = new BDD(5);
		
		Expression<String> parsA = RuleSet.simplify(ExprParser.parse("(x1 & !x2 & !x4 & !x5) | x3"));
		Expression<String> parsB = RuleSet.simplify(ExprParser.parse("(!x1 & x4 & !x5)"));
		a.build(parsA);
		b.build(parsB);
		res.apply("&", a, b);
		
		Expression<String> parsComp = RuleSet.simplify(ExprParser.parse("((x1 & !x2 & !x4 & !x5) | x3) & (!x1 & x4 & !x5)"));
		comparison.build(parsComp);

		assertEquals(comparison, res);
	}

	@Test
	public void testRestrict() {
		BDD a = new BDD(5);
		Expression<String> parsA = RuleSet.simplify(ExprParser.parse("(x1 & !x2 & !x4 & !x5) | x3"));
		a.build(parsA);
		a.restrict(3, true);
		a.draw();
		
	}

	@Test
	public void testSatCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testAnySat() {
		fail("Not yet implemented");
	}

}
