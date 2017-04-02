package bachelor_thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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

		/*
		 * Iterator<Integer> iterator = this.code.iterator(); hash = 0; int
		 * count = 0; int ten = 10; while (iterator.hasNext() && count++ < 10) {
		 * hash += ten * iterator.next(); ten *= 10; }
		 */

	}

	/**
	 * Constructs a PruferCode out of a list that stores the prufer code.
	 * 
	 * @param arg
	 *            List that stores the prufer code
	 */
	public PruferCode(ArrayList<Integer> arg) {
		this.code = arg;

		/*
		 * Iterator<Integer> iterator = this.code.iterator(); hash = 0; int
		 * count = 0; int ten = 10; while (iterator.hasNext() && count++ < 10) {
		 * hash += ten * iterator.next(); ten *= 10; }
		 */
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