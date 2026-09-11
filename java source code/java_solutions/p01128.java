import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main{

	double EPS = 1e-9;
	
	class P implements Comparable<P>{
		double x, y;
		boolean me, up;
		public P(double x, double y, boolean me, boolean up) {
			this.x = x;
			this.y = y;
			this.me = me;
			this.up = up;
		}
		public int compareTo(P o) {
			if(Math.abs(x-o.x)<EPS)return (int) Math.signum(x-o.x);
			else return (int) Math.signum(y-o.y);
		}
	}
	
	boolean cross(P a, P b, P s, P t){
		if(ex(a, b, s)*ex(a, b, t)>0)return false;
		if(ex(b, a, s)*ex(b, a, t)>0)return false;
		if(ex(t, s, a)*ex(t, s, b)>0)return false;
		return ex(s, t, a)*ex(s, t, b)<EPS;
	}
	double ex(P s, P t, P r){
		double x1 = t.x-s.x, y1 = t.y-s.y, x2 = r.x-s.x, y2 = r.y-s.y;
		return x1*y2-x2*y1;
	}
	double dist(P a, P b, P p){
		double x1 = b.x-a.x, y1 = b.y-a.y, x2 = p.x-a.x, y2 = p.y-a.y;
		return Math.abs((x1*y2-x2*y1))/Math.hypot(x1, y1);
	}
	P cp(P a, P b, P s, P t, boolean m, boolean u){
		double ds = dist(a, b, s), dt = dist(a, b, t);
		double k = ds/(ds+dt);
		double dx = t.x-s.x, dy = t.y-s.y;
		return new P(s.x+k*dx, s.y+k*dy, m, u);
	}
	boolean on(P a, P b, P p){
		if(Math.abs(a.x-b.x)<EPS)return Math.min(a.y, b.y)<=p.y&&p.y<=Math.max(a.y, b.y);
		double alpha = (b.y-a.y)/(b.x-a.x);
		double beta = a.y-alpha*a.x;
		if(Math.abs(p.y-(alpha*p.x+beta))>EPS)return false;
		return Math.min(a.x, b.x)<=p.x&&p.x<=Math.max(a.x, b.x);
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while(T--!=0){
			P A = new P(sc.nextDouble(), sc.nextDouble(), true, true), B = new P(sc.nextDouble(), sc.nextDouble(), true, true);
			int n = sc.nextInt();
			P[][] line = new P[n][2];
			for(int i=0;i<n;i++){
				double x1 = sc.nextDouble(), y1 = sc.nextDouble(), x2 = sc.nextDouble(), y2 = sc.nextDouble();
				boolean m = sc.nextInt()==1, u = sc.nextInt()==1;
				line[i][0] = new P(x1, y1, m, u);
				line[i][1] = new P(x2, y2, m, u);
			}
			List<P> l = new ArrayList<P>();
			for(int i=0;i<n;i++){
				P s = line[i][0], t = line[i][1];
				double x1 = B.x-A.x, y1 = B.y-A.y, x2 = t.x-s.x, y2 = t.y-s.y;
				if(Math.abs(x1*y2-x2*y1)<EPS){
					if(on(A, B, s))l.add(s);
					if(on(A, B, t))l.add(t);
				}
				else 
					if(cross(A, B, s, t))l.add(cp(A, B, s, t, s.me, s.up));
			}
			Collections.sort(l);
			int res = 0;
			if(l.size()>0){
				P h = l.get(0);
				boolean u = h.me?h.up:!h.up;
				for(int i=1;i<l.size();i++){
					h = l.get(i);
					if(h.me){
						if(h.up!=u){
							res++; u=!u;
						}
					}
					else{
						if(h.up==u){
							res++; u=!u;
						}
					}
				}
			}
			System.out.println(res);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
