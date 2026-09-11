import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

// Doragoso Ball
// 2013/04/26
public class Main{
	Scanner sc=new Scanner(System.in);

	int n, m, nb, k;
	int[] us, vs, ws;
	int s, t;
	int[] bs;

	void run(){
		for(;;){
			n=sc.nextInt();
			m=sc.nextInt();
			nb=sc.nextInt();
			k=sc.nextInt();
			if(n==0){
				break;
			}
			us=new int[m];
			vs=new int[m];
			ws=new int[m];
			for(int i=0; i<m; i++){
				us[i]=sc.nextInt()-1;
				vs[i]=sc.nextInt()-1;
				ws[i]=sc.nextInt();
			}
			s=sc.nextInt()-1;
			t=sc.nextInt()-1;
			bs=new int[nb];
			for(int i=0; i<nb; i++){
				bs[i]=sc.nextInt()-1;
			}
			solve();
		}
	}

	void solve(){
		int N=n*(1<<nb);
		@SuppressWarnings("unchecked")
		ArrayList<E>[] es=new ArrayList[N];
		for(int i=0; i<N; i++){
			es[i]=new ArrayList<E>();
		}
		int[] has=new int[n];
		fill(has, -1);
		for(int i=0; i<nb; i++){
			has[bs[i]]=i;
		}
		for(int i=0; i<m; i++){
			int u=us[i], v=vs[i];
			for(int S=0; S<1<<nb; S++){
				if(has[u]==-1||(S>>has[u]&1)==1){
					int T=S;
					if(has[v]>=0){
						T|=1<<has[v];
					}
					int uS=u*(1<<nb)+S;
					int vT=v*(1<<nb)+T;
					es[uS].add(new E(vT, ws[i]));
				}
				if(has[v]==-1||(S>>has[v]&1)==1){
					int T=S;
					if(has[u]>=0){
						T|=1<<has[u];
					}
					int vS=v*(1<<nb)+S;
					int uT=u*(1<<nb)+T;
					es[vS].add(new E(uT, ws[i]));
				}
			}
		}
		int sS=s*(1<<nb);
		if(has[s]>=0){
			sS|=1<<has[s];
		}
		ArrayList<Integer> d=kDijkstra(es, sS, k)[t*(1<<nb)+((1<<nb)-1)];
		println(""+(d.size()>0?d.get(d.size()-1):"NA"));
	}

	ArrayList<Integer>[] kDijkstra(ArrayList<E>[] es, int s, int k){
		int n=es.length;
		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] d=new ArrayList[n];
		for(int i=0; i<n; i++)
			d[i]=new ArrayList<Integer>();
		PriorityQueue<P> que=new PriorityQueue<P>();
		que.offer(new P(s, 0));
		for(; !que.isEmpty();){
			P p=que.poll();
			if(d[p.v].size()>=k)
				continue;
			d[p.v].add(p.d);
			for(E e : es[p.v])
				que.offer(new P(e.to, p.d+e.cost));
		}
		return d;
	}

	class E{
		int to, cost;

		E(int to, int cost){
			this.to=to;
			this.cost=cost;
		}
	}

	class P implements Comparable<P>{
		int v, d;

		P(int v, int d){
			this.v=v;
			this.d=d;
		}

		public int compareTo(P p){
			return d-p.d;
		}
	}

	void println(String s){
		System.out.println(s);
	}

	public static void main(String[] args){
		new Main().run();
	}
}
