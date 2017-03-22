package bachelor_thesis;

import java.util.ArrayList;
import java.util.Arrays;

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
}