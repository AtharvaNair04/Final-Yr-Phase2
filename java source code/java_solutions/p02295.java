import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
public class Main {
	Scanner in = new Scanner(System.in);
	public static void main(String[] args) {
		new Main();
	}

	public Main() {
		int q = in.nextInt();
		for(int i=0;i<q;i++)new CGL_2B().doIt();
	}



	class CGL_2B{
		double cross(Point2D p1,Point2D p2){
			return p1.getX()*p2.getY()-p1.getY()*p2.getX();
		}
		//和
		Point2D sum(Point2D p1,Point2D p2){
			return new Point2D.Double(p1.getX()+p2.getX(),p1.getY()+p2.getY());
		}
		Point2D mul(double n,Point2D p1){
			return new Point2D.Double(p1.getX()*n,p1.getY()*n);
		}
		Point2D diff(Point2D p1,Point2D p2){
			return new Point2D.Double(p1.getX()-p2.getX(),p1.getY()-p2.getY());
		}
		Point2D intersectionPoint(Line2D l1,Line2D l2){
			return intersectionPoint(l1.getP1(), l1.getP2(), l2.getP1(), l2.getP2()); 
		}
		Point2D intersectionPoint(Point2D a1,Point2D a2,Point2D b1,Point2D b2){
			Point2D a=diff(a2,a1),
					b=diff(b2,b1);
			return sum(a1, mul(cross(b, diff(b1,a1))/cross(b,a), a));
		}
		void doIt(){
			Line2D l1 = new Line2D.Double(in.nextDouble(),in.nextDouble(),in.nextDouble(),in.nextDouble());;
			Line2D l2 = new Line2D.Double(in.nextDouble(),in.nextDouble(),in.nextDouble(),in.nextDouble());;
			Point2D p = intersectionPoint(l1, l2);
			System.out.printf("%.10f %.10f\n",p.getX(),p.getY());
		}
	}


}
