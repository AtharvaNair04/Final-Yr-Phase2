import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

//Princess in Danger
public class Main{

	int[][] dist, wf;
	int INF = 1<<29, P = 107;
	int N, M, L, K, A, H;
	
	class E{
		int t, d;
		public E(int t, int d) {
			this.t = t;
			this.d = d;
		}
	}
	class Scanner {
		int nextInt() {
			try {
				int c = System.in.read();
				while (c != '-' && (c < '0' || '9' < c))
					c = System.in.read();
				if (c == '-') return -nextInt();
				int res = 0;
				do {
					res *= 10;
					res += c - '0';
					c = System.in.read();
				} while ('0' <= c && c <= '9');
				return res;
			} catch (Exception e) {
				return -1;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	void run(){
		Scanner sc = new Scanner();
		for(;;){
			N = sc.nextInt(); M = sc.nextInt(); L = sc.nextInt(); K = sc.nextInt(); A = sc.nextInt(); H = sc.nextInt();
			if((N|M|L|K|A|H)==0)break;
			boolean[] ice = new boolean[N];
			ice[A] = ice[H] = true;
			while(L--!=0)ice[sc.nextInt()] = true;
			List<E>[] es = new ArrayList[N];
			for(int i=0;i<N;i++)es[i]=new ArrayList<E>();
			wf = new int[N][N];
			for(int[]a:wf)Arrays.fill(a, INF);
			while(K--!=0){
				int s = sc.nextInt(), t = sc.nextInt(), d = sc.nextInt();
				wf[s][t] = wf[t][s] = d;
				es[s].add(new E(t, d)); es[t].add(new E(s, d));
			}
			for(int k=0;k<N;k++)for(int i=0;i<N;i++)for(int j=0;j<N;j++)wf[i][j]=Math.min(wf[i][j], wf[i][k]+wf[k][j]);
			dist = new int[N][M+1];
			for(int[]a:dist)Arrays.fill(a, INF);
			for(int i=0;i<=M;i++)dist[A][i] = 0;
			PriorityQueue<Integer> q = new PriorityQueue<Integer>(N, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					int a = o1/P, b = o1%P, c = o2/P, d = o2%P;
					return (dist[a][b]+wf[a][H])-(dist[c][d]+wf[c][H]);
				}
			});
			q.add(A*P+M);
			int res = INF;
			while(!q.isEmpty()){
				int v = q.poll();
				int pos = v/P, m = v%P;
				if(pos==H){
					res = dist[pos][m]; break;
				}
				for(E e:es[pos]){
					for(int t=0;t+m<=M;t++){
						if(t>0&&!ice[pos])break;
						int nm = t+m-e.d;
						if(nm<0)continue;
						int w = dist[pos][m]+t+e.d;
						if(w<dist[e.t][nm]){
							q.remove(e.t*P+nm); dist[e.t][nm] = w;
							int r = nm-1;
							while(r>=0&&w<=dist[e.t][r]){
								q.remove(e.t*P+r); dist[e.t][r] = w; r--;
							}
							q.add(e.t*P+nm);
						}
					}
				}
				
			}
			System.out.println(res==INF?"Help!":res);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
