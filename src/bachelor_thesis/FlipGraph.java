package bachelor_thesis;

import org.jgrapht.graph.SimpleGraph;

import java.util.Iterator;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;
import org.jgrapht.graph.DefaultEdge;

public class FlipGraph extends SimpleGraph<LabeledTree, DefaultEdge> {

	int n = -1;
	//LabeledTree lt = null;

	public FlipGraph(LabeledTree lt) {
		super(DefaultEdge.class);
		this.createFlipGraph(lt);
		// TODO check if 'lt' is graceful
		// TODO implement constructor

	}

	public FlipGraph(int n) {
		super(DefaultEdge.class);
		// TODO implement constructor
		// idea: start from a path because it is easy to find a graceful
		// labeling
		if (n < 0)
			throw new IllegalArgumentException();
		LabeledTree lt = new LabeledTree();
		int min = 0;
		int max = n;
		
		lt.addVertex(0);
		for (int i = 1; i<n; i++) {
			if (i % 2 == 0){
				min++;
				lt.addVertex(min);
				lt.addEdge(min, max);
			} else {
				max--;
				lt.addVertex(max);
				lt.addEdge(min, max);
			}
		}
		
		this.createFlipGraph(lt);
		
	}

	private void createFlipGraph(LabeledTree lt) {
		this.addFlipNode(lt);
		Set<LabeledTree> neighbors = lt.getFlipTrees();
		this.addFlipNodeSet(neighbors);
		Iterator<LabeledTree> iterator = neighbors.iterator();
		// boolean updateOccured = false;
		while (iterator.hasNext()) {
			LabeledTree current = iterator.next();
			if (!this.equals(current) && !this.containsFlipEdge(lt, current)) {
				this.addFlipNode(current);
				this.addEdge(lt, current);
				this.createFlipGraph(current);
				// updateOccured = true;
			}
		}
	}

	public void addFlipNode(LabeledTree lt) {
		if (!lt.isContainedIn(this.vertexSet()))
			this.addVertex(lt);
	}

	public boolean containsFlipEdge(LabeledTree lt1, LabeledTree lt2) {
		Iterator<DefaultEdge> iterator = this.edgeSet().iterator();
		while (iterator.hasNext()) {
			if (this.edgeCorrepondsToVertices(iterator.next(), lt1, lt2))
				return true;
		}
		return false;
	}

	public boolean edgeCorrepondsToVertices(DefaultEdge e, LabeledTree lt1, LabeledTree lt2) {
		return (lt1.equals(this.getEdgeSource(e)) && lt2.equals(this.getEdgeTarget(e)))
				|| (lt1.equals(this.getEdgeTarget(e)) && lt2.equals(this.getEdgeSource(e)));
	}

	public void addFlipNodeSet(Set<LabeledTree> vertexSet) {
		Iterator<LabeledTree> iterator = vertexSet.iterator();
		while (iterator.hasNext()) {
			this.addFlipNode(iterator.next()); // TODO change this method
		}
	}

	public String toString() {
		System.out.println("\n\n\nUsing Stefan's toString() method:\n");
		String result = "Vertices:\n";
		LabeledTree[] flipNodes = this.vertexSet().toArray(new LabeledTree[0]);
		for (int i = 0; i < flipNodes.length; i++) {
			// System.out.println("lt" + i + ": " + flipNodes[i]);
			result += "lt" + i + ": " + flipNodes[i] + "\n";
		}

		result += "\nEdges:\n";

		Iterator<DefaultEdge> iterator = this.edgeSet().iterator();
		while (iterator.hasNext()) {
			result += this.getEdgeIndicesFromArray(iterator.next(), flipNodes) + ", ";
		}

		return result;
	}

	private Pair<Integer, Integer> getEdgeIndicesFromArray(DefaultEdge e, LabeledTree[] ltArray) {
		int a = -1;
		int b = -1;
		for (int i = 0; i < ltArray.length; ++i) {
			if (this.getEdgeSource(e).equals(ltArray[i]) || this.getEdgeTarget(e).equals(ltArray[i])) {
				if (a == -1)
					a = i;
				else
					b = i;
			}
		}
		return new Pair<Integer, Integer>(a, b);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3090899308347100128L;

}
