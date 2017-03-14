package bachelor_thesis;

import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.Random;
import java.util.Set;

public class Main {

	public static void main(String[] args) {

		Random rnd = new Random(System.currentTimeMillis());

		UndirectedGraph<Integer, DefaultEdge> g = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		int n = 5;

		for (int i = 0; i < n; i++)
			g.addVertex(i);

		// Create a random graph

		for (int i = 0; i < n; i++)
			for (int j = i + 1; j < n; j++) {
				if (rnd.nextInt(1000) < 500) {
					g.addEdge(i, j);
				}
			}

		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(0, 3);
		g.addEdge(0, 4);

		System.out.println(g);

		System.out.println(checkGraceful(g));
	}

	public static boolean checkGraceful(Graph<Integer, DefaultEdge> g) {
		int[] edgeLabels = new int[g.edgeSet().size() + 1];

		Set<DefaultEdge> edges = g.edgeSet();
		for (DefaultEdge e : edges) {
			System.out.println(e);
			int max = Math.max(g.getEdgeSource(e), g.getEdgeTarget(e));
			int min = Math.min(g.getEdgeSource(e), g.getEdgeTarget(e));
			edgeLabels[max - min]++;
		}

		for (int i = 1; i < edgeLabels.length; i++) {
			if (edgeLabels[i] != 1)
				return false;
		}
		return true;
	}
}