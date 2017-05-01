package bachelor_thesis;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.traverse.DepthFirstIterator;
import org.antlr.v4.runtime.misc.Pair;
import org.jgrapht.GraphTests;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class LabeledTree extends SimpleWeightedGraph<Integer, DefaultWeightedEdge> {

	private int id = -1;

	/**
	 * Stores the prufer code (if it has been already given or computed before).
	 */
	private PruferCode pfCode = null;

	/**
	 * Stores last edge that has been added. This variable eases the
	 * implementation of {@link #addEdge(Object, Object, int) addEdge}. It may
	 * be removed in the future if possible.
	 */
	private DefaultWeightedEdge lastEdge;

	/**
	 * Constructs a (possibly) labeled tree out of a prufer code. Note (22nd of
	 * March): the 'labeled' boolean variable can be ignored for the moment as
	 * it is not relevant. It might come in handy later.
	 * 
	 * @param pfc
	 *            The prufer code that represents the tree to be constructed.
	 * @param labeled
	 *            If set to true, the edges will be labeled.
	 */
	public LabeledTree(PruferCode pfc) {

		super(DefaultWeightedEdge.class);

		this.pfCode = pfc;
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

	public void setId(int arg) {
		this.id = arg;
	}

	public int getId() {
		return this.id;
	}

	public LabeledTree(List<Integer> originalSequence) {

		super(DefaultWeightedEdge.class);
		List<Integer> sequence = new ArrayList<Integer>(originalSequence);
		int n = sequence.size() + 2; // number of vertices
		int[] appears = new int[n + 1]; // appears[i] = k means that label i
										// appears k times in the pfc
		int[] taken = new int[n + 1]; // which numbers are available for u (see
										// below)

		// initialize 'appears'
		Iterator<Integer> iterator = sequence.iterator();
		while (iterator.hasNext()) {
			appears[iterator.next()]++;
		}
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
	 * A basic constructor.
	 */
	public LabeledTree() {
		super(DefaultWeightedEdge.class);
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
	 */
	public void addEdge(int v1, int v2, int label) {
		lastEdge = this.addEdge(v1, v2);
		this.setEdgeWeight(lastEdge, label);
	}

	/**
	 * Adds the edge and ,if necessary, it adds the missing vertices (before
	 * adding the edge)
	 */
	private void addEdgeAndVertices(int v1, int v2) {
		if (!this.containsVertex(v1))
			this.addVertex(v1);
		if (!this.containsVertex(v2))
			this.addVertex(v2);
		this.addEdge(v1, v2);

		this.pfCode = null;

	}

	/**
	 * Labels the edges of the graph with the absolute difference of the labels
	 * of their end-vertices.
	 */
	@SuppressWarnings("unused")
	private void labelEdges() {
		for (DefaultWeightedEdge e : this.edgeSet()) {
			int v = this.getEdgeSource(e);
			int u = this.getEdgeTarget(e);
			this.setEdgeWeight(e, Math.abs(u - v));

		}
	}

	/**
	 * Tests wether the graph is graceful or not.
	 * 
	 * @return Return 'true' if the graph is graceful.
	 */
	public boolean isGraceful() {
		int[] vertexLabels = new int[this.edgeSet().size() + 1];
		Set<Integer> vertices = this.vertexSet();
		if (this.vertexSet().size() != this.edgeSet().size() + 1) {
			throw new RuntimeException(); // debug
			// return false;
		}
		for (Integer v : vertices) {
			vertexLabels[v]++;
		}
		for (int i = 0; i < vertexLabels.length; i++) {
			if (vertexLabels[i] != 1) {
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
	 * Returns the weight og edge {u,v}
	 * 
	 * @param v1
	 *            the first vertex
	 * @param v2
	 *            the second vertex
	 * @return Weight of the edge {u,v}
	 */
	public int getEdgeWeight(int v1, int v2) {
		return (int) this.getEdgeWeight(this.getEdge(v1, v2));
	}

	/**
	 * Returns the prufer code.
	 * 
	 * @return the prufer code
	 */
	public PruferCode getPruferCode() {
		if (this.pfCode != null)
			return this.pfCode;
		ArrayList<Integer> vertexList = new ArrayList<Integer>(this.vertexSet());
		Collections.sort(vertexList);
		int n = vertexList.size();

		if (n < 2)
			return null;

		int[] result = new int[n - 2];

		LabeledTree treeCopy = (LabeledTree) this.clone();

		for (int i = 0; i < n - 2; ++i) {
			for (Integer iterator : vertexList) {
				if (treeCopy.isLeaf(iterator)) {
					result[i] = treeCopy.getFirstNeighborLabel(iterator);
					treeCopy.removeVertex(iterator);
					break;
				}
			}
		}
		this.pfCode = new PruferCode(result);
		vertexList = null;
		treeCopy = null;
		return this.pfCode;
	}

	/**
	 * Returns the label of the first neighbor. By 'first neighbor' it is simply
	 * meant the first neighbor that is seen by the algorithm. This method is
	 * used for computing the prufer code of the labeled tree. When removing a
	 * leaf, its (sole) neighbor's label needs to be known.
	 * 
	 * @param The
	 *            node for which to get the neighbor's label
	 * @return The neighbors label.
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
	 * Returns the set of flip trees of the tree upon which this method is
	 * called upon. A tree t1 is a flip tree of tree t0 if t1 can be obtained by
	 * flipping an edge in t0.
	 * 
	 * @return The set of flip trees.
	 */
	public Set<LabeledTree> getFlipTreesDeprecated() {
		Set<LabeledTree> flipTrees = new HashSet<LabeledTree>();
		for (DefaultWeightedEdge e : this.edgeSet()) {
			// create copy of tree
			LabeledTree treeCopy = (LabeledTree) this.clone();
			// try to attach the edge 'e' somewhere else
			// choose the vertex sets such that we avoid attaching the edge back
			// to its original place
			Set<Integer> vertexSet1 = this.vertexSet();
			// why does this yield an error?
			// Probably because the equals() method has not been properly
			// overidden.
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
						if (treeCopy.isGraceful() && GraphTests.isTree(treeCopy)) {

							addTree(flipTrees, (LabeledTree) treeCopy.clone());// clone()
																				// is
																				// VERY
																				// important
																				// here
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

	public Set<LabeledTree> getFlipTrees() {

		Set<LabeledTree> result = new HashSet<LabeledTree>();

		// number of vertices
		int n = this.vertexSet().size();

		// store for each edge its source, target, and weight
		HashSet<Pair<Integer, Integer>> edges = new HashSet<Pair<Integer, Integer>>();
		Iterator<DefaultWeightedEdge> edgeExplorer = this.edgeSet().iterator();
		while (edgeExplorer.hasNext()) {
			DefaultWeightedEdge e = edgeExplorer.next();
			edges.add(new Pair<Integer, Integer>(this.getEdgeSource(e), this.getEdgeTarget(e)));
		}

		Iterator<Pair<Integer, Integer>> edgeIterator = edges.iterator();
		while (edgeIterator.hasNext()) {

			// Pick next edge
			Pair<Integer, Integer> e = edgeIterator.next();
			int edgeSource = e.a;
			int edgeTarget = e.b;
			int edgeLabel = Math.abs(edgeSource - edgeTarget);

			// Remove the edge
			this.removeEdge(edgeSource, edgeTarget);

			// Get the vertices of one of the connected components
			Set<Integer> connectedComponentVertices = new HashSet<Integer>();
			DepthFirstIterator<Integer, DefaultWeightedEdge> iterator = new DepthFirstIterator<Integer, DefaultWeightedEdge>(
					this, edgeSource);
			iterator.setCrossComponentTraversal(false);
			while (iterator.hasNext())
				connectedComponentVertices.add(iterator.next());

			// Try re-attaching the edge somewhere else
			iterator = new DepthFirstIterator<Integer, DefaultWeightedEdge>(this, edgeSource);
			while (iterator.hasNext()) {
				int currentVertex = iterator.next();

				if ((0 <= currentVertex + edgeLabel) && (currentVertex + edgeLabel < n)
						&& ((currentVertex + edgeLabel != edgeTarget) || currentVertex != edgeSource)
						&& !connectedComponentVertices.contains(currentVertex + edgeLabel)) {
					LabeledTree newTree = (LabeledTree) this.clone();
					newTree.addEdge(currentVertex, currentVertex + edgeLabel);
					result.add(newTree);
				}

				if ((0 <= currentVertex - edgeLabel) && (currentVertex - edgeLabel < n)
						&& ((currentVertex - edgeLabel != edgeTarget) || (currentVertex != edgeSource))
						&& !connectedComponentVertices.contains(currentVertex - edgeLabel)) {
					LabeledTree newTree = (LabeledTree) this.clone();
					newTree.addEdge(currentVertex, currentVertex - edgeLabel);
					result.add(newTree);
				}
			}
			// add edge back
			this.addEdge(edgeSource, edgeTarget);
		}
		result.remove(this);
		return result;
	}	

	private boolean isLeaf(int vertex) {
		return this.degreeOf(vertex) == 1;
	}
	

	/**
	 * Two graphs are equal if they have equal vertex set and equal edge set
	 * PROBLEM: It seems that this method does not override the equals() method
	 * from the Object class. Overriding that method is not a 'must' since I
	 * managed to work my way around this problem, but it will make
	 * implementation of LabeledTree and FlipGraph very clear (and more elegant
	 * than it currently is).
	 * 
	 * @return Returns true if 'this' and 'other' are equal graphs AND are of
	 *         the same type.
	 */
	/*public boolean equals(Object other) {
		if (other.getClass() != this.getClass()) {
			throw new RuntimeException("Debugging");
		} else {
			LabeledTree lt = (LabeledTree) other;
			for (Integer v : this.vertexSet())
				if (!lt.vertexSet().contains(v)) {
					return false;
				}
			for (Integer v : lt.vertexSet())
				if (!this.vertexSet().contains(v)) {
					return false;
				}
			for (DefaultWeightedEdge e : this.edgeSet()) {
				int u = this.getEdgeSource(e);
				int v = this.getEdgeTarget(e);
				if (!lt.containsEdge(u, v)) {
					return false;
				}
			}
			for (DefaultWeightedEdge e : lt.edgeSet()) {
				int u = lt.getEdgeSource(e);
				int v = lt.getEdgeTarget(e);
				if (!this.containsEdge(u, v)) {
					return false;
				}
			}
		}
		return true;
	}*/
	
	public boolean equals(Object other) {
		LabeledTree otherTree = (LabeledTree) other;
		return this.getPruferCode().equals(otherTree.getPruferCode());
	}

	public int hashCode() {
		return this.getPruferCode().hashCode();
	}

	/**
	 * Return true if 'this' is contained in the set 'set'
	 * 
	 * @param set
	 *            The set that is queried.
	 * @return Return true if it is in the set.
	 */
	public boolean isContainedIn(Set<LabeledTree> set) {
		Iterator<LabeledTree> iterator = set.iterator();
		while (iterator.hasNext()) {
			if (this.equals(iterator.next()))
				return true;
		}
		return false;
	}

	private static boolean addTree(Set<LabeledTree> set, LabeledTree lt) {
		Iterator<LabeledTree> iterator = set.iterator();
		while (iterator.hasNext()) {
			if (lt.equals(iterator.next()))
				return false;
		}
		set.add(lt);
		return true;
	}

	private static boolean removeTree(Set<LabeledTree> set, LabeledTree lt) {
		for (LabeledTree iterator : set) {
			if (iterator.equals(lt)) {
				set.remove(iterator);
				return true;
			}
		}
		return false;
	}

	public boolean isPath() {
		Iterator<Integer> iterator = this.vertexSet().iterator();
		int leaves = 0;
		while (iterator.hasNext()) {
			if (this.degreeOf(iterator.next()) == 1)
				leaves++;
		}
		return leaves == 2;
	}

	public boolean isCanonicalPath() {
		if (!this.isPath())
			return false;
		Iterator<Integer> iterator = this.vertexSet().iterator();
		while (iterator.hasNext()) {
			int current = iterator.next();
			if (this.degreeOf(current) == 1 && (current == 0 || current == this.vertexSet().size() - 1))
				return true;
		}
		return false;
	}

	public boolean isPseudoPath() {
		Iterator<Integer> iterator = this.vertexSet().iterator();
		int leaves = 0;
		while (iterator.hasNext()) {
			if (this.degreeOf(iterator.next()) == 1)
				leaves++;
		}
		return leaves == 3;
	}

	public Pair<DefaultWeightedEdge, DefaultWeightedEdge> getEdgeFlip(LabeledTree lt) {
		Set<DefaultWeightedEdge> set1 = new HashSet<DefaultWeightedEdge>(this.edgeSet());
		Set<DefaultWeightedEdge> set2 = new HashSet<DefaultWeightedEdge>(lt.edgeSet());
		DefaultWeightedEdge e1 = null;
		DefaultWeightedEdge e2 = null;

		Iterator<DefaultWeightedEdge> iterator = set1.iterator();
		while (iterator.hasNext()) {
			e1 = iterator.next();
			if (!this.edgeIsContainedIn(e1, set2))
				break;
		}

		iterator = set2.iterator();
		while (iterator.hasNext()) {
			e2 = iterator.next();
			if (!this.edgeIsContainedIn(e2, set1))
				break;
		}

		Pair<DefaultWeightedEdge, DefaultWeightedEdge> result = new Pair<DefaultWeightedEdge, DefaultWeightedEdge>(e1,
				e2);

		return result;
	}

	private boolean edgeIsContainedIn(DefaultWeightedEdge e, Set<DefaultWeightedEdge> set) {
		Iterator<DefaultWeightedEdge> iterator = set.iterator();
		int v1 = this.getEdgeSource(e);
		int v2 = this.getEdgeTarget(e);
		while (iterator.hasNext()) {
			DefaultWeightedEdge eCurr = iterator.next();
			int u1 = this.getEdgeSource(eCurr);
			int u2 = this.getEdgeTarget(eCurr);
			if ((v1 == u1 && v2 == u2) || (v1 == u2 && v2 == u1))
				return true;
		}

		return false;

	}

	@Override
	public String toString() {
		Integer[] vertexArr = this.vertexSet().toArray(new Integer[this.vertexSet().size()]);
		Arrays.sort(vertexArr);
		DefaultWeightedEdge[] edgeArr = this.edgeSet().toArray(new DefaultWeightedEdge[this.edgeSet().size()]);
		final LabeledTree lt = (LabeledTree) this.clone();
		Arrays.sort(edgeArr, new Comparator<DefaultWeightedEdge>() {
			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {

				if ((Math.min(lt.getEdgeSource(o1), lt.getEdgeTarget(o1)) == Math.min(lt.getEdgeSource(o2),
						lt.getEdgeTarget(o2)))
						&& (Math.max(lt.getEdgeSource(o1), lt.getEdgeTarget(o1)) == Math.max(lt.getEdgeSource(o2),
								lt.getEdgeTarget(o2))))
					return 0;

				if (Math.min(lt.getEdgeSource(o1), lt.getEdgeTarget(o1)) < Math.min(lt.getEdgeSource(o2),
						lt.getEdgeTarget(o2)))
					return -1;

				else if (Math.min(lt.getEdgeSource(o1), lt.getEdgeTarget(o1)) > Math.min(lt.getEdgeSource(o2),
						lt.getEdgeTarget(o2)))
					return 1;

				else {
					if (Math.max(lt.getEdgeSource(o1), lt.getEdgeTarget(o1)) < Math.max(lt.getEdgeSource(o2),
							lt.getEdgeTarget(o2)))
						return -1;
					else
						return 1;
				}
			}

			@Override
			public boolean equals(Object obj) {
				return super.equals(obj);
			}
		});
		String result = "([";

		for (int i = 0; i < vertexArr.length; ++i)
			result += vertexArr[i] + ", ";
		result = result.substring(0, result.length() - 2) + "], [";

		for (int i = 0; i < edgeArr.length; ++i)
			result += "(" + Math.min(lt.getEdgeSource(edgeArr[i]), lt.getEdgeTarget(edgeArr[i])) + ","
					+ Math.max(lt.getEdgeSource(edgeArr[i]), lt.getEdgeTarget(edgeArr[i])) + "),";
		result = result.substring(0, result.length() - 1) + "])";
		return result; // + " with prufer code: " + this.getPruferCode();
	}

	public Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> howToGet(LabeledTree lt) {
		Set<DefaultWeightedEdge> set1 = new HashSet<DefaultWeightedEdge>(this.edgeSet());
		Set<DefaultWeightedEdge> set2 = new HashSet<DefaultWeightedEdge>(lt.edgeSet());

		DefaultWeightedEdge e1 = null;
		Iterator<DefaultWeightedEdge> iterator = set1.iterator();
		while (iterator.hasNext()) {
			DefaultWeightedEdge current = iterator.next();
			if (!edgeIsContainedIn(current, set2)) {
				e1 = current;
				break;
			}
		}

		DefaultWeightedEdge e2 = null;
		iterator = set2.iterator();
		while (iterator.hasNext()) {
			DefaultWeightedEdge current = iterator.next();
			if (!edgeIsContainedIn(current, set1)) {
				e2 = current;
				break;
			}
		}

		int min1 = Math.min(this.getEdgeSource(e1), this.getEdgeTarget(e1));
		int min2 = Math.min(this.getEdgeSource(e2), this.getEdgeTarget(e2));
		int max1 = Math.max(this.getEdgeSource(e1), this.getEdgeTarget(e1));
		int max2 = Math.max(this.getEdgeSource(e2), this.getEdgeTarget(e2));

		Pair<Integer, Integer> pair1 = new Pair<Integer, Integer>(min1, max1);
		Pair<Integer, Integer> pair2 = new Pair<Integer, Integer>(min2, max2);

		return new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(pair1, pair2);

	}

	public static LabeledTree canonicalPath(int n) {
		int min = 0;
		int max = n;
		LabeledTree canonicalPath = new LabeledTree();
		canonicalPath.addVertex(0);
		for (int iter = 1; iter < n; iter++) {
			if (iter % 2 == 0) {
				min++;
				canonicalPath.addVertex(min);
				canonicalPath.addEdge(min, max);
			} else {
				max--;
				canonicalPath.addVertex(max);
				canonicalPath.addEdge(min, max);
			}
		}
		return canonicalPath;
	}

	public static LabeledTree canonicalStar(int n) {
		int[] code = new int[n - 2];
		for (int i = 0; i < n - 2; ++i) {
			code[i] = 0;
		}
		return new LabeledTree(new PruferCode(code));
	}

	@Override
	public boolean addVertex(Integer v) {
		pfCode = null;
		return super.addVertex(v);
	}

	@Override
	public DefaultWeightedEdge addEdge(Integer arg0, Integer arg1) {
		pfCode = null;
		return super.addEdge(arg0, arg1);
	}

	@Override
	public boolean addEdge(Integer sourceVertex, Integer targetVertex, DefaultWeightedEdge e) {
		pfCode = null;
		return super.addEdge(sourceVertex, targetVertex, e);
	}

	@Override
	public boolean removeAllEdges(Collection<? extends DefaultWeightedEdge> arg0) {
		pfCode = null;
		return super.removeAllEdges(arg0);
	}

	@Override
	public Set<DefaultWeightedEdge> removeAllEdges(Integer sourceVertex, Integer targetVertex) {
		pfCode = null;
		return super.removeAllEdges(sourceVertex, targetVertex);
	}

	@Override
	public boolean removeAllVertices(Collection<? extends Integer> arg0) {
		pfCode = null;
		return super.removeAllVertices(arg0);
	}

	@Override
	public boolean removeEdge(DefaultWeightedEdge e) {
		pfCode = null;
		return super.removeEdge(e);
	}

	@Override
	public DefaultWeightedEdge removeEdge(Integer sourceVertex, Integer targetVertex) {
		pfCode = null;
		return super.removeEdge(sourceVertex, targetVertex);
	}

	@Override
	public boolean removeVertex(Integer arg0) {
		pfCode = null;
		return super.removeVertex(arg0);
	}

	@Override
	protected boolean removeAllEdges(DefaultWeightedEdge[] arg0) {
		pfCode = null;
		return super.removeAllEdges(arg0);
	}

	public boolean hasAllDegsLessEqThan(int deg) {
		Iterator<Integer> iterator = this.vertexSet().iterator();
		while (iterator.hasNext()) {
			int current = iterator.next();
			if (this.degreeOf(current) > deg)
				return false;
		}
		
		return true;
	}
	
	/**
	 * This 'serialVersionUID' variable is here just to avoid a warning.
	 */
	private static final long serialVersionUID = 8571442369098258435L;

}