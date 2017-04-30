package bachelor_thesis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

public class DFSFlipGraph {

	private HashSet<LabeledTree> vertexSet;

	public DFSFlipGraph(int n) {
		vertexSet = new HashSet<LabeledTree>();
		
		Stack<LabeledTree> stack = new Stack<LabeledTree>();
		stack.add(LabeledTree.canonicalPath(n));

		while (!stack.isEmpty()) {
			LabeledTree explorer = stack.pop();
			vertexSet.add(explorer);

			Iterator<LabeledTree> iterator = explorer.getFlipTrees().iterator();
			while (iterator.hasNext()) {
				LabeledTree current = iterator.next();
				if (stack.search(current) == -1 && !vertexSet.contains(current)) {
					stack.push(current);
				}
				vertexSet.add(explorer);
			}
		}
	}

	public HashSet<LabeledTree> getVertexSet() {
		return this.vertexSet;
	}

	@Override
	public String toString() {

		String result = "";

		Iterator<LabeledTree> iterator = vertexSet.iterator();
		while (iterator.hasNext()) {
			result += iterator.next() + "\n";
		}
		
		return result;
	}

}
