package bachelor_thesis;
import org.jgrapht.Graph;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Set;

public class Main {

	public static void main(String[] args) {
		
		LabeledTree<DefaultWeightedEdge> lt = new LabeledTree<DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		for (int i = 0; i < 6;++i) {
			lt.addVertex(i);
		}
		
		lt.addEdge(0,3);
		lt.addEdge(1,3);
		lt.addEdge(2,3);
		lt.addEdge(3,4);
		lt.addEdge(4,5);
		
		System.out.println("Prufer Code: " + lt.getPruferCode());
		
		System.out.println(new LabeledTree<DefaultWeightedEdge>(lt.getPruferCode()));
		
		
	}
	
	public static boolean checkGraceful (Graph<Integer, DefaultEdge> g) {
		int[] edgeLabels = new int[g.edgeSet().size()+1];
		
	    Set<DefaultEdge> edges = g.edgeSet();
		for (DefaultEdge e: edges){
			System.out.println(e);
			int max = Math.max(g.getEdgeSource(e), g.getEdgeTarget(e));
			int min = Math.min(g.getEdgeSource(e), g.getEdgeTarget(e));
			edgeLabels[max-min]++;
		}
		
		for (int i = 1; i<edgeLabels.length; i++) {
			if (edgeLabels[i]!=1)
				return false;
		}
		return true;
	}
}