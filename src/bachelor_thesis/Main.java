package bachelor_thesis;

import org.jgrapht.Graph;
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
		LabeledTree lt6 = new LabeledTree(new PruferCode(new int[] { 0, 0 }), true);
		System.out.println("\n\nFlip trees of " + lt6 + " are:\n" + lt6.getFlipTrees());

		// Test 7
		LabeledTree lt7 = new LabeledTree(new PruferCode(new int[] { 3, 1 }), true);
		System.out.println("\n\nFlip trees of " + lt7 + " are:\n" + lt7.getFlipTrees());
		
		// Test 8
		LabeledTree lt8 = new LabeledTree(new PruferCode(new int[]{2}),true);
		System.out.println("\n\nFlip trees of " + lt8 + " are:\n" + lt8.getFlipTrees());
		
		// Remove this
		LabeledTree ltx = new LabeledTree();

		for (int i = 0; i < 6; ++i) {
			ltx.addVertex(i);
		}

		ltx.addEdge(0, 3);
		ltx.addEdge(1, 3);
		ltx.addEdge(2, 3);
		ltx.addEdge(3, 4);
		ltx.addEdge(4, 5);

		LabeledTree lty = new LabeledTree();

		for (int i = 0; i < 6; ++i) {
			lty.addVertex(i);
		}

		lty.addEdge(3, 0);
		lty.addEdge(1, 3);
		lty.addEdge(3, 2);
		lty.addEdge(4, 3);
		lty.addEdge(5, 4);

		LabeledTree ltz = new LabeledTree();

		for (int i = 0; i < 6; ++i) {
			ltz.addVertex(i);
		}

		ltz.addEdge(0, 3);
		ltz.addEdge(1, 3);
		ltz.addEdge(2, 3);
		ltz.addEdge(3, 4);
		ltz.addEdge(4, 5);

		System.out.println("\n\n\nhere: " + ltx.equals(ltz));

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