package bachelor_thesis;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PruferCode {

	/**
	 * Stores the Prufer code
	 */
	private ArrayList<Integer> code;
	private int hash = -1;

	/**
	 * Constructs a PruferCode out of an array that stores the prufer code.
	 * 
	 * @param arg
	 *            Array that stores the prufer code
	 */
	public PruferCode(int[] arg) {
		this.code = new ArrayList<Integer>();
		for (int i = 0; i < arg.length; ++i) {
			this.code.add(arg[i]);
		}

	}

	/**
	 * Constructs a PruferCode out of a list that stores the prufer code.
	 * 
	 * @param arg
	 *            List that stores the prufer code
	 */
	public PruferCode(ArrayList<Integer> arg) {
		this.code = arg;
	}

	/**
	 * Transorm the PruferCode in an ArrayList. It is more or less get-method.
	 */
	public ArrayList<Integer> toList() {
		return this.code;
	}

	public int getLength() {
		return this.code.size();
	}

	@Override
	public String toString() {
		return Arrays.toString(this.code.toArray());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass())
			return false;

		PruferCode other = (PruferCode) obj;

		Iterator<Integer> iterator = this.code.iterator();
		while (iterator.hasNext())
			if (!other.has(iterator.next()))
				return false;

		iterator = other.code.iterator();
		while (iterator.hasNext())
			if (!this.has(iterator.next()))
				return false;

		return true;
	}

	public static Set<List<Integer>> generateAllPruferCodes(int n) {
		HashSet<Integer> numbers = new HashSet<Integer>();
		for (int i = 0; i < n; ++i)
			numbers.add(i);

		ArrayList<HashSet<Integer>> sets = new ArrayList<HashSet<Integer>>();
		for (int i = 0; i < n - 2; ++i)
			sets.add(numbers);

		return Sets.cartesianProduct(sets);
	}

	public static boolean isGraceful(List<Integer> code, int n) {
		int[] labels = new int[n + 1];

		ArrayList<Integer> sequence = new ArrayList<Integer>(code);

		// System.out.println("\n\n\ninput:" + sequence);

		int[] list = new int[n + 1];
		for (int i = 0; i < n + 1; i++)
			list[i] = i;

		Iterator<Integer> iterator = sequence.iterator();
		while (iterator.hasNext()) {
			int current = iterator.next();
			// find smallest element in the list that is not in the sequence
			for (int i = 0; i < n; i++)
				if (list[i] > -1 && !sequence.contains(list[i])) {
					// System.out.println("\n");
					// System.out.println("current = " + current + ", list[" + i
					// + "] = " + list[i]);
					// System.out.println(Arrays.toString(list));
					// System.out.println(sequence);
					++labels[Math.abs(current - i)];
					iterator.remove();
					list[i] = -1;
					break;
				}
		}

		// System.out.println("\n\n");
		// System.out.println(Arrays.toString(list));
		// System.out.println(sequence);
		int a = -1;
		int b = -1;

		for (int i = 0; i < list.length; ++i) {
			if (a != -1)
				b = i;
			else if (list[i] != -1)
				a = i;
		}

		// System.out.println("a = " + a + ", b = " + b);

		++labels[Math.abs(a - b)];

		for (int i = 1; i < labels.length; ++i)
			if (labels[i] != 1)
				return false;
		return true;
	}

	@Override
	public int hashCode() {
		if (hash == -1) {
			hash = 0;
			Iterator<Integer> iterator = code.iterator();
			while (iterator.hasNext()) {
				hash += iterator.next();
				hash *= 10;
			}
		}
		return this.hash; // % 100;
	}

	private boolean has(int x) {
		Iterator<Integer> iterator = this.code.iterator();
		while (iterator.hasNext()) {
			if (iterator.next() == x)
				return true;
		}
		return false;
	}
}
