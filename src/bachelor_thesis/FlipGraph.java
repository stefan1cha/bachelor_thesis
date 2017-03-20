package bachelor_thesis;

import org.jgrapht.graph.SimpleGraph;

import javax.management.RuntimeErrorException;

import org.jgrapht.graph.DefaultEdge;

public class FlipGraph extends SimpleGraph<LabeledTree, DefaultEdge> {

	public FlipGraph(LabeledTree lt) {
		super(DefaultEdge.class);
		
	}
	
	public FlipGraph(int n) {
		super(DefaultEdge.class);
		// TODO implement constructor
		throw new RuntimeErrorException(null, "Stefan says: This constructor has not been implemented yet.");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3090899308347100128L;

}
