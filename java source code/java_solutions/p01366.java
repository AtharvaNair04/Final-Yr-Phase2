import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
public class Main{
	public static void main(String[] args)throws Exception{
		new Main().solve();
	}
	void solve(){
		Scanner sc=new Scanner(System.in);
		while(true){
			int n=sc.nextInt();//the number of cities
			int m=sc.nextInt();//the number of roads
			if(n==0&&m==0)break;
			Dijkstra ds=new Dijkstra(n);
			for(int i=0;i<m;i++){
				int u=sc.nextInt()-1;
				int v=sc.nextInt()-1;
				int d=sc.nextInt();
				int c=sc.nextInt();
				ds.addEdge(u, v, d, c);
				ds.addEdge(v, u, d, c);
			}
			ds.getShortestPath(0, 0);
			Prim prim=new Prim(n,ds.edges,ds.d);
			System.out.println(prim.get_minimum_spanning_tree());
		}
	}
	class Dijkstra{
		int n;
		long[] d;
		long[] c;
		int s;
		List<Edge>[]edges;
		PriorityQueue<Vertice> pq;
		final long INF=Long.MAX_VALUE/4;
		int[] prev;//????????????????????????????????????????????????
		Dijkstra(int n){
			this.n=n;
			d=new long[n];
			c=new long[n];
			@SuppressWarnings("unchecked")
			List<Edge>[]edges=new List[n];
			this.edges=edges;
			for(int i=0;i<n;i++){
				edges[i]=new ArrayList<Edge>();
			}
			s=-1;
			prev=new int[n];
		}
		long getShortestPath(int i,int j){
			if(i!=s){
				s=i;
				Arrays.fill(d, INF);
				Arrays.fill(prev,-1);
				Arrays.fill(c, INF);
				c[i]=0;
				d[i]=0;
				prev[0]=0;
				pq=new PriorityQueue<Vertice>();
				pq.add(new Vertice(i,d[i],0));
				while(!pq.isEmpty()){
					Vertice u=pq.poll();
					if(d[u.me]<u.distance){
						continue;//skip old vertex
					}
					for(int ii=0;ii<edges[u.me].size();ii++){
						Edge e=edges[u.me].get(ii);
						if(e.distance+d[e.from]<d[e.to]){
							d[e.to]=e.distance+d[e.from];
							c[e.to]=e.cost+c[e.from];
							pq.add(new Vertice(e.to,d[e.to],c[e.from]+e.cost));
							prev[e.to]=e.from;
						}
					}
				}
				s=i;
			}
			return d[j];
		}
		void addEdge(int from,int to,long distance,long cost){
			edges[from].add(new Edge(from,to,distance,cost));
		}
	}
	class Edge{
		int from;
		int to;
		long distance;
		long cost;
		Edge(int i,int j,long distance,long cost){
			this.from=i;
			this.to=j;
			this.distance=distance;
			this.cost=cost;
		}
	}
	class Vertice implements Comparable<Vertice>{
		int me;
		long distance;
		long total_cost;
		Vertice(int me,long distance,long total_cost){
			this.me=me;
			this.distance=distance;
			this.total_cost=total_cost;
		}
		@Override
		public int compareTo(Vertice o){
			//			return Long.compare(this.cost, o.cost);
			return this.distance>o.distance?1:this.distance<o.distance?-1:this.total_cost>o.total_cost?1:this.total_cost<o.total_cost?-1:0;
		}
	}

	class Prim{
		int n;
		//		int[][] table;//??£??\?????????
		boolean[] used;
		List<Edge>[] edges;
		long[] d;
		long sum_weight=-1;//????°???¨?????¨?????¨???????????????
		Prim(int n,List<Edge>[] edges,long[] d){
			this.n=n;
			this.edges=edges;
			used=new boolean[n];
			Arrays.fill(used, false);
			this.d=d;
		}
		long get_minimum_spanning_tree(){
			if(sum_weight==-1){
				sum_weight=0;
				for(int k=1;k<n;k++){
					int min=999999999;
					for(int l=0;l<edges[k].size();l++){
						Edge e=edges[k].get(l);
						if(d[e.to]+e.distance!=d[e.from])continue;
						min=(int)Math.min(min, e.cost);
					}
					sum_weight+=min;
				}
			}
			return sum_weight;
		}
	}
	//	class Edge implements Comparable<Edge>{
	//		//  int from;
	//		int to;
	//		int cost;
	//		Edge(int to,int cost){
	//			//   this.from=from;
	//			this.to=to;
	//			this.cost=cost;
	//		}
	//		public int compareTo(Edge o){
	//			return this.cost>o.cost?1:this.cost==o.cost?0:-1;
	//		}
	//	}
	void tr(Object...o){System.out.println(Arrays.deepToString(o));}
}
