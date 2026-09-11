import java.util.*;

public class Main {
	static class Route {
		int to;
		int cost;
		long time;

		public Route(int to, int cost, long time) {
			this.to = to;
			this.cost = cost;
			this.time = time;
		}
	}
	static class Graph {
		int N;
		ArrayList<ArrayList<Route>> node;
		int[] exRate;
		long[] exTime;
		
		public Graph(int N) {
			this.N = N;
			node = new ArrayList<>();
			for(int i=0; i<N; i++)
				node.add(new ArrayList<Route>());
			exRate = new int[N];
			exTime = new long[N];
		}
		
		public void addEdge(int from, int to, int cost, int time) {
			node.get(from).add(new Route(to, cost, time));
			node.get(to).add(new Route(from, cost, time));
		}
		
		public void addEx(int i, int rate, int time) {
			exRate[i] = rate;
			exTime[i] = time;
		}
	}
	
	static class Path {
		int to;
		int have;
		long time;
		public Path(int to, int have, long time) {
			this.to = to;
			this.have = have;
			this.time = time;
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int M = sc.nextInt();
		int S = sc.nextInt();
		
		Graph g = new Graph(N);
		for(int i=0; i<M; i++) {
			g.addEdge(sc.nextInt()-1, sc.nextInt()-1, sc.nextInt(), sc.nextInt());
		}
		for(int i=0; i<N; i++) {
			g.addEx(i, sc.nextInt(), sc.nextInt());
		}
		
		int maxCost = (N-1)*50;
		if(S>maxCost)
			S = maxCost;
		final long INF = (long)1e20;
		long[][] time = new long[N][maxCost+1];
		for(int i=0; i<N; i++)
			Arrays.fill(time[i], INF);
		
		LinkedList<Path> q = new LinkedList<>();
		q.add(new Path(0, S, 0));
		
		while(!q.isEmpty()) {
			Path p = q.poll();
			if(time[p.to][p.have] <= p.time)
				continue;
			time[p.to][p.have] = p.time;
			for(Route r : g.node.get(p.to)) {
				int next = r.to;
				int have = p.have-r.cost;
				if(have<0)
					continue;
				long t = p.time + r.time;
				if(time[next][have]>t)
					q.add(new Path(next, have, t));
			}
			
			int afterCoin = Math.min(p.have + g.exRate[p.to], maxCost);
			long afterTime = p.time + g.exTime[p.to];
			if(time[p.to][afterCoin] > afterTime) {
				q.add(new Path(p.to, afterCoin, afterTime));
			}
		}
		
		for(int i=1; i<N; i++) {
			long min = Long.MAX_VALUE;
			for(int j=0; j<maxCost; j++) {
				min = Math.min(min, time[i][j]);
			}
			System.out.println(min);
		}
		
		sc.close();
	}
}
