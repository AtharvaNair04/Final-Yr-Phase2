import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
	public static double EPS = 1e-9;
	public static void main(String[] args) throws IOException{
		Scanner in = new Scanner(System.in);
		while(true){
			int n = in.nextInt();
			if(n == 0) break;
			int sx = in.nextInt();
			int sy = in.nextInt();
			int ex = in.nextInt();
			int ey = in.nextInt();
			Pos s = new Pos(sx, sy);
			Pos e = new Pos(ex, ey);
			ArrayList<Pos> list = new ArrayList<Pos>();
			boolean zero = false;
			for(int i=0; i<n; i++){
				int lx = in.nextInt();
				int ly = in.nextInt();
				int rx = in.nextInt();
				int ry = in.nextInt();
				int h = in.nextInt();
				if(zero) continue;
				Pos[] p = {
						new Pos(lx, ly),
						new Pos(lx, ry),
						new Pos(rx, ry),
						new Pos(rx, ly),
				};
				double minLen = 100000000;
				double sign = 0;
				boolean inter = true;
				for(int j=0; j<4; j++){
					if(cross(p[j], p[(j+1)%4], s, e)){
						zero = true;
						break;
					}
					double l = Math.abs(dist(s, e, p[j]));
					l = Math.min(l, Math.abs(sign = dist(p[j], p[(j+1)%4], s)));
					l = Math.min(l, Math.abs(dist(p[j], p[(j+1)%4], e)));
					
					minLen = Math.min(minLen, l);
					if(sign > 0) inter = false;
				}
				if(inter || minLen < EPS) zero = true;
				list.add(new Pos(minLen, h));
				
			}
			if(zero){
				System.out.println(0);
				continue;
			}
			System.out.println(binarySearch(list));
			
			
		}
	}
	
	public static final int times = 5000;
	public static double binarySearch(ArrayList<Pos> list){
		double max = 1000;
		double min = 0;
		double mid = 0;
		for(int i=0; i<times; i++){
			mid = (max+min)/2;
			if(check(mid, list)){
				min = mid;
			}else{
				max = mid;
			}
		}
		return mid;
	}
	
	public static boolean check(double r, ArrayList<Pos> list){
		for(Pos p: list){
			double h = p.y;
			if(h > r) h = r;
			Pos o = new Pos(0, r);
			if(o.dist2(p, h) < r*r) return false;
		}
		return true;
	}
	
	public static double triarea(Pos a, Pos b, Pos c){
		double dx1 = b.x - a.x;
		double dy1 = b.y - a.y;
		double dx2 = c.x - a.x;
		double dy2 = c.y - a.y;
		return dx1*dy2 - dx2*dy1;
	}
	
	public static boolean cross(Pos a1, Pos a2, Pos b1, Pos b2){
		if(cross(a1.x-a2.x, a1.y-a2.y, b1.x-b2.x, b1.y-b2.y)==0
				&& cross(a1.x-a2.x, a1.y-a2.y, a1.x-b2.x, a1.y-b2.y)==0){
			if(Math.abs(a1.dist2(b1)+a2.dist2(b1) - a1.dist2(a2)) < EPS
					|| Math.abs(a1.dist2(b2)+a2.dist2(b2) - a1.dist2(a2)) < EPS){
				return true;
			}else return false;
		}
		return triarea(a1, a2, b1)*triarea(a1, a2, b2) <= 0
				&& triarea(b1, b2, a1)*triarea(b1, b2, a2) <= 0;
	}
	
	public static double cross(double x1, double y1, double x2, double y2){
		return x1*y2 - x2*y1;
	}
	
	public static double dist(Pos a, Pos b, Pos c){
		// a->b ??¨ c??¨????????¢
		if(dot(b.x-a.x, b.y-a.y, c.x-a.x, c.y-a.y) < EPS)
			return Math.sqrt(a.dist2(c));
		if(dot(a.x-b.x, a.y-b.y, c.x-b.x, c.y-b.y) < EPS)
			return Math.sqrt(b.dist2(c));
		return triarea(a, b, c)/Math.sqrt(a.dist2(b));
	}
	
	public static double dot(double x, double y, double x1, double y2){
		return x*x1 + y*y2;
	}
	
	public static double dot(Pos a, Pos b){
		return a.x*b.x + a.y*b.y;
	}
}

class Pos{
	double x, y;
	public Pos(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double dist2(Pos p){
		return (x-p.x)*(x-p.x) + (y-p.y)*(y-p.y);
	}
	
	public double dist2(Pos p, double h){
		return (x-p.x)*(x-p.x) + (y-h)*(y-h);
	}
}
//
//class Stage extends Drawable{
//	Pos[][] rect;
//	Pos[] use;
//	Pos s, e;
//	public Stage(Pos[][] rect, Pos s, Pos e, Pos[] use) {
//		this.rect = rect;
//		this.s = s;
//		this.e = e;
//		this.use = use;
//	}
//	
//	@Override
//	public void draw(Graphics g) {
//		g.setColor(Color.WHITE);
//		for(int i=0; i<rect.length; i++){
//			for(int j=0; j<rect[i].length; j++){
//				Pos p1 = rect[i][j];
//				Pos p2 = rect[i][(j+1)%rect[i].length];
////				g.fillOval(p(p1.x), p(p1.y), 5, 5);
//				g.drawLine(px(p1.x), py(p1.y), px(p2.x), py(p2.y));
//			}
//		}
//		g.drawLine(px(s.x), py(s.y), px(e.x), py(e.y));
//		g.setColor(Color.CYAN);
//		for(int i=0; i<use.length; i++){
//			Pos p1 = use[i];
//			g.fillOval(px(p1.x), py(p1.y), 2, 2);
//		}
//	}
//}
