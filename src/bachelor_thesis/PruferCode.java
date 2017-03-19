package bachelor_thesis;

import java.util.ArrayList;
import java.util.Arrays;

public class PruferCode {

	/**
	 * 
	 */
	ArrayList<Integer> code;

	public PruferCode(int[] arg) {
		this.code = new ArrayList<Integer>();
		for (int i = 0; i < arg.length; ++i) {
			this.code.add(arg[i]);
		}
		
	}

	public PruferCode(ArrayList<Integer> arg) {
		this.code = arg;
	}
	
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