package bdd;

import java.util.Hashtable;

/**
 * @author Offtermatt
 */
public class BDD {

	/**
	 * Implementation of a simple class to represent a triple, used in the BDD
	 * // for the triples that denotes (var, low, high) of a {@Link Node} n
	 * 
	 * @author Offtermatt
	 *
	 * @param <A>
	 * @param <B>
	 * @param <C>
	 */
	private class Triple<A, B, C> {
		private A first;
		private B second;
		private C third;

		public Triple(A a, B b, C c) {
			first = a;
			second = b;
			third = c;
		}

		public A getFirst() {
			return first;
		}

		public void setFirst(A first) {
			this.first = first;
		}

		public B getSecond() {
			return second;
		}

		public void setSecond(B second) {
			this.second = second;
		}

		public C getThird() {
			return third;
		}

		public void setThird(C third) {
			this.third = third;
		}
	}

	/**
	 * A lookup table that maps triples of (var, low, high) to {@Link Node}s n
	 */
	private Hashtable<Triple, Node> H;

	/**
	 * The "real" bdd.
	 */
	private Node root;

	/**
	 * Used to denote the number of variables the model for the given boolean
	 * expression has
	 */
	private int n;

	/**
	 * initialises the BDD to work over a given set of variables
	 * 
	 * @param variableCount
	 *            The number of variables that will be used, denoted with the
	 *            integers 1 to n.
	 */
	public BDD(int variableCount) {
		n = variableCount;
	}
}
