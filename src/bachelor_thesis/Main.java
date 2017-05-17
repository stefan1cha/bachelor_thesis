package bachelor_thesis;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.GraphTests;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) {
		int n = 24;
		for (int i = 6; i < 200; ++i) {
			if (i % 4 == 3)
				System.err.println("i = " + i + " -> " + constructThatTree(i).getFlipTrees().size() + " vs. " + c(i));
			else {
				if (constructThatTree(i).getFlipTrees().size() == c(i))
					System.out
							.println("i = " + i + " -> " + constructThatTree(i).getFlipTrees().size() + " vs. " + c(i));
				else
					System.err
							.println("i = " + i + " -> " + constructThatTree(i).getFlipTrees().size() + " vs. " + c(i));

			}
		}

		System.out.println("\n\n\n\n\n");
		LabeledTree lt = constructThatTree(20);
		System.out.println(lt);
		Iterator<LabeledTree> iterator = lt.getFlipTrees().iterator();
		while (iterator.hasNext()) {
			System.out.println(lt.getEdgeFlip(iterator.next()));
		}

		System.out.println("\n\n\n");
		lt = constructThatTree(19);
		System.out.println(lt);
		iterator = lt.getFlipTrees().iterator();
		while (iterator.hasNext()) {
			System.out.println(lt.getEdgeFlip(iterator.next()));
		}

	}

	public static int c(int n) {
		int s = ((n - (n % 4)) / 4) + 1;
		if (n % 4 == 0)
			return (int) (s + Math.floorDiv(n - 8, 12));
		else if (n % 4 == 1)
			return (int) (s + Math.floorDiv(n - 5, 12));
		else if (n % 4 == 2)
			return (int) (s + Math.floorDiv(n - 2, 12));
		else
			return (int) (s + Math.ceil((n - 8) / 12)) + 1;

	}

	public static LabeledTree constructThatTree(int input) {
		LabeledTree lt = null;
		int n = input - (input % 4);
		if (input % 4 != 3) {
			lt = new LabeledTree();
			// add vertices
			for (int i = 0; i < n; i++) {
				lt.addVertex(i);
			}

			// add edges
			for (int i = 1; i <= n / 4 - 1; i++)
				lt.addEdge(0, i);
			for (int i = n / 2 + 1; i <= n - 1; i++)
				lt.addEdge(0, i);
			for (int i = 0; i <= n / 4 - 1; i++)
				lt.addEdge(n / 2 + 2 * i + 1, n / 4 + i);
			lt.addEdge(n / 2, 3 * n / 4);

			if (input % 4 == 1) {
				lt.addVertex(input - 1);
				lt.addEdge(0, input - 1);
			} else if (input % 4 == 2) {
				lt.addVertex(input - 1);
				lt.addVertex(input - 2);
				lt.addEdge(0, input - 1);

				lt.addEdge(0, input - 2);
			}
		} else {
			n += 4;
			lt = new LabeledTree();
			// add vertices
			for (int i = 0; i < n; i++) {
				lt.addVertex(i);
			}

			// add edges
			for (int i = 1; i <= n / 4 - 1; i++)
				lt.addEdge(0, i);
			for (int i = n / 2 + 1; i <= n - 1; i++)
				lt.addEdge(0, i);
			for (int i = 0; i <= n / 4 - 1; i++)
				lt.addEdge(n / 2 + 2 * i + 1, n / 4 + i);
			lt.addEdge(n / 2, 3 * n / 4);

			// apply corrections
			lt.removeEdge(0, n - 1);
			lt.removeEdge(n - 1, n / 2 - 1);
			lt.removeVertex(n - 1);

			lt.removeEdge(n / 2, 3 * n / 4);

			lt.addEdge(0, n / 2);
			lt.addEdge(n / 2 - 1, 3 * n / 4 - 1);

		}
		return lt;
	}

	public static LabeledTree pickRandomTree(int n) {
		FlipGraph fg = new FlipGraph(n);
		Iterator<LabeledTree> iterator = fg.vertexSet().iterator();
		while (iterator.hasNext()) {
			if (Math.random() < 0.05)
				return iterator.next();
			else
				iterator.next();
		}
		return null;
	}

	public static void doFlips(LabeledTree lt) {
		int n = lt.vertexSet().size();
		while (lt != null) {
			System.out.println(lt + "  degree of " + (n - 1) + ": " + lt.degreeOf(n - 1));
			lt = tryOneFlip(lt);
		}
	}

	public static LabeledTree tryOneFlip(LabeledTree lt) {
		int n = lt.vertexSet().size();
		Iterator<LabeledTree> iterator = lt.getFlipTrees().iterator();
		while (iterator.hasNext()) {
			LabeledTree current = iterator.next();
			if (current.degreeOf(n - 1) < lt.degreeOf(n - 1)) {
				return current;
			}
		}
		return null;
	}

	public static double b(int n) {
		int s = 0;
		if (n % 3 == 0)
			s = (int) Math.ceil(n / 6);
		if (n % 3 == 1)
			s = (int) Math.ceil((n - 4) / 6);
		if (n % 3 == 2)
			s = (int) Math.ceil((n - 2) / 6);
		return Math.max(Math.ceil((n - 3) / 2), 0) + s;
	}

	public static double a(int n) {
		return Math.floor(n * n / 3.0) - n + 1;
	}

	public static void getStats(int start, int n) {

		FlipGraph fg;

		int maxDeg;
		int minDeg;

		float avgDeg;

		int[] nodesOfDeg;

		for (int i = start; i <= n; ++i) {
			fg = new FlipGraph(i);
			maxDeg = 0;
			minDeg = Integer.MAX_VALUE;
			avgDeg = 0;

			System.out.println("\n\n\n\n\n\n=================================================\n         n = " + i
					+ "\n=================================================\n");

			System.out.println("#vertices = " + fg.vertexSet().size());
			System.out.println("#edges = " + fg.edgeSet().size());

			for (LabeledTree lt : fg.vertexSet()) {
				if (fg.degreeOf(lt) > maxDeg)
					maxDeg = fg.degreeOf(lt);
				if (fg.degreeOf(lt) < minDeg)
					minDeg = fg.degreeOf(lt);
				avgDeg += fg.degreeOf(lt);
			}
			avgDeg /= fg.vertexSet().size();

			System.out.println("\nmin    degree = " + minDeg);
			System.out.println("max    degree = " + maxDeg);

			DecimalFormat df = new DecimalFormat("#.##");
			System.out.println("avg    degree = " + df.format(avgDeg));

			final FlipGraph fgF = (FlipGraph) fg.clone();

			LabeledTree[] vertexArray = fg.vertexSet().toArray(new LabeledTree[0]);
			Arrays.sort(vertexArray, new Comparator<LabeledTree>() {
				@Override
				public int compare(LabeledTree o1, LabeledTree o2) {
					if (fgF.degreeOf(o1) < fgF.degreeOf(o2))
						return -1;
					else if (fgF.degreeOf(o1) == fgF.degreeOf(o2))
						return 0;
					else
						return 1;
				};

				@Override
				public boolean equals(Object obj) {
					return super.equals(obj);
				}
			});

			System.out.println("median degree = " + fg.degreeOf(vertexArray[vertexArray.length / 2]));

			nodesOfDeg = new int[maxDeg + 1];
			for (int j = 0; j < vertexArray.length; ++j) {
				nodesOfDeg[fg.degreeOf(vertexArray[j])]++;
			}

			// FloydWarshallShortestPaths<LabeledTree, DefaultEdge> fwsp = null;
			// fwsp = new FloydWarshallShortestPaths<>(fg);
			// System.out.println("\ndiameter = " + (int) fwsp.getDiameter());

			System.out.println("\n\n  degree  |   number of vertices of that degree\n------------------------");
			for (int j = 0; j < nodesOfDeg.length; ++j) {
				if (j < 10)
					System.out.println("(" + j + ", " + nodesOfDeg[j] + ")");
				else
					System.out.println("(" + j + ", " + nodesOfDeg[j] + ")");

			}

			System.out.println(i + " & " + fg.vertexSet().size() + " & " + fg.edgeSet().size() + " & " + maxDeg + " & "
					+ minDeg + " & " + avgDeg + " & " + fg.degreeOf(vertexArray[vertexArray.length / 2]) + " & " + "0");

		}

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