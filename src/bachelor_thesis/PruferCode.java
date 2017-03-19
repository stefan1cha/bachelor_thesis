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

	@Override
	public String toString() {
		/*String res = "";
		for (int i = 0; i < code.length; ++i) {
			res+= Integer.toString(code[i]);
		}
		return res;
		*/
		return Arrays.toString(this.code);
		//return code.toString();
	}
}
