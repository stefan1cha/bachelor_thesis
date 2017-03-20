package bachelor_thesis;

import org.jgrapht.Graph;
import org.jgrapht.GraphTests;
import org.jgrapht.graph.DefaultEdge;

import java.util.Set;

public class Main {

	public static void main(String[] args) {

		// Test 1
		LabeledTree lt = new LabeledTree();

		for (int i = 0; i < 6; ++i) {
			lt.addVertex(i);
		}

		lt.addEdge(0, 3);
		lt.addEdge(1, 3);
		lt.addEdge(2, 3);
		lt.addEdge(3, 4);
		lt.addEdge(4, 5);

		System.out.println("Prufer Code: " + lt.getPruferCode());

		System.out.println("Labeled Tree: " + new LabeledTree(lt.getPruferCode(), false));

		System.out.println("Graceful: " + lt.isGraceful());

		// Test 2

		LabeledTree lt2 = new LabeledTree(new PruferCode(new int[] { 0, 6, 5, 5, 0 }), false);

		System.out.println("\n\nLabeled Tree: " + lt2);

		System.out.println("Prufer Code: " + lt2.getPruferCode());

		System.out.println("Graceful: " + lt2.isGraceful());

		// Test 3
		LabeledTree lt3 = new LabeledTree(new PruferCode(new int[] { 0, 0, 0 }), true);
		System.out.println("\n\nLabeled Tree: " + lt3);
		System.out.println("Graceful: " + lt3.isGraceful());

		// Test 4
		LabeledTree lt4 = new LabeledTree(new PruferCode(new int[] { 8, 8, 4, 5, 2, 2, 2 }), true);
		System.out.println("\n\nLabeled Tree: " + lt4);
		System.out.println("Graceful: " + lt4.isGraceful());

		// Test 5
		LabeledTree lt5 = new LabeledTree(new PruferCode(new int[] { 8, 8, 3, 5, 2, 2, 2 }), true);
		System.out.println("\n\nLabeled Tree: " + lt5);
		System.out.println("Graceful: " + lt5.isGraceful());
		
		// Test 6
		LabeledTree lt6 = new LabeledTree(new PruferCode(new int[] {0,0}), true);
		System.out.println("\n\nFlip trees of " + lt6 + " are:\n" + lt6.getFlipTrees());
		
		// Remove this
		LabeledTree ltx = new LabeledTree();
		ltx.addVertex(0);
		ltx.addVertex(1);
		System.out.println("\n\n\nltx " + GraphTests.isTree(ltx));

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