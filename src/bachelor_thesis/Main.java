package bachelor_thesis;

import org.antlr.v4.runtime.misc.Pair;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class Main {

	public static void main(String[] args) {

		// TODO ***IMPORTANT*** implement equals() and hashCode() for
		// LabeledTree, FlipGraph, and solve the same issue for DefaultEdge and
		// DefaultWeightedEdge (probably by creating new classes)

		int n = 11; // TODO

		// FlipGraph fg = new FlipGraph(4);
		// System.out.println(fg);

		/*FlipGraph fgRec = new FlipGraph();
		FlipGraph fgIter = new FlipGraph();

		fgRec.createFlipGraphRec(new LabeledTree(new PruferCode(new int[] { 5, 2, 4, 1 }), true));
		fgIter.createFlipGraph(new LabeledTree(new PruferCode(new int[] { 5, 2, 4, 1 }), true));

		Set<LabeledTree> copy = new HashSet<LabeledTree>(fgRec.vertexSet());

		copy.removeAll(fgIter.vertexSet());

		System.out.println("Set difference:" + FlipGraph.treesToPruferCodes(copy));

		LabeledTree lt1 = new LabeledTree(new PruferCode(new int[] { 3, 5, 4, 0 }), true);
		LabeledTree lt2 = new LabeledTree(new PruferCode(new int[] { 5, 1, 0, 0 }), true);

		System.out.println(lt1);
		System.out.println(lt2);
		
		System.out.println(lt1.getPruferCode());
		System.out.println(lt2.getPruferCode());
		*/

		getStats(n);

	}

	public static void getStats(int n) {

		FlipGraph fg;
		FloydWarshallShortestPaths<LabeledTree, DefaultEdge> fwsp = null;

		int maxDeg;
		int minDeg;

		float avgDeg;

		int[] nodesOfDeg;

		for (int i = 3; i < n; ++i) {
			fg = new FlipGraph(i);
			fwsp = new FloydWarshallShortestPaths<>(fg);
			maxDeg = 0;
			minDeg = Integer.MAX_VALUE;
			avgDeg = 0;

			System.out.println("\n\n\n\n\n\n=================================================\n         n = " + i
					+ "\n=================================================\n");

			System.out.println("#vertices = " + fg.vertexSet().size());

			System.out.println("\ndiameter = " + (int) fwsp.getDiameter());

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

			System.out.println("\n\n  degree  |   number of vertices of that degree");
			for (int j = 0; j < nodesOfDeg.length; ++j) {
				if (j < 10)
					System.out.println("     " + j + "    |    " + nodesOfDeg[j]);
				else
					System.out.println("     " + j + "   |    " + nodesOfDeg[j]);

			}

			if (i > 3) {
				int min = 0;
				int max = i;
				LabeledTree canonicalPath = new LabeledTree();
				canonicalPath.addVertex(0);
				for (int iter = 1; iter < i; iter++) {
					if (iter % 2 == 0) {
						min++;
						canonicalPath.addVertex(min);
						canonicalPath.addEdge(min, max);
					} else {
						max--;
						canonicalPath.addVertex(max);
						canonicalPath.addEdge(min, max);
					}
				}

				int[] starArr = new int[i - 2];
				Arrays.fill(starArr, 0);
				LabeledTree canonicalStar = new LabeledTree(new PruferCode(starArr), true);

				// System.out.println(canonicalPath);
				// System.out.println(canonicalStar);

				System.out.println("\n\ndistance between canonical path and canonical star = "
						+ fwsp.getPath(canonicalPath, canonicalStar).getLength());
				System.out.println("Obtain the canonical star from the canonical path as follows:");
				Iterator<Pair<DefaultWeightedEdge, DefaultWeightedEdge>> iterator = fg
						.getFlipSequence(canonicalPath, canonicalStar).iterator();
				while (iterator.hasNext()) {
					System.out.println(iterator.next());
				}
			}

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