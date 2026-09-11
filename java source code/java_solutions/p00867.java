import java.util.*;
import java.awt.geom.*;

public class Main{

	double EPS = 1.0e-08;

	private void doit() {
		Scanner sc = new Scanner(System.in);
		while(true){
			int n = sc.nextInt();
			if(n == 0) break;
			Line2D [] data = new Line2D[n];
			for(int i = 0; i < n; i++){
				Point2D p1 = new Point2D.Double(sc.nextInt(), sc.nextInt());
				Point2D p2 = new Point2D.Double(sc.nextInt(), sc.nextInt());
				data[i] = new Line2D.Double(p1, p2);
			}
			UnionFind uf = new UnionFind(n);
			for(int i = 0; i < n; i++){
				for(int j = i + 1; j < n; j++){
					if(data[i].intersectsLine(data[j])){
						uf.unite(i, j);
					}
				}
			}
			ArrayList<ArrayList<Line2D>> digits = new ArrayList<ArrayList<Line2D>>();
			HashMap<Integer, Integer> toind = new HashMap<Integer, Integer>();
			for(int i = 0; i < n; i++){
				int res = uf.find(i);
				if(toind.containsKey(res)){
				}
				else{
					digits.add(new ArrayList<Line2D>());
					toind.put(res, toind.size());
				}
				digits.get(toind.get(res)).add(data[i]);
			}

			int [] ans = new int[10];
			for(int i = 0; i < digits.size(); i++){
				int res  = solve(digits.get(i));
				//if(res == -1) continue;
				ans[res]++;
			}

			for(int i = 0; i < 9; i++){
				System.out.print(ans[i] + " ");
			}
			System.out.println(ans[9]);
		}
	}

	private int solve(ArrayList<Line2D> list) {
		int size = list.size();
		if(size == 1){
			return 1;
		}
		if(size == 3){
			//4 or 7
			for(int i = 0; i < size; i++){
				for(int j = i + 1; j < size; j++){
					Line2D l1 = list.get(i), l2 = list.get(j);

					if(l1.intersectsLine(l2)){
						Point2D p = intersectPtSS(l1, l2);
						boolean cand =
								((p.distance(l1.getP1()) < EPS || p.distance(l1.getP2()) < EPS) &&
										(p.distance(l2.getP1()) < EPS || p.distance(l2.getP2()) < EPS));
						if(! cand){
							return 4;
						}
					}
				}
			}
			return 7;
		}
		if(size == 4){
			//0,3,9
			int count = 0;
			for(int i = 0; i < size; i++){
				for(int j = 0; j < size; j++){
					if(i == j) continue;
					Line2D l1 = list.get(i), l2 = list.get(j);
					if(l1.intersectsLine(l2)){
						count++;
					}
				}
			}
			if(count == 6){
				return 3;
			}

			for(int i = 0; i < size; i++){
				for(int j = i + 1; j < size; j++){
					Line2D l1 = list.get(i), l2 = list.get(j);

					if(l1.intersectsLine(l2)){
						Point2D p = intersectPtSS(l1, l2);
						boolean cand =
								((p.distance(l1.getP1()) < EPS || p.distance(l1.getP2()) < EPS) &&
										(p.distance(l2.getP1()) < EPS || p.distance(l2.getP2()) < EPS));
						if(! cand){
							return 9;
						}
					}
				}
			}
			return 0;
		}
		else{
			//2, 5, 8
			int count = 0;
			for(int i = 0; i < size; i++){
				for(int j = 0; j < size; j++){
					if(i == j) continue;
					Line2D l1 = list.get(i), l2 = list.get(j);
					if(l1.intersectsLine(l2)){
						count++;
					}
				}
			}
			if(count == 12){
				return 8;
			}
			if(count == 10){
				return 6;
			}
			for(int i = 0; i < size; i++){
				int c = 0;
				for(int j = 0; j < size; j++){
					if(i == j) continue;
					Line2D l1 = list.get(i), l2 = list.get(j);

					if(l1.intersectsLine(l2)){
						c++;
					}
				}
				if(c == 1){
					Line2D now = list.get(i);
					for(int j = 0; j < size; j++){
						if(i == j) continue;
						if(now.intersectsLine(list.get(j))){
							Point2D p1 = list.get(j).getP1();
							Point2D p2 = list.get(j).getP2();
							if(now.ptLineDist(p1) > EPS){
								Point2D temp = p1;
								p1 = p2;
								p2 = temp;
							}

							int res = ccw2(p1, p2, now.getP1());
							if(res == 0){
								res = ccw2(p1, p2, now.getP2());
							}
							if(res > 0){
								return 5;
							}
							else{
								return 2;
							}
						}
					}
				}
			}
			return -1;
		}
	}

	class UnionFind{
		int [] par, rank;
		int num;
		public UnionFind(int n) {
			par = new int[n];
			rank = new int[n];
			for(int i = 0 ; i < n; i++){
				par[i] = i;
				//rank[i] = 0;
			}
			num = n;
		}
		int find(int x){
			if(par[x] == x)return x;
			else return par[x] = find(par[x]);
		}
		void unite(int x, int y){
			x = find(x);
			y = find(y);
			if(x == y) return ;
			if(rank[x] < rank[y]) par[x] = y;
			else{
				par[y] = x;
				if(rank[x] == rank[y]) rank[x]++;
			}
			num--;
		}
		boolean same(int x, int y){
			return find(x) == find(y);
		}
	}

	private Point2D intersectPtSS(Line2D l, Line2D m) {
		Point2D lVec = sub(l.getP2(), l.getP1());
		Point2D mVec = sub(m.getP2(), m.getP1());
		Point2D m1l1Vec = sub(m.getP1(), l.getP1());
		double a = cross(m1l1Vec, lVec);
		double b = cross(lVec, mVec);
		if(Math.abs(a) < EPS && Math.abs(b) < EPS){
			//平行な直線同士の場合
			if(l.getP1().distance(m.getP1()) < EPS) return l.getP1();
			if(l.getP1().distance(m.getP2()) < EPS) return l.getP1();
			return l.getP2();
		}
		double t = a / b;
		double resx = m.getX1() + t * mVec.getX();
		double resy = m.getY1() + t * mVec.getY();
		return new Point2D.Double(resx, resy);
	}

	private double cross(Point2D p1, Point2D p2) {
		double res = p1.getX() * p2.getY() - p1.getY() * p2.getX();
		return res;
	}

	private Point2D sub(Point2D p2, Point2D p1) {
		double x = p2.getX() - p1.getX();
		double y = p2.getY() - p1.getY();
		return new Point2D.Double(x, y);
	}

	private int ccw2(Point2D p1, Point2D p2, Point2D p3){
		Point2D p3p1 = sub(p3, p1);
		Point2D p3p2 = sub(p3, p2);
		double res = cross(p3p1, p3p2);
		if(Math.abs(res) < EPS) return 0;
		else if(res < EPS) return -1;
		else return 1;
	}

	public static void main(String[] args) {
		new Main().doit();
	}
}
