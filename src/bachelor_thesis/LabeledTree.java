package bachelor_thesis;

import java.util.ArrayList;
import java.util.Set;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class LabeledTree<V extends Number, E extends DefaultWeightedEdge> extends SimpleWeightedGraph<V, E> {

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
	public void addEdge(V v1, V v2, int label) {
		lastEdge = this.addEdge(v1, v2);
		this.setEdgeWeight(lastEdge, label);
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
	public int getEdgeWeight(V v1, V v2) {
		return (int) this.getEdgeWeight(this.getEdge(v1, v2));
	}

	/**
	 * 
	 */
	public PruferCode getPruferCode() {
		ArrayList<V> vertexList = new ArrayList<V>(this.vertexSet());
		int n = vertexList.size();
		
		int[] result = new int[n-2];
		
		// TODO lazy evaluation
		
		@SuppressWarnings("unchecked")
		LabeledTree<V,E> treeCopy = (LabeledTree<V, E>) this.clone();

		for (int i = 0; i < n - 2; ++i) {
			// does the for-each loop preserve order?
			for (V iterator: vertexList) {
				if (treeCopy.isLeaf(iterator)) {
					result[i] = treeCopy.getFirstNeighborLabel(iterator).intValue(); 
					treeCopy.removeVertex(iterator);
					break;
				}
			}
		}
		return new PruferCode(result);
	}
	
	/**
	 * Method description
	 * @param v
	 * @return
	 */
	private V getFirstNeighborLabel(V v) {
		Set<E> edges = this.edgesOf(v);
		E firstEdge = null;
		for (E iterator: edges) {
			firstEdge = iterator;
			break;
		}
		if (this.getEdgeTarget(firstEdge) != v)
			return this.getEdgeTarget(firstEdge);
		else
			return this.getEdgeSource(firstEdge);

	}
	
	private boolean isLeaf(V vertex) {
		return this.degreeOf(vertex) == 1;
	}
	

	/**
	 * This 'serialVersionUID' variable is here just to avoid a warning.
	 */
	private static final long serialVersionUID = 8571442369098258435L;

}
