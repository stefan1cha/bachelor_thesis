package bachelor_thesis;

public class Pair<A,B> {

	private A fst;
	private B snd;
	
	public Pair(A fst, B snd) {
		this.fst = fst;
		this.snd = snd;
	}
	
	public A fst() {
		return this.fst;
	}
	
	public B snd() {
		return this.snd;
	}
	
	
}
