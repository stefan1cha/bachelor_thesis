package bachelor_thesis;

import org.jgrapht.graph.SimpleGraph;

import java.util.Iterator;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.jgrapht.graph.DefaultEdge;

public class FlipGraph extends SimpleGraph<LabeledTree, DefaultEdge> {
	
	int n = -1;
	LabeledTree lt = null;

	public FlipGraph(LabeledTree lt) {
		super(DefaultEdge.class);
		this.lt = lt;
		// TODO check if 'lt' is graceful
		//TODO implement constructor
		
	}
	
	public FlipGraph(int n) {
		super(DefaultEdge.class);
		// TODO implement constructor
		// idea: start from a path because it is easy to find a graceful labeling
		throw new RuntimeErrorException(null, "Stefan says: This constructor has not been implemented yet.");
	}
	
	public void createFlipGraph(LabeledTree lt) {
		this.addFlipNode(lt);
		Set<LabeledTree> neighbors = lt.getFlipTrees();
		this.addFlipNodeSet(neighbors);
		Iterator<LabeledTree> iterator = neighbors.iterator();
		//boolean updateOccured = false;
		while (iterator.hasNext()) {
			LabeledTree current = iterator.next();
			if (!this.equals(current) && !this.containsFlipEdge(lt, current)) {
				this.addFlipNode(current);
				this.addEdge(lt, current);
				this.createFlipGraph(current);
				//updateOccured = true;
			}
		}
	}
	
	public void addFlipNode(LabeledTree lt) {
		if (!lt.isContainedIn(this.vertexSet()))
			this.addVertex(lt);
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
		return (lt1.equals(this.getEdgeSource(e)) && lt2.equals(this.getEdgeTarget(e))) || (lt1.equals(this.getEdgeTarget(e)) &&lt2.equals(this.getEdgeSource(e)));
	}
	
	public void addFlipNodeSet(Set<LabeledTree> vertexSet) {
		Iterator<LabeledTree> iterator = vertexSet.iterator();
		while (iterator.hasNext()) {
			this.addFlipNode(iterator.next());  //TODO change this method
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3090899308347100128L;

}
