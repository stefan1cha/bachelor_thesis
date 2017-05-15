package bachelor_thesis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.ClosestFirstIterator;

public class DFSFlipGraph extends SimpleGraph<LabeledTree, DefaultEdge> {

	private static final long serialVersionUID = 1L;
	public static int maxDeg = 0;
	public static int minDeg = Integer.MAX_VALUE;
	public static int sum = 0;

	public DFSFlipGraph(int n) {
		super(DefaultEdge.class);

		Stack<LabeledTree> stack = new Stack<LabeledTree>();

		stack.push(LabeledTree.canonicalPath(n));
		while (!stack.isEmpty()) {
			LabeledTree explorer = stack.pop();
			this.addVertex(explorer);

			Set<LabeledTree> neighbors = explorer.getFlipTrees();
			int deg = neighbors.size();
			if (deg > maxDeg)
				maxDeg = deg;
			if (deg < minDeg)
				minDeg = deg;
			sum += deg;
			
				

			Iterator<LabeledTree> iterator = neighbors.iterator();
			while (iterator.hasNext()) {
				LabeledTree current = iterator.next();
				if (!this.containsVertex(current))
					stack.push(current);
				this.addVertex(current);
			}
		}
		System.out.println(maxDeg);
		System.out.println(minDeg);
		System.out.println((1.0 * sum)/this.vertexSet().size());
	}

	public DFSFlipGraph(LabeledTree lt) {
		super(DefaultEdge.class);
		int debugCounter = 0;

		Stack<LabeledTree> stack = new Stack<LabeledTree>();

		stack.push(lt);
		while (!stack.isEmpty()) {
			LabeledTree explorer = stack.pop();
			this.addVertex(explorer);

			Set<LabeledTree> neighbors = explorer.getFlipTrees();

			Iterator<LabeledTree> iterator = neighbors.iterator();
			while (iterator.hasNext()) {
				LabeledTree current = iterator.next();
				if (!this.containsVertex(current))
					stack.push(current);
				boolean newVertex = this.addVertex(current);

				if (newVertex) {
					this.addEdge(explorer, current);
					current.depth = explorer.depth + 1;
				}

			}
			
			if (debugCounter < 1000) {
				debugCounter++;
			} else {
				debugCounter = 0;
				System.out.println(this.vertexSet().size());
			}
			
		}
	}

	@Override
	public String toString() {
		String result = "";
		result += "Vertices:\n";
		LabeledTree[] flipNodes = this.vertexSet().toArray(new LabeledTree[0]);
		for (int i = 0; i < flipNodes.length; i++) {
			if (i < 10)
				result += "lt" + i + " : " + flipNodes[i] + " with depth " + flipNodes[i].depth + "\n";
			else
				result += "lt" + i + ": " + flipNodes[i] + " with depth " + flipNodes[i].depth + "\n";

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

	public int getDiameter() {
				
		BreadthFirstIterator<LabeledTree, DefaultEdge> iterator = new BreadthFirstIterator<>(this);

		LabeledTree curr = null;
		while (iterator.hasNext()) {
			curr = iterator.next();
			curr.depth = -1;
		}

		LabeledTree lt1 = curr;
		
		int depth = 0;
		Queue<LabeledTree> queue = new LinkedList<LabeledTree>();
		queue.add(lt1);
		queue.add(null);
		while (!queue.isEmpty()) {
			LabeledTree explorer = queue.remove();
			if (explorer != null) {
				// add neighbors
				ClosestFirstIterator<LabeledTree, DefaultEdge> cfi = new ClosestFirstIterator<LabeledTree, DefaultEdge>(
						this, explorer, 1);
				while (cfi.hasNext()) {
					LabeledTree local = cfi.next();
					if (local.depth == -1) {
						queue.add(local);
						local.depth = depth;
					}
				}
			} else {
				// new level: increase depth
				if (!queue.isEmpty()) {
					queue.add(null);
					depth++;
				}
			}
		}
		return depth;
	}

	

}
