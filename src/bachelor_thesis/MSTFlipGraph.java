package bachelor_thesis;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class MSTFlipGraph extends SimpleGraph<LabeledTree, DefaultEdge> {

	public MSTFlipGraph(Class<? extends DefaultEdge> edgeClass) {
		super(edgeClass);
		
	}

	/**
	 * Just to avoid a compilation warning
	 */
	private static final long serialVersionUID = 1L;

}
