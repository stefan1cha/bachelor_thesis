package bachelor_thesis;

import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.antlr.v4.runtime.misc.Pair;

public class FlipGraph extends SimpleGraph<LabeledTree, DefaultEdge> {

	// Constructor
	public FlipGraph() {
		super(DefaultEdge.class);
	}

	// Constructor
	public FlipGraph(LabeledTree lt) {
		super(DefaultEdge.class);
		if (!lt.isGraceful())
			throw new IllegalArgumentException();
		this.createFlipGraph(lt);
	}
	
	// Constructor
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

	// Method	
	public void createFlipGraph(LabeledTree lt) {

		//int counter = 0;
		//int limit = 24000;
		Stack<LabeledTree> stack = new Stack<LabeledTree>();
		HashSet<LabeledTree> visited = new HashSet<LabeledTree>();

		stack.push(lt);
		while (!stack.isEmpty()) {
			LabeledTree explorer = stack.pop();
			this.addVertex(explorer);
			visited.add(explorer);

			Set<LabeledTree> neighbors = explorer.getFlipTrees();

			Iterator<LabeledTree> iterator = neighbors.iterator();
			while (iterator.hasNext()) {
				LabeledTree current = iterator.next();
				if (!visited.contains(current))
					stack.push(current);
				this.addVertex(current);
				if (!this.containsEdge(explorer, current)) {
					this.addEdge(explorer, current);
				}
			}

			/*if (counter++ > limit) {
				int vsize = this.vertexSet().size();
				if (vsize > 900000)
					limit = 10000;
				else if (vsize > 1088650)
					limit = 2000;
				System.out.println("#vertices:" + vsize + "    #edges:" + this.edgeSet().size() + "\n");
				neighbors = null;
				counter = 0;
				System.gc();
			}
			*/

		}

	}


	public String toString() {
		String result = "Vertices:\n";
		LabeledTree[] flipNodes = this.vertexSet().toArray(new LabeledTree[0]);
		for (int i = 0; i < flipNodes.length; i++) {
			if (i < 10)
				result += "lt" + i + " : " + flipNodes[i] + "    is path: " + flipNodes[i].isPath() + "\n";
			else
				result += "lt" + i + ": " + flipNodes[i] + "    is path: " + flipNodes[i].isPath() + "\n";

		}

		result += "\nEdges:\n";

		int count = 0;
		Iterator<DefaultEdge> iterator = this.edgeSet().iterator();
		while (iterator.hasNext()) {
			result += this.getEdgeIndicesFromArray(iterator.next(), flipNodes) + ", ";
			if (count++ > 10) {
				count = 0;
				result += "\n";
			}
		}
		int k = 2;
		if (result.charAt(result.length() - 1) == '\n')
			k++;
		return result.substring(0, result.length() - k);
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

	public ArrayList<Pair<DefaultWeightedEdge, DefaultWeightedEdge>> getFlipSequence(LabeledTree source,
			LabeledTree sink) {
		ArrayList<Pair<DefaultWeightedEdge, DefaultWeightedEdge>> res = new ArrayList<>();
		if (!this.containsVertex(source) || !this.containsVertex(sink))
			return null;
		FloydWarshallShortestPaths<LabeledTree, DefaultEdge> fwsp = new FloydWarshallShortestPaths<>(this);
		GraphPath<LabeledTree, DefaultEdge> walk = fwsp.getPath(source, sink);
		List<LabeledTree> treeSeq = walk.getVertexList();
		Iterator<LabeledTree> iterator = treeSeq.iterator();
		LabeledTree prev = iterator.next();
		LabeledTree current;
		while (iterator.hasNext()) {
			current = iterator.next();
			res.add(prev.getEdgeFlip(current));
			prev = current;
		}
		return res;
	}

	public static Set<PruferCode> treesToPruferCodes(Set<LabeledTree> trees) {
		Iterator<LabeledTree> iterator = trees.iterator();
		Set<PruferCode> result = new HashSet<PruferCode>();
		while (iterator.hasNext()) {
			result.add(iterator.next().getPruferCode());
		}
		return result;

	}

	public int getDiameter() {
		FloydWarshallShortestPaths<LabeledTree, DefaultEdge> fw = new FloydWarshallShortestPaths<>(this);
		return (int) fw.getDiameter();
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------

	public void createFlipGraphRec(LabeledTree lt) {
		this.addVertex(lt);
		Set<LabeledTree> neighbors = lt.getFlipTrees();
		this.addVertexSet(neighbors);

		Iterator<LabeledTree> iterator = neighbors.iterator();
		while (iterator.hasNext()) {
			LabeledTree current = iterator.next();
			if (!this.equals(current) && !this.containsFlipEdge(lt, current)) {
				this.addVertex(current);
				this.addFlipEdgeAux(lt, current);
				this.createFlipGraphRec(current);
			}
		}
	}

	public void createFlipGraphAux(LabeledTree lt) {
		// int counter = 0;
		// int limit = 24000;
		Stack<LabeledTree> stack = new Stack<LabeledTree>();
		HashSet<LabeledTree> visited = new HashSet<LabeledTree>();

		stack.push(lt);
		while (!stack.isEmpty()) {
			LabeledTree explorer = stack.pop();
			this.addVertex(explorer);
			visited.add(explorer);

			Set<LabeledTree> neighbors = explorer.getFlipTrees();

			Iterator<LabeledTree> iterator = neighbors.iterator();
			while (iterator.hasNext()) {
				LabeledTree current = iterator.next();
				if (!visited.contains(current))
					stack.push(current);
				if (!this.containsVertex(current))
					this.addVertex(current);
				if (!this.containsEdge(explorer, current)) {
					this.addEdge(explorer, current);
				}
			}


		}

	}
	
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

	public void addVertexSet(Set<LabeledTree> vertexSet) {
		Iterator<LabeledTree> iterator = vertexSet.iterator();
		while (iterator.hasNext()) {
			this.addVertex(iterator.next());
		}
	}

	public int getDiameterRandomized(int trials) {
		DijkstraShortestPath<LabeledTree, DefaultEdge> dsp = new DijkstraShortestPath<>(this);
		LabeledTree source;
		LabeledTree target;
		int max = 0;
		double winningProbability = (trials * 1.0) / this.vertexSet().size();

		HashSet<TwoTrees> pairs = this.getDistinctPairs();
		Iterator<TwoTrees> iterator = pairs.iterator();
		while (iterator.hasNext()) {
			if (throwDie(winningProbability)) {
				TwoTrees current = iterator.next();
				source = current.getFirst();
				target = current.getSecond();
				int pathLength = (int) dsp.getPathWeight(source, target);
				if (pathLength > max) {
					max = pathLength;
					System.out.println("source:" + source.getPruferCode());
					System.out.println("target: " + target.getPruferCode());
					System.out.println("path length: " + pathLength);
					System.out.println("\n");
				}
			} else
				iterator.next();
		}

		return max;
	}

	private boolean throwDie(double winningProbability) {
		double value = Math.random();
		if (value < winningProbability)
			return true;
		else
			return false;
	}

	private HashSet<TwoTrees> getDistinctPairs() {

		HashSet<TwoTrees> result = new HashSet<TwoTrees>();

		Iterator<LabeledTree> outerIterator = this.vertexSet().iterator();

		while (outerIterator.hasNext()) {
			Iterator<LabeledTree> innerIterator = this.vertexSet().iterator();
			LabeledTree current = outerIterator.next();

			while (innerIterator.hasNext()) {
				TwoTrees newSet = new TwoTrees(current, innerIterator.next());
				if (newSet.distinctTrees())
					result.add(newSet);
			}
		}

		return result;
	}

	public FlipGraph getJustWithPathsAndPseudoPaths() {
		FlipGraph copy = (FlipGraph) this.clone();

		Iterator<LabeledTree> iterator = this.vertexSet().iterator();
		while (iterator.hasNext()) {
			LabeledTree current = iterator.next();
			if (!current.isPath() && !current.isPseudoPath())
				copy.removeVertex(current);
		}
		return copy;
	}

	/**
	 * This variable can be ignored. It is here just to get rid of some compiler
	 * warning.
	 */
	private static final long serialVersionUID = -3090899308347100128L;

}
