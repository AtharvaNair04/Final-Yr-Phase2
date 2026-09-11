import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;

class Main{
	Scanner sc=new Scanner(System.in);

	int INF=1<<28;
	double EPS=1e-9;

	int n;
	P v;
	C[] cs;

	void run(){
		for(;;){
			n=sc.nextInt();
			if(n==0){
				break;
			}
			int x=sc.nextInt();
			int y=sc.nextInt();
			int z=sc.nextInt();
			v=new P(x, y, z);
			cs=new C[n];
			double maxDist=0;
			for(int i=0; i<n; i++){
				x=sc.nextInt();
				y=sc.nextInt();
				z=sc.nextInt();
				int r=sc.nextInt();
				cs[i]=new C(x, y, z, r);
				maxDist=Math.max(maxDist, cs[i].c.abs()+r);
				// debug(cs[i].c.abs()+r+"");
			}
			v=v.div(v.abs());
			double d=0.01, e=0.00001;
			double e2=0.000001;
			P p=new P(0, 0, 0);
			P last=new P(0, 0, 0);
			for(int t=0;;){
				p=p.add(v.mul(d));
				int k=-1;
				for(int i=0; i<n; i++){
					if(cs[i].contains(p)){
						k=i;
					}
				}
				if(k!=-1){
					for(; cs[k].contains(p);){
						p=p.sub(v.mul(e));
					}
					P q=p.add(v.mul(e));
					p=p.add(q).div(2);
					for(; cs[k].contains(p);){
						p=p.sub(v.mul(e2));
					}
					q=p.add(v.mul(e2));
					p=p.add(q).div(2);

					P u=p.sub(cs[k].c);// @ü
					u=u.div(u.abs());// ³K»
					v=v.sub(u.mul(2*v.dot(u)));
					v=v.div(v.abs());
					last=new P(p);
					// debug(p);
					if(++t>=5){
						break;
					}
				}
				// ­U»è
				if(p.abs()>maxDist+10){
					break;
				}
			}
			println(String.format("%.3f %.3f %.3f", last.x, last.y, last.z));
		}
	}

	class C{
		P c;
		double r;

		C(double x, double y, double z, double r){
			c=new P(x, y, z);
			this.r=r;
		}

		boolean contains(P p){
			return p.sub(c).abs()+EPS<r;
		}
	}

	class P{
		double x, y, z;

		P(){
			this(0, 0, 0);
		}

		P(double x, double y, double z){
			this.x=x;
			this.y=y;
			this.z=z;
		}

		P(P p){
			this(p.x, p.y, p.z);
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
			return Math.sqrt(abs2());
		}

		double abs2(){
			return x*x+y*y+z*z;
		}

		// àÏ
		double dot(P p){
			return x*p.x+y*p.y+z*p.z;
		}

		public String toString(){
			return String.format("(%.3f, %.3f, %.3f)", x, y, z);
		}
	}

	void debug(Object... os){
		System.err.println(Arrays.deepToString(os));
	}

	void print(String s){
		System.out.print(s);
	}

	void println(String s){
		System.out.println(s);
	}

	public static void main(String[] args){
		new Main().run();
	}
}
