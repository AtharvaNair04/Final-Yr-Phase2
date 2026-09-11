import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;

import static java.lang.Math.*;

public class Main{

	int N;
	final int MESH = 1200;
	final double diameter = 2.0;
	final double EPS = 1e-13;
	Point[] ps;

	void run(){
		Scanner in = new Scanner(System.in);
		for(;;){
			N = in.nextInt();
			if(N==0) return ;
			ps = new Point[N];
			for(int i=0; i<N; i++){
				double x = in.nextDouble(), y = in.nextDouble();
				ps[i] = new Point(x,y);
			}
			solve();
		}
	}

	boolean inRange(double theta){
		return 0<=theta && theta<=PI;
	}

	double toRange(double theta){
		if(inRange(theta)){
			return theta;
		}
		else if(inRange(theta + PI)){
			return theta + PI;
		}
		else if(inRange(theta - PI)){
			return theta - PI;
		}
		else if(inRange(theta - 2*PI)){
			return theta - 2*PI;
		}
		else if(inRange(theta + 2*PI)){
			return theta + 2*PI;
		}
		return 0.0;
	}

	double[] findThreshold(){
		HashSet<Double> threshold = new HashSet<Double>();
		threshold.add(0.0);
		threshold.add(PI/2);
		threshold.add(PI);
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++)if(i!=j){
				double dy = ps[i].y - ps[j].y, dx = ps[i].x - ps[j].x;
				threshold.add(toRange(atan(dx/dy)));
				threshold.add(toRange(atan(-dx/dy)));
				threshold.add(toRange(atan(dy/dx)));
				threshold.add(toRange(atan(-dy/dx)));
			}
		}
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++)if(i!=j){
				double dy = ps[i].y - ps[j].y, dx = ps[i].x - ps[j].x;
				double a = hypot(dy, dx);
				if(a < diameter) continue;
				else{
					threshold.add(toRange(acos(diameter/a) + atan2(dy,dx)));
					threshold.add(toRange(acos(diameter/a) - atan2(dy,dx)));
					threshold.add(toRange(acos(diameter/a) + atan2(dx,dy)));
					threshold.add(toRange(acos(diameter/a) - atan2(dx,dy)));
					threshold.add(toRange(acos(-diameter/a) + atan2(dy,dx)));
					threshold.add(toRange(acos(-diameter/a) - atan2(dy,dx)));
					threshold.add(toRange(acos(-diameter/a) + atan2(dx,dy)));
					threshold.add(toRange(acos(-diameter/a) - atan2(dx,dy)));
				}
			}
		}
		Double[] tmp = threshold.toArray(new Double[0]);
		double[] tmpThreshold = new double[tmp.length];
		for(int i=0; i<tmp.length; i++){
			tmpThreshold[i] = tmp[i];
		}
		Arrays.sort(tmpThreshold);
		for(int i=0; i<tmpThreshold.length-1; i++){
			double theta = 0.5*(tmpThreshold[i+1] + tmpThreshold[i]);
			Pair[] vs = new Pair[N];
			for(int j=0; j<N; j++){
				vs[j] = new Pair(ps[j].x*sin(theta) + ps[j].y*cos(theta), j);
			}
			Arrays.sort(vs, new Cmp());
			double xx = 0, yy = 0;
			for(int j=1; j<N; j++){
				if(vs[j-1].value + diameter > vs[j].value){
					xx += ps[vs[j].index].x - ps[vs[j-1].index].x;
					yy += ps[vs[j].index].y - ps[vs[j-1].index].y;
				}
			}
			threshold.add(toRange(atan2(yy,xx)));
			threshold.add(toRange(atan2(xx,yy)));
			threshold.add(toRange(atan2(-yy,xx)));
			threshold.add(toRange(atan2(-xx,yy)));
		}
		if(false)for(int i=1; i<=30; i++){
			for(int j=1; j<=30; j++){
				threshold.add(atan2(i,j));
				threshold.add(toRange(atan2(-i,j)));
			}
		}
		tmp = threshold.toArray(new Double[0]);
		tmpThreshold = new double[tmp.length];
		for(int i=0; i<tmp.length; i++){
			tmpThreshold[i] = tmp[i];
		}
		Arrays.sort(tmpThreshold);
		return tmpThreshold;
	}

	double calc(double theta){
		double[] xs = new double[N];
		for(int i=0; i<N; i++){
			xs[i] = ps[i].x*sin(theta) + ps[i].y*cos(theta);
		}
		return rangeMerge(xs);
	}

	double rangeMerge(double[] xs){
		Arrays.sort(xs);
		double ret = diameter;
		for(int i=0; i<N-1; i++){
			ret += diameter - max(0.0, (xs[i] + diameter) - xs[i+1]);
		}
		return ret;
	}

	void solve(){
		double[] threshold = findThreshold();
		double minValue = Double.MAX_VALUE, maxValue = -Double.MAX_VALUE;
		double minAns = -1, maxAns = -1;
		for(double th:threshold){
			double result = calc(th);
			if(result > maxValue){
				maxValue = result;
				maxAns = th;
			}
			if(result < minValue){
				minValue = result;
				minAns = th;
			}
		}
		System.out.printf("%.13f\n", minAns + EPS);
		System.out.printf("%.13f\n", maxAns + EPS);
	}

	class Point{
		double x, y;
		Point(double _x, double _y){
			this.x = _x; this.y = _y;
		}
	}

	class Pair{
		double value;
		int index;
		Pair(double v, int i){
			this.value = v;
			this.index = i;
		}
	}

	class Cmp implements Comparator<Pair>{
		public int compare(Pair a, Pair b){
			double dif = a.value - b.value;
			return dif>0?1:dif<0?-1:0;
		}
	}

	public static void main(String args[]){
		new Main().run();
	}
}
