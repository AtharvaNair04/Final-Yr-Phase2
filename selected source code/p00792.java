import java.awt.geom.*;
import java.util.*;

import static java.lang.Math.*;
public class Main {
	final Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		new Main();
	}
	Main(){
		new AOJ1212();
	}
	
	class AOJ1212{
		AOJ1212(){
			while(true){
				N=sc.nextInt();
				if(N<0)	break;
				solve();
			}
		}
		int N;
		final double EPS=1.0e-4;
		void solve(){
			Line2D[] mirrors = new Line2D[N];
			for(int i=0; i<N; ++i){
				String d = sc.next();
				int x=sc.nextInt(), y=sc.nextInt();
				mirrors[i]=new Line2D.Double(x, y, x+(d.equals("x")? 1: 0), y+(d.equals("y")? 1: 0));
			}
			Line2D[] walls = new Line2D[4];
			walls[0]=new Line2D.Double(0,0,8,0);
			walls[1]=new Line2D.Double(8,0,8,8);
			walls[2]=new Line2D.Double(8,8,0,8);
			walls[3]=new Line2D.Double(0,8,0,0);
			
			Point2D start = new Point2D.Double(0.75,0.25),
					ans=start;
			Line2D l=new Line2D.Double(0.75, 0.25, 0.75+100, 0.25+100);
			
			Point2D[] vv= {new Point2D.Double(100,100), new Point2D.Double(-100,100),
						new Point2D.Double(100,-100), new Point2D.Double(-100,-100)};
			
			int cnt=0,v=0;
			boolean fin=false;
			while(!fin){
				
				boolean bs = cnt>0 && l.ptSegDist(start)<EPS;
				++cnt;
				
				double dis = 100;
				Point2D p=null;
				int idx=-1;
				for(int i=0; i<N; ++i){
					if(l.intersectsLine(mirrors[i])){
						Point2D b = intersectionPoint(l, mirrors[i]);
						double a = b.distance(l.getP1());
						if(a < dis && a>EPS){
							dis = a;
							p=b;
							idx=i;
						}
					}
				}
				
//				System.out.println("P:"+p+" L:"+l.getP1()+","+l.getP2());
				
				if(p==null){
					if(bs)	break;
					for(int i=0; i<4; ++i){
						if(l.intersectsLine(walls[i])){
							Point2D tmp = intersectionPoint(l, walls[i]);
							if(tmp.distance(l.getP1()) > EPS){
								ans=tmp;
								fin=true;
								break;
							}
						}
					}
				}else{
					if(bs){
						double tmp = start.distance(l.getP1());
						if(tmp < dis)	break;
					}
					
					if(mirrors[idx].getX1()==mirrors[idx].getX2()){
						v = v^1;
					}else{
						v = v^2;
					}
					double px = p.getX()*100+EPS, py = p.getY()*100+EPS;
					p = new Point2D.Double((int)px/100.0, (int)py/100.0);
					l=new Line2D.Double(p, sum(p, vv[v]));
					
				}
			}
			
			System.out.println((int)(ans.getX()*100+EPS) +" "+ (int)(ans.getY()*100+EPS));
			
		}
		Point2D intersectionPoint(Line2D l1,Line2D l2){
			return intersectionPoint(l1.getP1(), l1.getP2(), l2.getP1(), l2.getP2());
		}
		Point2D intersectionPoint(Point2D a1,Point2D a2,Point2D b1,Point2D b2){
			Point2D a=diff(a2,a1),
					b=diff(b2,b1);
			return sum(a1, mul(cross(b, diff(b1,a1))/cross(b,a), a));
		}
		Point2D sum(Point2D p1,Point2D p2){
			return new Point2D.Double(p1.getX()+p2.getX(), p1.getY()+p2.getY());
		}
		Point2D diff(Point2D p1,Point2D p2){
			return new Point2D.Double(p1.getX()-p2.getX(), p1.getY()-p2.getY());
		}
		Point2D mul(double n,Point2D p1){
			return new Point2D.Double(p1.getX()*n, p1.getY()*n);
		}
		double cross(Point2D p1,Point2D p2){
			return p1.getX()*p2.getY() - p2.getX()*p1.getY();
		}
	}
}
