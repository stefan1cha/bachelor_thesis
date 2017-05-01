package bachelor_thesis;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class DFSFlipGraph extends SimpleGraph<LabeledTree, DefaultEdge>{

	//private HashSet<LabeledTree> vertexSet;


	private static final long serialVersionUID = 1L;

	public DFSFlipGraph(int n) {		
		super(DefaultEdge.class);
		
		int counter = 0;
		int limit = 100_000;
		
		Stack<LabeledTree> stack = new Stack<LabeledTree>();

		stack.push(LabeledTree.canonicalPath(n));
		while (!stack.isEmpty()) {
			LabeledTree explorer = stack.pop();
			this.addVertex(explorer);

			Set<LabeledTree> neighbors = explorer.getFlipTrees();

			Iterator<LabeledTree> iterator = neighbors.iterator();
			while (iterator.hasNext()) {
				LabeledTree current = iterator.next();
				if (!this.containsVertex(current))
					stack.push(current);
				this.addVertex(current);
				counter++;
				if (counter > limit) {
					//System.out.println(this.vertexSet().size());
					counter = 0;
				}
			}
		}
	}

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
