import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

// Exportation in Space
// 2012/12/13
public class Main{
	Scanner sc=new Scanner(System.in);

	double EPS=1e-6;
	int n;
	P[] ps;

	void run(){
		n=sc.nextInt();
		ps=new P[n];
		for(int i=0; i<n; i++){
			double x=sc.nextInt();
			double y=sc.nextInt();
			double z=sc.nextInt();
			ps[i]=new P(x, y, z);
		}
		solve();
	}

	void solve(){
		for(int i=0; i<n; i++){
			P d=new P(random()*EPS, random()*EPS, random()*EPS);
			ps[i]=ps[i].add(d);
		}

		P[][] ch=convexHull(ps);
		double sum=0;
		for(P[] ps : ch){
			double a=ps[0].sub(ps[1]).abs();
			double b=ps[1].sub(ps[2]).abs();
			double c=ps[2].sub(ps[0]).abs();
			sum+=area(a, b, c);
		}
		println(String.format("%.12f", sum));
	}

	double area(double a, double b, double c){
		double s=(a+b+c)/2;
		return sqrt(s*(s-a)*(s-b)*(s-c));
	}

	P[][] convexHull(P[] ps){
		int n=ps.length;

		int[][] vs=new int[n][n];
		List<int[]> crt=new ArrayList<int[]>();
		crt.add(new int[]{0, 1, 2});
		crt.add(new int[]{2, 1, 0});

		for(int i=3; i<n; i++){
			List<int[]> next=new ArrayList<int[]>();
			for(int[] t : crt){
				int v=ps[t[1]].sub(ps[t[0]]).det(ps[t[2]].sub(ps[t[0]]))
						.dot(ps[i].sub(ps[t[0]]))<0?-1:1;
				if(v<0)
					next.add(t);
				for(int j=0; j<3; j++){
					if(vs[t[(j+1)%3]][t[j]]==0){
						vs[t[j]][t[(j+1)%3]]=v;
					}else{
						if(vs[t[(j+1)%3]][t[j]]!=v){
							if(v>0)
								next.add(new int[]{t[j], t[(j+1)%3], i});
							else
								next.add(new int[]{t[(j+1)%3], t[j], i});
						}
						vs[t[(j+1)%3]][t[j]]=0;
					}
				}
			}
			crt=next;
		}

		P[][] pss=new P[crt.size()][3];
		for(int i=0; i<pss.length; i++){
			for(int j=0; j<3; j++)
				pss[i][j]=ps[crt.get(i)[j]];
		}
		return pss;
	}

	class P{
		double x, y, z;

		P(double x, double y, double z){
			this.x=x;
			this.y=y;
			this.z=z;
		}

		P add(P p){
			return new P(x+p.x, y+p.y, z+p.z);
		}

		P sub(P p){
			return new P(x-p.x, y-p.y, z-p.z);
		}

		P mul(double m){
			return new P(x*m, y*m, z*m);
		}

		P div(double d){
			return new P(x/d, y/d, z/d);
		}

		double abs(){
			return sqrt(abs2());
		}

		double abs2(){
			return x*x+y*y+z*z;
		}

		double dot(P p){
			return x*p.x+y*p.y+z*p.z;
		}

		P det(P p){
			return new P(y*p.z-z*p.y, z*p.x-x*p.z, x*p.y-y*p.x);
		}
	}

	void println(String s){
		System.out.println(s);
	}

	public static void main(String[] args){
		new Main().run();
	}
}
