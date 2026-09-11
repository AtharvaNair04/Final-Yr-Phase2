import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

//Color the Map
public class Main{

	class R{
		List<L> l;
		boolean u;
		int color, id;
		public R(int id) {
			this.id = id;
			l = new ArrayList<L>();
			u = false;
			color = 0;
		}
		boolean adj(R r){
			for(L a:l)for(L b:r.l){
				int x1 = a.x2-a.x1, y1 = a.y2-a.y1, x2 = b.x2-b.x1, y2 = b.y2-b.y1;
				if(x1*y2-x2*y1!=0)continue;
				if(x1==0){
					if(a.x1!=b.x1)continue;
					P[] p = new P[4];
					p[0] = new P(a.x1, a.y1, 0); p[1] = new P(a.x2, a.y2, 0);
					p[2] = new P(b.x1, b.y1, 1); p[3] = new P(b.x2, b.y2, 1);
					Arrays.sort(p);
					if(p[0].id==p[1].id)continue;
					if(!p[1].eq(p[2]))return true;
				}
				else{
					double alpha = y1/x1;
					double beta = a.y1-alpha*a.x1;
					double y = b.y1, x = alpha*b.x1+beta;
					if(Math.abs(y-x)<1e-7){
						P[] p = new P[4];
						p[0] = new P(a.x1, a.y1, 0); p[1] = new P(a.x2, a.y2, 0);
						p[2] = new P(b.x1, b.y1, 1); p[3] = new P(b.x2, b.y2, 1);
						Arrays.sort(p);
						if(p[0].id==p[1].id)continue;
						if(!p[1].eq(p[2]))return true;
					}
				}
			}
			return false;
		}
	}
	class P implements Comparable<P>{
		int id;
		double x, y;
		public P(double x, double y, int d) {
			this.x = x;
			this.y = y;
			id = d;
		}
		public int compareTo(P o) {
			if(x<o.x)return -1;
			if(o.x<x)return 1;
			return (int) Math.signum(y-o.y);
		}
		boolean eq(P p){
			return Math.abs(x-p.x)<1e-7 && Math.abs(y-p.y)<1e-7;
		}
	}
	class L{
		int x1, y1, x2, y2;
		public L(int x1, int y1, int x2, int y2) {
			if(x1<x2||x1==x2&&y1<y2){
				this.x1 = x1;
				this.y1 = y1;
				this.x2 = x2;
				this.y2 = y2;
			}
			else{
				this.x1 = x2;
				this.y1 = y2;
				this.x2 = x1;
				this.y2 = y1;
			}
		}
	}

	R[] c;
	int N, min;
	boolean[][] adj;
	
	void dfs(int k, int maxcolor){
		boolean[] u = new boolean[11];
		for(int i=0;i<N;i++){
			if(adj[k][i])u[c[i].color] = true;
		}
		if(k==N-1){
			for(int i=1;i<=maxcolor+1;i++){
				if(!u[i]){
					min = Math.min(min, Math.max(i, maxcolor));
					return;
				}
			}
		}
		for(int i=1;i<=maxcolor+1;i++){
			if(!u[i]){
				c[k].color = i;
				dfs(k+1, Math.max(i, maxcolor));
				c[k].color = 0;
			}
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			int n = sc.nextInt();
			if(n==0)break;
			int id = 0;
			Map<String, Integer> ref = new HashMap<String, Integer>();
			c = new R[10];
			while(n--!=0){
				String name = sc.next();
				R r;
				if(ref.containsKey(name))r = c[ref.get(name)];
				else{
					r = new R(id);
					c[id] = r;
					ref.put(name, id++);
				}
				int sx = sc.nextInt(), sy = sc.nextInt();
				int px = sx, py = sy;
				for(;;){
					int x = sc.nextInt();
					if(x==-1){
						r.l.add(new L(px, py, sx, sy));
						break;
					}
					int y = sc.nextInt();
					r.l.add(new L(px, py, x, y));
					px = x; py = y;
				}
			}
			adj = new boolean[id][id];
			for(int i=0;i<id;i++)for(int j=i+1;j<id;j++){
				if(adj[i][j])continue;
				if(c[i].adj(c[j])){
					adj[i][j] = true;
					adj[j][i] = true;
				}
			}
			N = id;
			min = 10;
			dfs(0, 0);
			System.out.println(min);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
