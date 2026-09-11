import java.util.ArrayList;
import java.util.Scanner;

class Point {
	double EPS = 0.0000000001;
	double x; double y;
	public Point(double ax, double ay){x=ax;y=ay;}
	public Point sum(Point p) { return new Point(x+p.x,y+y); }
	public Point diff(Point p) { return new Point(x-p.x,y-p.y); }
	public Point mult(double k) { return new Point(x*k,y*k); }
	public Point div(double k) { return new Point(x*(1.0/k),y*(1.0/k)); }
	public boolean equals(Point p){return Math.abs(x-p.x)<EPS && Math.abs(y-p.y)<EPS;}
}
class Vector extends Point {
	public Vector(double ax, double ay) {super(ax,ay);}
	public Vector(Point p1, Point p2) {super(p1.x-p2.x,p2.x-p2.y);}
	public double norm() {return x*x+y*y;}
	public double abs() {return Math.sqrt(norm());}
	// TODO:
	public boolean isSmallerThan(Vector v) {return x!=v.x?x<v.x:y<v.y;}
	public double dotProduct(Vector v) {return x*v.x+y*v.y;}
	public double crossProduct(Vector v) {return x*v.y - y*v.x;}
}

class Segment {
	Point p1; Point p2;
	public Segment(Point ap1, Point ap2){p1=ap1;p2=ap2;}
	public double dotProduct(Segment s) {return (p1.x-p2.x)*(s.p1.x-s.p2.x)+(p1.y-p2.y)*(s.p1.y-s.p2.y);}
	public double crossProduct(Segment s) {return (p1.x-p2.x)*(s.p1.y-s.p2.y) - (p1.y-p2.y)*(s.p1.x-s.p2.x);}
}
class Line extends Segment{public Line(Point ap1, Point ap2){super(ap1,ap2);}}


public class Main{

	static double EPS = 0.0000000001;
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int q = scan.nextInt();
		for (int i=0;i<q;i++) {
			Line l1 = new Line(new Point(scan.nextDouble(),scan.nextDouble()), new Point(scan.nextDouble(),scan.nextDouble()));
			Line l2 = new Line(new Point(scan.nextDouble(),scan.nextDouble()), new Point(scan.nextDouble(),scan.nextDouble()));
			if (isOrthogonal(l1,l2)) {
				System.out.println(1);
			} else if (isParallel(l1,l2)) {
				System.out.println(2);
			} else {
				System.out.println(0);
			}
		}
		
	}
	static boolean equals(double d1, double d2){return Math.abs(d1-d2)<EPS;}
	boolean isOrthogonal(Vector v1, Vector v2) {return equals(v1.dotProduct(v2),0.0);}
	static boolean isOrthogonal(Segment s1, Segment s2) {return equals(s1.dotProduct(s2),0.0);}
	boolean isParallel(Vector v1, Vector v2) {return equals(v1.crossProduct(v2),0.0);}
	static boolean isParallel(Segment s1, Segment s2) {return equals(s1.crossProduct(s2),0.0);}
	
	class Circle {Point c;double r;Circle(Point ac, double ar){c=ac;ar=r;}}
	class Polygon extends ArrayList<Point>{}

}
