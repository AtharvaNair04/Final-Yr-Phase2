import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main{
	public static Rope[] rope;
	public static int n;
	public static double EPS = 1e-8;
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		while(true){
			n = in.nextInt();
			if(n == 0) return;
			rope = new Rope[n];
			for(int i=0; i<n; i++){
				rope[i] = new Rope(in.nextInt(), in.nextInt(), in.nextInt());
			}
			System.out.println(binarySearch());
		}
	}
	
	public static double binarySearch(){
		double max = 300;
		double min = 1;
		double mid = 0;
		for(int i=0; i<100; i++){
			mid = (max+min)/2;
			if(check(mid)){
				min = mid;
			}else{
				max = mid;
			}
		}
		return mid;
	}
	
	public static boolean check(double h){
		for(int i=0; i<n; i++){
			if(!rope[i].setR(h)) return false;
		}
		Queue<Point> qu = new LinkedList<Point>();
		for(int i=0; i<n; i++){
			qu.add(new Point(rope[i].x, rope[i].y));
		}
		for(int i=0; i<n-1; i++){
			for(int j=i+1; j<n; j++){
				Point[] p = crossPoint(rope[i], rope[j]);
				qu.add(p[0]);
				qu.add(p[1]);
			}
		}
		out: while(!qu.isEmpty()){
			Point p = qu.poll();
			for(int i=0; i<n; i++){
				if(!cross(rope[i], p)) continue out;
			}
			return true;
		}
		return false;
	}
	
	public static Point[] crossPoint(Rope r1, Rope r2){
		double x1 = r2.x - r1.x;
		double y1 = r2.y - r1.y;
		double a = (x1*x1+y1*y1+r1.r*r1.r-r2.r*r2.r)/2.0;
		Point[] res = {
				new Point(culc(x1, y1, r1.r, a, true)+r1.x, culc(y1, x1, r1.r, a, false)+r1.y),
				new Point(culc(x1, y1, r1.r, a, false)+r1.x, culc(y1, x1, r1.r, a, true)+r1.y),
		};
		return res;
	}
	
	public static double culc(double x, double y, double r, double a, boolean sign){
		return (a*x + (sign?1:-1)*y*Math.sqrt((x*x+y*y)*r*r-a*a))/(x*x+y*y);
	}
	
	public static boolean cross(Rope rp, Point p){
		return dist2(rp.x, rp.y, p.x, p.y) <= rp.r*rp.r+EPS;
	}
	
	public static boolean cross(Rope r1, Rope r2){
		return dist2(r1.x, r1.y, r2.x, r2.y) <= (r1.r + r2.r)*(r1.r + r2.r);
	}
	
	public static double dist2(double x1, double y1, double x2, double y2){
		return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
	}
}

class Point{
	double x, y;
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
}

class Rope{
	int x, y, l;
	double r;
	public Rope(int x, int y, int l){
		this.x = x;
		this.y = y;
		this.l = l;
		r = 0;
	}
	
	public boolean setR(double h){
		if(h > l) return false;
		r =  Math.sqrt(l*l-h*h);
		return true;
	}
}
