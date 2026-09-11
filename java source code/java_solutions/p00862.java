import java.util.*;
import java.awt.geom.*;
public class Main {
	double EPS = 1.0e-08;

	//1824 start
	//1900 end
	//0008 start
	//0406 cording end
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			int n = sc.nextInt();
			if(n == 0) break;
			//Point2D [] plist = new Point2D[n];
			ArrayList<Point2D> plist = new ArrayList<Point2D>();
			for(int i = 0; i < n; i++){
				plist.add(new Point2D.Double(sc.nextDouble(), sc.nextDouble()));
			}
			
			double low = 0.0, high = 10000 * 2;
			while(high - low > 1.0e-08){
				double mid = (high + low) / 2;
				//Point2D [] work = plist.clone();
				ArrayList<Point2D> work = new ArrayList<Point2D>();
				work.addAll(plist);
				
				//create cut line
				Line2D [] cutLine = new Line2D[n];
				for(int i = 0; i < n; i++){
					Point2D p1 = work.get(i), p2 = work.get((i+1) % n);
					cutLine[i] = getcutV(p1,p2, mid);
				}
				
				//polygon cutする
				for(Line2D cut: cutLine){
					work = polygonCut(work, cut);
				}
				if(work.size() < 1){
					//over 
					high = mid;
				}
				else{
					low = mid;
				}
			}
			System.out.printf("%.6f\n",high);
		}
		
		
	}

	private ArrayList<Point2D> polygonCut(ArrayList<Point2D> plist, Line2D cut) {
		int n = plist.size();
		ArrayList<Point2D> ans = new ArrayList<Point2D>();
		for(int i =0; i<n; i++){
			Point2D from = plist.get(i), to = plist.get((i+1)%n);
			if(cut.relativeCCW(from) < 0){
				ans.add(from);
			}
			int temp1 = cut.relativeCCW(from);
			int temp2 = cut.relativeCCW(to);
			if(temp1 * temp2 < 0){
				Point2D IntersectP = intersectPt(cut, new Line2D.Double(from,to));
				ans.add(IntersectP);
			}
		}
		return ans;
	}

	private Point2D intersectPt(Line2D l, Line2D m) {
		Point2D lVec = getV(l.getP2(), l.getP1());
		Point2D mVec = getV(m.getP2(), m.getP1());
		Point2D m1l1Vec = getV(m.getP1(), l.getP1());
		double a = getCross(m1l1Vec, lVec);
		double b = getCross(lVec, mVec);
		if(Math.abs(a) < EPS && Math.abs(b) < EPS){
			return l.getP1();
		}
		double t = a / b;
		double resx = m.getX1() + t * mVec.getX();
		double resy = m.getY1() + t * mVec.getY();
		return new Point2D.Double(resx, resy);
	}

	private double getCross(Point2D p1, Point2D p2) {
		double res = p1.getX() * p2.getY() - p1.getY() * p2.getX();
		return res;
	}

	private Line2D getcutV(Point2D p1, Point2D p2, double mid) {
		Point2D p2p1 = getV(p2, p1);
		double p1p2Dis = p1.distance(p2);
		Point2D p2p1NV = normalVector(p2p1);
		Point2D cutV = mul(p2p1NV, mid / p1p2Dis);
		Point2D cutp1 = add(p1, cutV);
		Point2D cutp2 = add(p2, cutV);
		Line2D res = new Line2D.Double(cutp1, cutp2);
		return res;
	}

	private Point2D add(Point2D p1, Point2D p2) {
		double x = p2.getX() + p1.getX();
		double y = p2.getY() + p1.getY();
		return new Point2D.Double(x, y);
	}

	private Point2D normalVector(Point2D p) {
		return new Point2D.Double(-p.getY(), p.getX());
	}
	
	private Point2D mul(Point2D p,double value) {
		return new Point2D.Double(p.getX() * value, p.getY() * value);
	}

	private Point2D getV(Point2D p2, Point2D p1) {
		double x = p2.getX() - p1.getX();
		double y = p2.getY() - p1.getY();
		return new Point2D.Double(x, y);
	}

	public static void main(String[] args) {
		Main obj = new Main();
		obj.doit();
	}
}
