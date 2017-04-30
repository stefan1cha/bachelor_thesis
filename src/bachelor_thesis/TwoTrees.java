package bachelor_thesis;

public class TwoTrees {

	private LabeledTree lt1;
	private LabeledTree lt2;

	public TwoTrees(LabeledTree lt1, LabeledTree lt2) {
		this.lt1 = lt1;
		this.lt2 = lt2;
	}

	public LabeledTree getFirst() {
		return this.lt1;
	}

	public LabeledTree getSecond() {
		return this.lt2;
	}

	public boolean distinctTrees() {
		return !this.lt1.equals(this.lt2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;

		TwoTrees pair = (TwoTrees) obj;

		if (this.lt1.equals(pair.lt1) && this.lt2.equals(pair.lt2)) {
			return true;
		}

		if (this.lt1.equals(pair.lt2) && this.lt2.equals(pair.lt1))
			return true;

		return false;

	}

	@Override
	public int hashCode() {
		return lt1.hashCode() + lt2.hashCode();
	}

	@Override
	public String toString() {
		return "{" + lt1.getPruferCode().toString() + "," + lt2.getPruferCode().toString() + "}";
	}
}
