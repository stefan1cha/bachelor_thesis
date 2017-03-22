package bachelor_thesis;

import org.jgrapht.graph.SimpleGraph;

import java.util.Iterator;
import java.util.Set;

import org.antlr.v4.runtime.misc.Pair;
import org.jgrapht.graph.DefaultEdge;

public class FlipGraph extends SimpleGraph<LabeledTree, DefaultEdge> {

	int n = -1;

	/**
	 * Construct a flip graph starting with a given labeled tree. The flip graph
	 * is constructed recursively (depth first search heuristic)
	 * 
	 * @param lt
	 *            From which tree the construction of the flip graph starts.
	 */
	public FlipGraph(LabeledTree lt) {
		super(DefaultEdge.class);
		if (!lt.isGraceful())
			throw new IllegalArgumentException();
		this.createFlipGraph(lt);
	}

	/**
	 * Construct the flip graph for the trees with 'n' vertices. The
	 * construction start from a (gracefully labeled) path.
	 * 
	 * @param n
	 *            The number of vertices of the trees.
	 */
	public FlipGraph(int n) {
		super(DefaultEdge.class);
		if (n < 0)
			throw new IllegalArgumentException();
		LabeledTree lt = new LabeledTree();
		int min = 0;
		int max = n;

		lt.addVertex(0);
		for (int i = 1; i < n; i++) {
			if (i % 2 == 0) {
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

	/**
	 * Construct a flip graph starting from LabeledTree 'lt'. The constructor
	 * cannot replace this method, since it recursive. In Java, constructors
	 * cannot be recursive.
	 * 
	 * @param lt
	 */
	private void createFlipGraph(LabeledTree lt) {
		this.addFlipNode(lt);
		Set<LabeledTree> neighbors = lt.getFlipTrees();
		this.addFlipNodeSet(neighbors);

		Iterator<LabeledTree> iterator = neighbors.iterator();
		while (iterator.hasNext()) {
			LabeledTree current = iterator.next();
			if (!this.equals(current) && !this.containsFlipEdge(lt, current)) {
				this.addFlipNode(current);
				this.addFlipEdgeAux(lt, current);
				this.createFlipGraph(current);
			}
		}
	}

	/**
	 * This method is a work-around the problem concerning the equals() method
	 * (see LabeledTree.java)
	 * 
	 * @param lt1
	 * @param lt2
	 */
	private void addFlipEdgeAux(LabeledTree lt1, LabeledTree lt2) {
		Iterator<LabeledTree> iterator = this.vertexSet().iterator();
		while (iterator.hasNext()) {
			LabeledTree current = iterator.next();
			if (lt1.equals(current))
				lt1 = current;
			if (lt2.equals(current))
				lt2 = current;
		}
		this.addEdge(lt1, lt2);
	}

	/**
	 * A work-around to the equals() method problem (see LabeledTree.java)
	 * @param e
	 * @param lt1
	 * @param lt2
	 * @return
	 */
	public void addFlipNode(LabeledTree lt) {
		if (!lt.isContainedIn(this.vertexSet()))
			this.addVertex(lt);
	}

	/**
	 * Another work-around to the equals() method problem (see LabeledTree.java)
	 * @param e
	 * @param lt1
	 * @param lt2
	 * @return
	 */
	public boolean containsFlipEdge(LabeledTree lt1, LabeledTree lt2) {
		Iterator<DefaultEdge> iterator = this.edgeSet().iterator();
		while (iterator.hasNext()) {
			if (this.edgeCorrepondsToVertices(iterator.next(), lt1, lt2))
				return true;
		}
		return false;
	}

	/**
	 * Another work-around to the equals() method problem (see LabeledTree.java)
	 * @param e
	 * @param lt1
	 * @param lt2
	 * @return
	 */
	public boolean edgeCorrepondsToVertices(DefaultEdge e, LabeledTree lt1, LabeledTree lt2) {
		return (lt1.equals(this.getEdgeSource(e)) && lt2.equals(this.getEdgeTarget(e)))
				|| (lt1.equals(this.getEdgeTarget(e)) && lt2.equals(this.getEdgeSource(e)));
	}

	/**
	 * Add all labeled trees that are in a given set to the flip graph
	 * @param vertexSet The set containing the trees to be added.
	 */
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
			result += "lt" + i + ": " + flipNodes[i] + "\n";
		}

		result += "\nEdges:\n";

		Iterator<DefaultEdge> iterator = this.edgeSet().iterator();
		while (iterator.hasNext()) {
			result += this.getEdgeIndicesFromArray(iterator.next(), flipNodes) + ", ";
		}
		return result.substring(0, result.length() - 2);
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
	 * This variable can be ignored. It is here just to get rid of some compiler
	 * warning.
	 */
	private static final long serialVersionUID = -3090899308347100128L;

}
