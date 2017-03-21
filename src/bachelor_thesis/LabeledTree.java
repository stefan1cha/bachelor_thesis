package bachelor_thesis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.GraphTests;

public class LabeledTree extends SimpleWeightedGraph<Integer, DefaultWeightedEdge> {

	/**
	 * 
	 */
	PruferCode pfCode = null;

	/**
	 * Stores last edge that has been added. This variable eases the
	 * implementation of {@link #addEdge(Object, Object, int) addEdge}.
	 */
	private DefaultWeightedEdge lastEdge;

	public LabeledTree(PruferCode pfc, boolean labeled) {

		// TODO for lazy evaluation do this: this.pfCode = pfc.
		// But this will be deferred because the getPruferCode() method needs to
		// be thoroughly tested.

		super(DefaultWeightedEdge.class);

		int n = pfc.getLength() + 2; // number of vertices
		int[] appears = new int[n + 1]; // appears[i] = k means that label i
										// appears k times in the pfc
		int[] taken = new int[n + 1]; // which numbers are available for u (see
										// below)
		ArrayList<Integer> sequence = pfc.toList();

		// initialize 'appears'
		Iterator<Integer> iterator = sequence.iterator();
		while (iterator.hasNext()) {
			appears[iterator.next()]++;
		}

		// System.out.println("appears: " + Arrays.toString(appears));
		// System.out.println("sequence: " + sequence + "\n\n");

		int u;
		int v;
		for (int i = 0; i < n - 2; i++) {
			// get first label
			v = sequence.get(0);
			// remove first label
			sequence.remove(0);
			// search for smallest label u in 'taken' s.t. u is not in
			// 'sequence
			// Then update 'sequence' and 'appears'
			for (u = 0; u < appears.length; ++u) {
				if (appears[u] == 0 && taken[u] == 0) {
					sequence.add(u);
					appears[u]++;
					appears[v]--;
					taken[u]++;
					break;
				}
			}
			// System.out.println(i + " appears: " + Arrays.toString(appears));
			// System.out.println(i + " sequence: " + sequence);
			// System.out.println(i + " taken:" + Arrays.toString(taken) +
			// "\n\n");
			this.addEdgeAndVertices(u, v);
		}

		u = v = -1;
		for (int i = 0; i < taken.length; ++i) {
			if (taken[i] == 0) {
				if (u == -1)
					u = i;
				else if (v == -1)
					v = i;
				else
					break;
			}
		}
		this.addEdgeAndVertices(u, v);

		if (labeled)
			this.labelEdges();

	}

	/**
	 * 
	 */
	// Precondition: tree is labeled
	// TODO public (a collection of tree) getFlips(edge e)

	public LabeledTree() {
		super(DefaultWeightedEdge.class);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Adds an new weighted edge between the vertices v1 and v2 in the graph
	 * 
	 * @param v1
	 *            the first vertex
	 * @param v2
	 *            the second vertex
	 * @param weight
	 *            the label of the edge to be added
	 * @return void etc ?
	 */
	public void addEdge(int v1, int v2, int label) {
		lastEdge = this.addEdge(v1, v2);
		this.setEdgeWeight(lastEdge, label);
	}

	/**
	 * Adds the edge and ,if necessary, it adds the missing vertices before
	 */
	public void addEdgeAndVertices(int v1, int v2) {
		if (!this.containsVertex(v1))
			this.addVertex(v1);
		if (!this.containsVertex(v2))
			this.addVertex(v2);
		this.addEdge(v1, v2);

	}

	/**
	 * 
	 */
	public void labelEdges() {
		for (DefaultWeightedEdge e : this.edgeSet()) {
			int v = this.getEdgeSource(e);
			int u = this.getEdgeTarget(e);
			this.setEdgeWeight(e, Math.abs(u - v));

		}
	}

	/**
	 * 
	 */
	public boolean isGraceful() {
		int[] vertexLabels = new int[this.edgeSet().size() + 1];
		Set<Integer> vertices = this.vertexSet();
		if (this.vertexSet().size() != this.edgeSet().size() + 1) {
			//System.out.println(this.vertexSet().size() + " , " + this.edgeSet().size());
			throw new RuntimeException(); // debug
			// return false;
		}
		for (Integer v : vertices) {
			vertexLabels[v]++;
		}
		for (int i = 0; i < vertexLabels.length; i++) {
			if (vertexLabels[i] != 1) {
				System.out.println("nope"); // debug
				return false;
			}
		}

		int[] edgeLabels = new int[this.edgeSet().size() + 1];
		Set<DefaultWeightedEdge> edges = this.edgeSet();
		for (DefaultWeightedEdge e : edges) {
			int max = Math.max(this.getEdgeSource(e), this.getEdgeTarget(e));
			int min = Math.min(this.getEdgeSource(e), this.getEdgeTarget(e));
			edgeLabels[max - min]++;
		}

		for (int i = 1; i < edgeLabels.length; i++) {
			if (edgeLabels[i] != 1)
				return false;
		}
		return true;
	}

	/**
	 * method description
	 * 
	 * @param v1
	 *            the first vertex
	 * @param v2
	 *            the second vertex
	 * @return ? etc ?
	 */
	public int getEdgeWeight(int v1, int v2) {
		return (int) this.getEdgeWeight(this.getEdge(v1, v2));
	}

	/**
	 * 
	 */
	public PruferCode getPruferCode() {
		if (this.pfCode != null)
			return this.pfCode;
		ArrayList<Integer> vertexList = new ArrayList<Integer>(this.vertexSet());
		Collections.sort(vertexList);
		int n = vertexList.size();

		int[] result = new int[n - 2];

		// TODO lazy evaluation

		LabeledTree treeCopy = (LabeledTree) this.clone();

		for (int i = 0; i < n - 2; ++i) {
			// System.out.println(treeCopy);
			// does the for-each loop preserve order?
			for (Integer iterator : vertexList) {
				if (treeCopy.isLeaf(iterator)) {
					result[i] = treeCopy.getFirstNeighborLabel(iterator);
					treeCopy.removeVertex(iterator);
					break;
				}
			}
		}
		// System.out.println(treeCopy);
		return new PruferCode(result);
	}

	/**
	 * Method description
	 * 
	 * @param v
	 * @return
	 */
	private int getFirstNeighborLabel(int v) {
		Set<DefaultWeightedEdge> edges = this.edgesOf(v);
		DefaultWeightedEdge firstEdge = null;
		for (DefaultWeightedEdge iterator : edges) {
			firstEdge = iterator;
			break;
		}
		if (this.getEdgeTarget(firstEdge) != v)
			return this.getEdgeTarget(firstEdge);
		else
			return this.getEdgeSource(firstEdge);

	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public Set<LabeledTree> getFlipTrees() {
		// TODO implement method

		Set<LabeledTree> flipTrees = new HashSet<LabeledTree>();
		for (DefaultWeightedEdge e : this.edgeSet()) {
			// create copy of tree
			LabeledTree treeCopy = (LabeledTree) this.clone();
			// try to attach the edge 'e' somewhere else
			// choose the vertex sets such that we avoid attaching the edge back
			// to its original place
			Set<Integer> vertexSet1 = this.vertexSet();
			// why does this yield an error:
			// vertexSet1.remove(this.getEdgeSource(e));
			Set<Integer> vertexSet2 = this.vertexSet();
			// why does this yield an error:
			// vertexSet2.remove(this.getEdgeSource(e));

			// remove edge 'e'
			treeCopy.removeEdge(e);

			for (Integer i : vertexSet1) {
				for (Integer j : vertexSet2) {
					if (i != j && !treeCopy.containsEdge(i, j)) {
						treeCopy.addEdge(i, j);
						// System.out.println(treeCopy);
						if (treeCopy.isGraceful() && GraphTests.isTree(treeCopy)) {
							//System.out.println("-> " + i + ", " + j + ": " + treeCopy);
							// flipTrees.add((LabeledTree) treeCopy.clone()); //
							// clone() is VERY important here
							addTree(flipTrees, (LabeledTree) treeCopy.clone());// clone()
																				// is
																				// VERY
																				// important
																				// here
							//System.out.println("flipTrees :" + flipTrees);
						}
						// remove edge 'e' for next for-loop
						treeCopy.removeEdge(i, j);
					}
				}
			}
		}
		removeTree(flipTrees, (LabeledTree) this.clone());
		return flipTrees;
	}

	private boolean isLeaf(int vertex) {
		return this.degreeOf(vertex) == 1;
	}

	public boolean equals(Object other) {

		if (other.getClass() != this.getClass()) {
			throw new RuntimeException("Debugging");
			// return false;
		} else {
			LabeledTree lt = (LabeledTree) other;
			for (Integer v : this.vertexSet())
				if (!lt.vertexSet().contains(v))
					return false;
			for (Integer v : lt.vertexSet())
				if (!this.vertexSet().contains(v))
					return false;
			for (DefaultWeightedEdge e : this.edgeSet()) {
				int u = this.getEdgeSource(e);
				int v = this.getEdgeTarget(e);
				if (!lt.containsEdge(u, v))
					return false;
			}
			for (DefaultWeightedEdge e : lt.edgeSet()) {
				int u = lt.getEdgeSource(e);
				int v = lt.getEdgeTarget(e);
				if (!this.containsEdge(u, v))
					return false;
			}
		}
		return true;
	}

	public boolean isContainedIn(Set<LabeledTree> set) {
		Iterator<LabeledTree> iterator = set.iterator();
		while (iterator.hasNext()) {
			if (this.equals(iterator.next()))
				return true;
		}
		return false;
	}

	public static boolean addTree(Set<LabeledTree> set, LabeledTree lt) {
		Iterator<LabeledTree> iterator = set.iterator();
		while (iterator.hasNext()) {
			if (lt.equals(iterator.next()))
				return false;
		}
		set.add(lt);
		return true;
	}

	public static boolean removeTree(Set<LabeledTree> set, LabeledTree lt) {
		for (LabeledTree iterator : set) {
			if (iterator.equals(lt)) {
				set.remove(iterator);
				return true;
			}
		}
		return false;
	}

	/**
	 * This 'serialVersionUID' variable is here just to avoid a warning.
	 */
	private static final long serialVersionUID = 8571442369098258435L;

}