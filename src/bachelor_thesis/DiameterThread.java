package bachelor_thesis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

public class DiameterThread extends Thread {

	private static AtomicInteger progress = new AtomicInteger(0);

	private DijkstraShortestPath<LabeledTree, DefaultEdge> dsp;
	private FlipGraph fg;
	public HashSet<TwoTrees> pairs;
	private int max = 0;

	public DiameterThread(FlipGraph fg) {
		this.fg = (FlipGraph) fg.clone();
		dsp = new DijkstraShortestPath<>(this.fg);
		pairs = new HashSet<TwoTrees>();
	}

	public void add(TwoTrees tt) {
		pairs.add(tt);
	}

	public int getMax() {
		return max;
	}

	@Override
	public void run() {
		System.out.println("Thread has started");
		
		Iterator<TwoTrees> iterator = pairs.iterator();

		while (iterator.hasNext()) {
			TwoTrees tt = iterator.next();
			LabeledTree source = tt.getFirst();
			LabeledTree sink = tt.getSecond();
			int val = progress.incrementAndGet();
			if (val % 10000 == 0)
				System.out.println(val);
			int pathLength = (int) dsp.getPathWeight(source, sink);
			// System.out.println(tt.toString() + " with distance " +
			// pathLength);
			// System.out.println(progress.incrementAndGet());
			if (pathLength > max)
				max = pathLength;
		}
	}

}
