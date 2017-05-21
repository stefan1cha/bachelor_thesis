package bachelor_thesis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class DiFlipGraph extends SimpleDirectedGraph<LabeledTree, DefaultEdge> {
	private static final long serialVersionUID = 1L;
	public static final int DECREASE_EDGE_SUM = 1;
	public static final int DECREASE_DIAMETER = 2;
	public static final int INCREASE_DEGREE_OF_VERTEX_ZERO = 3;
	public static final int FORWARD = 1;
	public static final int BACKWARD = -1;
	public static final int UNORIENTED = 0;

	public DiFlipGraph(int n, int orientationCriterion) {
		super(DefaultEdge.class);
		FlipGraph fg = new FlipGraph(n);

		Iterator<LabeledTree> vertexIterator = fg.vertexSet().iterator();
		while (vertexIterator.hasNext()) {
			this.addVertex(vertexIterator.next());
		}

		Iterator<DefaultEdge> edgeIterator = fg.edgeSet().iterator();
		while (edgeIterator.hasNext()) {
			DefaultEdge e = edgeIterator.next();
			LabeledTree u = fg.getEdgeSource(e);
			LabeledTree v = fg.getEdgeTarget(e);

			int orientation = getOrientation(u, v, orientationCriterion);

			if (orientation == FORWARD) {
				this.addEdge(u, v);
			} else {
				this.addEdge(v, u);
			}
		}
	}
	
	public Set<LabeledTree> getSinks() {
		HashSet<LabeledTree> result = new HashSet<LabeledTree>();
		Iterator<LabeledTree> iterator = this.vertexSet().iterator();
		while (iterator.hasNext()) {
			LabeledTree current = iterator.next();
			if (this.outDegreeOf(current) == 0)
				result.add(current);
		}
		return result;
	}
	
	public void printSinks() {
		System.out.println("\n\nSinks:");
		Iterator<LabeledTree> iterator = this.getSinks().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	private static int getOrientation(LabeledTree u, LabeledTree v, int orientationCriterion) {
		if (orientationCriterion == DECREASE_EDGE_SUM) {
			int uSum = 0;
			Iterator<DefaultWeightedEdge> uIterator = u.edgeSet().iterator();
			while (uIterator.hasNext()) {
				DefaultWeightedEdge e = uIterator.next();
				uSum += u.getEdgeSource(e) + u.getEdgeTarget(e);
			}
			int vSum = 0;
			Iterator<DefaultWeightedEdge> vIterator = v.edgeSet().iterator();
			while (vIterator.hasNext()) {
				DefaultWeightedEdge e = vIterator.next();
				vSum += v.getEdgeSource(e) + v.getEdgeTarget(e);
			}

			if (uSum > vSum)
				return FORWARD;
			else
				return BACKWARD;
		} else if (orientationCriterion == DECREASE_DIAMETER) {
			FloydWarshallShortestPaths<Integer, DefaultWeightedEdge> fwspu = new FloydWarshallShortestPaths<>(u);
			FloydWarshallShortestPaths<Integer, DefaultWeightedEdge> fwspv = new FloydWarshallShortestPaths<>(v);
			if (fwspu.getDiameter() > fwspv.getDiameter())
				return FORWARD;
			else
				return BACKWARD;
		} else if (orientationCriterion == INCREASE_DEGREE_OF_VERTEX_ZERO) {
			if (u.degreeOf(0) < v.degreeOf(0))
				return FORWARD;
			else
				return BACKWARD;
		}
		else {
			throw new IllegalArgumentException();
		}
	}

	public String toString() {
		String result = "";
		result += "Vertices:\n";
		LabeledTree[] flipNodes = this.vertexSet().toArray(new LabeledTree[0]);
		for (int i = 0; i < flipNodes.length; i++) {
			int sum = 0;
			Iterator<DefaultWeightedEdge> iterator = flipNodes[i].edgeSet().iterator();
			while (iterator.hasNext()) {
				DefaultWeightedEdge e = iterator.next();
				sum += flipNodes[i].getEdgeSource(e) + flipNodes[i].getEdgeTarget(e);
			}
			if (i < 10)
				result += "lt" + i + " : " + flipNodes[i] + " with edge sum = " + sum + "\n";
			else
				result += "lt" + i + ": " + flipNodes[i] + " with edge sum = " + sum + "\n";

		}
		result += "Edges:\n";
		Iterator<DefaultEdge> edgeIterator = this.edgeSet().iterator();
		while (edgeIterator.hasNext()) {
			DefaultEdge e = edgeIterator.next();
			LabeledTree u = this.getEdgeSource(e);
			LabeledTree v = this.getEdgeTarget(e);
			result += "(" + getIndex(u, flipNodes) + "," + getIndex(v, flipNodes) + "), ";
		}
		return result;
	}

	private static int getIndex(LabeledTree lt, LabeledTree[] array) {
		for (int i = 0; i < array.length; ++i) {
			if (array[i].equals(lt)) {
				return i;
			}
		}
		throw new RuntimeException();
	}
}
