package bachelor_thesis;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) {

		int diameter;
		long start;
		long end;
		
		start = System.nanoTime();
		FlipGraph fg = new FlipGraph(10);
		end = System.nanoTime();
		System.out.println("ELAPSED TIME: " + ((end * 1.0 - start) / 1000000000L));

		System.out.println(fg.vertexSet().size());

		/*
		 * System.out.println(
		 * "\n\n\n\n----------------------------------------\nNormal\n----------------------------------------"
		 * ); start = System.nanoTime(); diameter = fg.getDiameter(); end =
		 * System.nanoTime(); System.out.println("ELAPSED TIME: " + ((end * 1.0
		 * - start) / 1000000000L)); System.out.println("Diameter is " +
		 * diameter);
		 * 
		 * 
		 * /*System.out.println(
		 * "\n\n\n\n----------------------------------------\nRandomized\n----------------------------------------"
		 * ); start = System.nanoTime(); diameter =
		 * fg.getDiameterRandomized(333); end = System.nanoTime();
		 * System.out.println("ELAPSED TIME: " + ((end * 1.0 - start) /
		 * 1000000000L)); System.out.println("Diameter is " + diameter);
		 * 
		 * 
		 * System.out.println(
		 * "\n\n\n\n----------------------------------------\nParallel\n----------------------------------------"
		 * ); start = System.nanoTime(); diameter = fg.getDiameterParallel(3);
		 * end = System.nanoTime(); System.out.println("ELAPSED TIME: " + ((end
		 * * 1.0 - start) / 1000000000L)); System.out.println("Diameter is " +
		 * diameter);
		 */

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