import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Main {
	class Edge{
		int p, q;
		double cost;
		Edge(int p, int q, double cost){
			this.p = p; this.q = q;
			this.cost = cost;
		}
	}
	int[] par;
//	union-findの木の初期化
	void unionfindInit(int n){
		for(int i = 0;i < n;i++) {
			par[i] = i;
		}
	}
//	木の根を求める
	int unionfindRoot(int x) {
		if(par[x]==x) {
			return x;
		}
		else {
			return par[x] = unionfindRoot(par[x]);
		}
	}
//	xとyが同じ集合に属するか否か
	boolean unionfindSame(int x, int y) {
		return unionfindRoot(x) == unionfindRoot(y);
	}
	
//	xとyの属する集合を併合
	void unionfinUnite(int x, int y) {
		x = unionfindRoot(x);
		y = unionfindRoot(y);
		if (x == y) return;
		par[x]=y;
	}
	void run(){
		Scanner scan = new Scanner(System.in);
		int N = scan.nextInt();
		par = new int[N+1];
		int M = scan.nextInt();
		int coordinate[][] = new int[N+1][2];
		int p, q;
		double hen1, hen2;
		double[] hypotenuse = new double[M]; //パイル間の距離
		PriorityQueue<Edge> edge = new PriorityQueue<Edge>(new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				if(e2.cost > e1.cost) {
					return 1;
				}
				else if(e2.cost < e1.cost){
					return -1;
				}
				return 0;
			}
		});
		Edge tmp = new Edge(0,0,0);
//		釘の座標情報
		for(int i=1; i< N+1;i++) {
			coordinate[i][0] = scan.nextInt();;
			coordinate[i][1] = scan.nextInt();;
		}
//		フェンスの情報
		unionfindInit(N+1);
		for(int i=0; i < M; i++) {
			p = scan.nextInt();
			q = scan.nextInt();
			hen1 = Math.abs(coordinate[p][0] - coordinate[q][0]);
			hen2 = Math.abs(coordinate[p][1] - coordinate[q][1]);
			hypotenuse[i] = Math.sqrt(hen1*hen1+hen2*hen2);
			edge.offer(new Edge(p, q, hypotenuse[i]));
		}
//		System.out.println("mainasu: " + mainasu_sum);
		double sum=0;
		for(int i = 0; i < M; i++) {
			tmp = edge.poll();
			if(!unionfindSame(tmp.p, tmp.q)) unionfinUnite(tmp.p, tmp.q);
			else {
				sum += tmp.cost;
//				System.out.println("tmp: " + tmp.cost);
			}
		}
		System.out.println(sum);
	}
	public static void main(String[] args) {
		new Main().run();
	}
}
