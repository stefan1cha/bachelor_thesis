package bachelor_thesis;

import java.util.Arrays;

public class PruferCode {

	/**
	 * 
	 */
	private int[] code;

	public PruferCode(int[] arg) {
		//code = new int [arg.length];
		this.code = arg;
		// TODO: Remove this (it has been used for debugging)
		checkUniqueness(this.code);
	}

	/**
	 * Debugging: Checks that each label appears at most once.
	 */
	public static boolean checkUniqueness(int[] a) {
		int[] arr = a.clone();
		Arrays.sort(arr);
		for (int i = 0; i < arr.length - 1; ++i) {
			if (arr[i] == arr[i + 1])
				return false;
		}
		return true;
	}
	
	public int getLength() {
		return this.code.length;
	}

	@Override
	public String toString() {
		return Arrays.toString(this.code);
	}
}