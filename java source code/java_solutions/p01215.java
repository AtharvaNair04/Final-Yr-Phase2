import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

public strictfp class Main{
	Scanner sc=new Scanner(System.in);

	int INF=1<<28;
	double EPS=1e-4;

	int n;
	double g;
	double x, y;
	double[] xs1, ys1, xs2, ys2;

	void run(){
		for(;;){
			n=sc.nextInt();
			if(n==0){
				break;
			}
			g=sc.nextDouble();
			x=sc.nextDouble();
			y=sc.nextDouble();
			xs1=new double[n];
			ys1=new double[n];
			xs2=new double[n];
			ys2=new double[n];
			for(int i=0; i<n; i++){
				double x1=sc.nextDouble();
				double y1=sc.nextDouble();
				double x2=sc.nextDouble();
				double y2=sc.nextDouble();
				if(x1>x2){
					xs1[i]=x1;
					ys1[i]=y1;
					xs2[i]=x2;
					ys2[i]=y2;
				}else{
					xs1[i]=x2;
					ys1[i]=y2;
					xs2[i]=x1;
					ys2[i]=y1;
				}
				if(1==2){
					xs1[i]=sc.nextDouble();
					ys1[i]=sc.nextDouble();
					xs2[i]=sc.nextDouble();
					ys2[i]=sc.nextDouble();
				}
			}
			solve();
		}
	}

	P p, v, a;
	int pseg, seg;
	boolean finished;
	double ans;

	void solve(){
		p=new P(x, y);
		v=new P(0, 0);
		a=new P(0, -g);
		pseg=-1;
		finished=false;
		boolean first=true;

		ans=Double.MAX_VALUE;
		for(;;){
			if(first){
				fallFirst();
				first=false;
			}else{
				fall();
			}
			if(finished){
				break;
			}
			slope();
		}
		for(; ans==Double.MAX_VALUE||Double.isNaN(ans)||Double.isInfinite(ans););
		// hoge
		println(String.format("%.12f", ans));
	}

	void fallFirst(){
		debug("fallFirst");
		debug("p", p);
		debug("v", v);
		double tMin=Double.MAX_VALUE;
		seg=-1;
		for(int i=0; i<n; i++){
			debug("i", i);
			double x1=xs1[i], y1=ys1[i], x2=xs2[i], y2=ys2[i];
			double a=(y2-y1)/(x2-x1);
			double b=(x2*y1-x1*y2)/(x2-x1);
			double y=a*p.x+b;
			for(double t : quad(-g/2, 0, p.y-y)){
				if(t>EPS&&between(p.x, x1, x2)&&between(y, y1, y2)){
					if(t<tMin){
						tMin=t;
						seg=i;
					}
				}
			}
		}

		debug("seg", seg);
		debug("tMin", tMin);
		if(seg==-1){
			debug("finished");
			ans=p.x;
			finished=true;
		}else{
			p=p.add(a.div(2).mul(sq(tMin))).add(v.mul(tMin));
			v=v.add(a.mul(tMin));
			debug("p", p);
			debug("v", v);
		}
		pseg=seg;
		debug();
	}

	void fall(){
		debug("fall");
		double c=-g/(2*v.x*v.x);
		double d=v.y/v.x+g*p.x/(v.x*v.x);
		double e=p.y-v.y/v.x*p.x-g*p.x*p.x/(2*v.x*v.x);

		c=-g/(2*sq(v.x));
		d=(v.x*v.y+g*p.x)/sq(v.x);
		e=(2*sq(v.x)*p.y-2*v.x*v.y*p.x-g*sq(p.x))/(2*sq(v.x));

		debug("c", c, "d", d, "e", e);

		double tMin=Double.MAX_VALUE;
		seg=-1;
		for(int i=0; i<n; i++){
			debug("i", i);
			double x1=xs1[i], y1=ys1[i], x2=xs2[i], y2=ys2[i];
			double a=(y2-y1)/(x2-x1);
			double b=(x2*y1-x1*y2)/(x2-x1);
			debug("a", a, "b", b);
			if(false){
				P[] ins=intersection(a, b, c, d, e);
				for(P in : ins){
					double t=(in.x-p.x)/v.x;
					debug("intersection", in);
					debug("t", t);
					if(t>EPS&&between(in.x, x1, x2)&&between(in.y, y1, y2)){
						if(t<tMin&&i!=pseg){
							tMin=t;
							seg=i;
						}
					}
				}
			}
			for(double t : quad(-g/2, v.y-a*v.x, p.y-a*p.x-b)){
				debug("t", t);
				double x=v.x*t+p.x;
				double y=-g/2*t*t+v.y*t+p.y;
				// if(t>EPS&&between(x, x1, x2)&&between(y, y1, y2)){
				if(t>EPS&&between(x, x1, x2)&&between(y, y1, y2)){
					if(t<tMin/* &&i!=pseg */){
						tMin=t;
						seg=i;
					}
				}
			}
		}
		debug("seg", seg);
		debug("tMin", tMin);
		if(seg==-1){
			debug("finished");
			if(false){
				for(double x : quad(c, d, e)){
					double t=(x-p.x)/v.x;
					if(t>EPS){
						debug("x", x, "t", t);
						// ans=x;
					}
				}
			}
			// hoge
			for(double t : quad(-g/2, v.y, p.y)){
				double x=v.x*t+p.x;
				// if(t>EPS){
				if(t+EPS>EPS){
					debug("x", x, "t", t);
					ans=x;
				}
			}
			double[] ts=quad(-g/2, v.y, p.y);
			ans=v.x*max(ts[0], ts[1])+p.x;
			// debug("quad", quad(-g/2, v.y, p.y));
			finished=true;
		}else{
			debug("update");
			p=p.add(a.div(2).mul(sq(tMin))).add(v.mul(tMin));
			v=v.add(a.mul(tMin));
			debug("p", p);
			debug("v", v);
		}
		pseg=seg;
		debug();
	}

	void slope(){
		debug("slope");
		P p1=new P(xs1[seg], ys1[seg]);
		P p2=new P(xs2[seg], ys2[seg]);

		P ex=p2.sub(p1);
		ex=ex.div(ex.abs());
		P ey=ex.rot90();
		debug("ex", ex);
		debug("ey", ey);

		P q1=inv(ex, ey, p1);
		P q2=inv(ex, ey, p2);
		P q=inv(ex, ey, p);
		P u=inv(ex, ey, v);
		P b=inv(ex, ey, a);
		debug("q", q);
		debug("u", u);
		debug("b", b);
		debug("q1", q1);
		debug("q2", q2);

		if(u.y+EPS>0){
			// if(v.y>EPS){
			debug("down");
			u.y=b.y=0;
		}else{
			debug("roll");
			u.y=b.y=0;

			double tMin=Double.MAX_VALUE;
			for(P qq : new P[]{q1, q2}){
				double[] ts=quad(b.x/2, u.x, q.x-qq.x);
				debug("ts", ts);
				for(double t : ts){
					if(t>EPS){
						tMin=min(tMin, t);
					}
				}
			}
			debug("tMin", tMin);
			q=q.add(b.div(2).mul(sq(tMin))).add(u.mul(tMin));
			u=u.add(b.mul(tMin));
			debug("q2", q);
			debug("u2", u);
		}
		p=mul(ex, ey, q);
		v=mul(ex, ey, u);
		debug("p", p);
		debug("v", v);
		debug();
	}

	double sq(double x){
		return x*x;
	}

	// b=[A1 A2]x
	P mul(P A1, P A2, P x){
		P b=new P(0, 0);
		b.x=A1.x*x.x+A2.x*x.y;
		b.y=A1.y*x.x+A2.y*x.y;
		return b;
	}

	// [A1 A2]x = b
	P inv(P A1, P A2, P b){
		double det=A1.det(A2);
		P x=new P(0, 0);
		x.x=A2.y*b.x-A2.x*b.y;
		x.y=-A1.y*b.x+A1.x*b.y;
		// return x.div(det);
		return x.mul(-1);
	}

	boolean between(double val, double a, double b){
		return min(a, b)+EPS<val&&val+EPS<max(a, b);
	}

	// y=ax+b
	// y=cx^2+dx+e
	P[] intersection(double a, double b, double c, double d, double e){
		double[] xs=quad(c, d-a, e-b);
		P[] ps=new P[xs.length];
		for(int i=0; i<xs.length; i++){
			double y1=a*xs[i]+b;
			double y2=c*sq(xs[i])+d*xs[i]+e;
			ps[i]=new P(xs[i], a*xs[i]+b);
			// ps[i]=new P(xs[i], (y1+y2)/2);
		}
		return ps;
	}

	double[] quad(double a, double b, double c){
		double D=b*b-4*a*c;
		if(D<0){
			return new double[0];
		}else{
			double E=1e-6;
			double sD=sqrt(max(D, 0));
			double x1, x2;
			if(abs(-b+sD)<E){
				x1=(2*c)/(-b-sD);
			}else{
				x1=(-b+sD)/(2*a);
			}
			if(abs(-b-sD)<E){
				x2=(2*c)/(-b+sD);
			}else{
				x2=(-b-sD)/(2*a);
			}
			return new double[]{x1, x2};
		}
	}

	class P{
		double x, y;

		P(double x, double y){
			this.x=x;
			this.y=y;
		}

		P add(P p){
			return new P(x+p.x, y+p.y);
		}

		P sub(P p){
			return new P(x-p.x, y-p.y);
		}

		P mul(double m){
			return new P(x*m, y*m);
		}

		P div(double d){
			return new P(x/d, y/d);
		}

		double abs(){
			return sqrt(abs2());
		}

		double abs2(){
			return x*x+y*y;
		}

		double arg(){
			return atan2(y, x);
		}

		double dot(P p){
			return x*p.x+y*p.y;
		}

		double det(P p){
			return x*p.y-y*p.x;
		}

		double ang(P p){
			return atan2(det(p), dot(p));
		}

		P rot90(){
			return new P(y, -x);
		}

		P rot(double d){
			return new P(cos(d)*x-sin(d)*y, sin(d)*x+cos(d)*y);
		}

		public String toString(){
			return "("+x+","+y+")";
		}
	}

	void debug(Object... os){
	//	 System.err.println(deepToString(os));
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
