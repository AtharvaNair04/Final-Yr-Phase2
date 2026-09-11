import java.util.Scanner;

public class Main {

	static double EPS = 0.0000000001;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Segment seg  =new Segment(new Point(sc.nextInt(),sc.nextInt()),new Point(sc.nextInt(),sc.nextInt()));

		int q=sc.nextInt();
		for (int i=0;i<q;i++) {
			Point p = reflect(seg,new Point(sc.nextInt(),sc.nextInt()));
			System.out.println(p.x+" "+p.y);
		}
	}
	static Point project(Segment s, Point p) {
		Vector base = s.p2.diff(s.p1);
		double r = p.diff(s.p1).dotProduct(base) / base.norm();
		return s.p1.sum(base.mult(r));
	}
	static Point reflect(Segment s, Point p) {
		return p.sum(project(s,p).diff(p).mult(2.0));
	}
}
class Point {
	static double EPS = 0.0000000001;
	double x; double y;
	public Point(double ax, double ay){x=ax;y=ay;}
	public Vector sum(Point p) { return new Vector(x+p.x,y+p.y); }
	public Vector diff(Point p) { return new Vector(x-p.x,y-p.y); }
	public Vector mult(double k) { return new Vector(x*k,y*k); }
	public Vector div(double k) { return new Vector(x*(1.0/k),y*(1.0/k)); }
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
