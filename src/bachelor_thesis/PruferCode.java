package bachelor_thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PruferCode {

	/**
	 * Stores the Prufer code
	 */
	private ArrayList<Integer> code;

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

	@Override
	public int hashCode() {
		// TODO return prufer code as integer
		return 1;
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