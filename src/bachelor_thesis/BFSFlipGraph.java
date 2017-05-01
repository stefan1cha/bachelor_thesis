package bachelor_thesis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Queue;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class BFSFlipGraph extends SimpleGraph<LabeledTree, DefaultEdge>{

	//private HashSet<LabeledTree> vertexSet;


	private static final long serialVersionUID = 1L;

	public BFSFlipGraph(int n) {		
		super(DefaultEdge.class);
		
		int counter = 0;
		int limit = 100_000;
		
		Queue<LabeledTree> queue = new LinkedList<LabeledTree>();
		
		queue.add(LabeledTree.canonicalPath(n));
		while (!queue.isEmpty()) {
			LabeledTree explorer = queue.remove();
			this.addVertex(explorer);

			Set<LabeledTree> neighbors = explorer.getFlipTrees();

			Iterator<LabeledTree> iterator = neighbors.iterator();
			while (iterator.hasNext()) {
				LabeledTree current = iterator.next();
				if (!this.containsVertex(current))
					queue.add(current);
				this.addVertex(current);
				counter++;
				if (counter > limit) {
					//System.out.println(this.vertexSet().size());
					counter = 0;
				}
			}
		}
	}

	/*public HashSet<LabeledTree> getVertexSet() {
		return this.vertexSet;
	}*/

	@Override
	public String toString() {

		String result = "";

		//Iterator<LabeledTree> iterator = vertexSet.iterator();
		Iterator<LabeledTree> iterator = this.vertexSet().iterator();
		while (iterator.hasNext()) {
			result += iterator.next() + "\n";
		}
		
		return result;
	}

}
