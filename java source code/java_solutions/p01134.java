import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.*;
import java.io.*;

public class Main {
	double EPS = 1e-10;
	ArrayList<L> ls;
	HashSet<P> cr;
	void run() {
		Scanner sc = new Scanner(System.in);
		for(;;) {
			int n = sc.nextInt();
			if( n == 0 ) break;
			
			ls = new ArrayList<L>();
			
			int sum = 1;
			for(int i=0;i<n;i++) {
				cr = new HashSet<P>();
				P p1 = new P(sc.nextDouble(), sc.nextDouble()), p2 = new P(sc.nextDouble(), sc.nextDouble());
				L l = new L(p1, p2);
				ls.add(l);
				int cnt = 0; 
				for(int j=0;j<ls.size()-1;j++) if( cross(l, ls.get(j)) ){
					P p = crossP(l, ls.get(j)); 
					if(!cr.contains(p)) cnt++;
					if(100 - abs(p.x) < EPS || 100 - abs(p.y) < EPS)  cnt--;
					else cr.add(p);
				}
				sum += cnt+1;
			}
			System.out.println(sum);
		}
	}
	
	P crossP(L l1, L l2) {
		double dx1 = l1.p1.x - l1.p2.x, dx2 = l2.p1.x - l2.p2.x,
			   dy1 = l1.p1.y - l1.p2.y, dy2 = l2.p1.y - l2.p2.y;
		double y = ( dy1 * dy2 * (l2.p1.x - l1.p1.x) + dx1*dy2*l1.p1.y - dx2*dy1*l2.p1.y ) / ( dx1*dy2 - dx2*dy1 );
		double x = ( dx1 * dx2 * (l2.p1.y - l1.p1.y) + dy1*dx2*l1.p1.x - dy2*dx1*l2.p1.x ) / ( dy1*dx2 - dy2*dx1 );
		return new P( x, y );
	}
	
	boolean cross(L l1, L l2) {
		return cross(l1.p1, l1.p2, l2.p1, l2.p2);
	}
	//線分交差判定
	boolean cross(P p1, P p2, P q1, P q2) {
		return ccw(p1, p2, q1) * ccw(p1, p2, q2) <= 0 &&
			ccw(q1, q2, p1) * ccw(q1, q2, p2) <= 0;
	}
	
	//時計回りなのか．反時計回りなのか，直線状なのか，とにかく位置関係をみる
	int ccw (P a, P b, P c) {
		P s = b.sub(a);
		P t = c.sub(a);
		if( s.det(t) > EPS ) return 1;
		if( s.det(t) < -EPS ) return -1;
		if( s.dot(t) < -EPS ) return 2;
		if( s.dot(s) + EPS < t.dot(t) ) return -2;
		return 0;
	}
	class L {
		P p1, p2;
		L(P p1, P p2) {
			this.p1 = new P(p1.x, p1.y);
			this.p2 = new P(p2.x, p2.y);
		}
	}
	
	class P {
		double x, y;
		P(double x, double y) {
			this.x = x;
			this.y = y;
		}
		double add(double a, double b) {	//誤差を考慮
			if( abs(a+b) < EPS * ( abs(a) + abs(b) ) ) return 0;
			return a + b;
		}
		
		P add(P p) {	//和
			return new P(x+p.x, y+p.y);
		}
		
		P sub(P p) {	//差
			return new P(x-p.x, y-p.y);
		}
		
		double dot(P p) {	//内積
				return add(x*p.x, y*p.y);
		}
		
		double det(P p) {	//外積
			return add(x*p.y, -y*p.x);
		}
		
		public boolean equals(Object o) {
			if( o instanceof P ) {
				P p = (P) o;
				if( d(new P(x, y), p) < EPS ) return true;
			}
			return false;
		}
		
		public int hashCode() {
			return (int)(x * 100000) + (int)(y * 100);
		}
	}
	
	double d(P p1, P p2) {
		return sqrt( (p1.x-p2.x) * (p1.x-p2.x) + (p1.y-p2.y) * (p1.y-p2.y) );
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
