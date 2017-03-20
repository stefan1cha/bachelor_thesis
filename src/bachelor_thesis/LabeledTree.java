package bachelor_thesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.Iterator;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class LabeledTree<E extends DefaultWeightedEdge> extends SimpleWeightedGraph<Integer, E> {

	/**
	 * 
	 */
	PruferCode pfCode = null;

	/**
	 * Stores last edge that has been added. This variable eases the
	 * implementation of {@link #addEdge(Object, Object, int) addEdge}.
	 */
	private E lastEdge;

	public LabeledTree(Class<? extends E> edgeClass) {
		super(edgeClass);
	}

	@SuppressWarnings("unchecked")
	public LabeledTree(PruferCode pfc) {
		super((Class<? extends E>) DefaultWeightedEdge.class);

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

		//System.out.println("appears: " + Arrays.toString(appears));
		//System.out.println("sequence: " + sequence + "\n\n");

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
			//System.out.println(i + " appears: " + Arrays.toString(appears));
			//System.out.println(i + " sequence: " + sequence);
			//System.out.println(i + " taken:" + Arrays.toString(taken) + "\n\n");
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
		ArrayList<Integer> vertexList = new ArrayList<Integer>(this.vertexSet());
		Collections.sort(vertexList);
		int n = vertexList.size();

		int[] result = new int[n - 2];

		// TODO lazy evaluation

		@SuppressWarnings("unchecked")
		LabeledTree<E> treeCopy = (LabeledTree<E>) this.clone();

		for (int i = 0; i < n - 2; ++i) {
			//System.out.println(treeCopy);
			// does the for-each loop preserve order?
			for (Integer iterator : vertexList) {
				if (treeCopy.isLeaf(iterator)) {
					result[i] = treeCopy.getFirstNeighborLabel(iterator);
					treeCopy.removeVertex(iterator);
					break;
				}
			}
		}
		//System.out.println(treeCopy);
		return new PruferCode(result);
	}

	/**
	 * Method description
	 * 
	 * @param v
	 * @return
	 */
	private int getFirstNeighborLabel(int v) {
		Set<E> edges = this.edgesOf(v);
		E firstEdge = null;
		for (E iterator : edges) {
			firstEdge = iterator;
			break;
		}
		if (this.getEdgeTarget(firstEdge) != v)
			return this.getEdgeTarget(firstEdge);
		else
			return this.getEdgeSource(firstEdge);

	}

	private boolean isLeaf(int vertex) {
		return this.degreeOf(vertex) == 1;
	}

	/**
	 * This 'serialVersionUID' variable is here just to avoid a warning.
	 */
	private static final long serialVersionUID = 8571442369098258435L;

}