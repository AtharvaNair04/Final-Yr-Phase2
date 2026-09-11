import java.util.*;
import java.awt.geom.*;
public class Main {
	double EPS = 1.0e-08;
	int INF = 1 << 24;
	Comparator< Point2D > com = new Comparator< Point2D >() {
		public int compare(Point2D o1, Point2D o2) {
			if(o1.getX() < o2.getX()) return -1;
			else if(o1.getX() > o2.getX()) return 1;
			else if(o1.getY() < o2.getY()) return -1;
			else if(o1.getY() > o2.getY()) return 1;
			else return 0;
		}
	};
	HashMap<Point2D, Integer> map;
	ArrayList<Point2D> topoint;
	Point2D s,e;

	class C implements Comparable<C>{
		int ind;
		Point2D p;
		public C(int ind, Point2D p) {
			this.ind = ind;
			this.p = p;
		}
		@Override
		public int compareTo(C o) {
			if(this.p.getX() < o.p.getX()) return -1;
			else if(this.p.getX() > o.p.getX()) return 1;
			else if(this.p.getY() < o.p.getY()) return -1;
			else if(this.p.getY() > o.p.getY()) return 1;
			else return 0;
		}
	}

	class Open implements Comparable<Open>{
		int now;
		double cost;
		ArrayList<Point2D> list;

		public Open(int now, double cost, ArrayList<Point2D> list) {
			this.now = now;
			this.cost = cost;
			this.list = list;
		}

		@Override
		public int compareTo(Open o) {
			if(this.cost < o.cost) return -1;
			if(this.cost > o.cost) return 1;
			return 0;
		}

	}

	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			int n = sc.nextInt();
			if(n == 0) break;
			//System.out.println("n = " + n);
			s = new Point2D.Double(sc.nextInt(), sc.nextInt());
			e = new Point2D.Double(sc.nextInt(), sc.nextInt());
			Line2D [] line = new Line2D[n];
			for(int i = 0 ; i < n; i++){
				Point2D p1 = new Point2D.Double(sc.nextInt(), sc.nextInt());
				Point2D p2 = new Point2D.Double(sc.nextInt(), sc.nextInt());
				line[i] = new Line2D.Double(p1, p2);
			}
			//道路とsignを見分ける
			ArrayList<Line2D> road = new ArrayList<Line2D>();
			ArrayList<Line2D> sign = new ArrayList<Line2D>();
			for(int i = 0; i < line.length; i++){
				boolean a = false;
				boolean b = false;
				for(int j = 0; j < line.length; j++){
					if(i == j) continue;
					if(line[j].ptSegDist(line[i].getP1()) < EPS){
						a = true;
					}
					if(line[j].ptSegDist(line[i].getP2()) < EPS){
						b = true;
					}
					if(a && b) break;
				}
				if(a && b){
					road.add(line[i]);
				}
				else if(a || b){
					sign.add(line[i]);
				}
				else{

				}

			}

			Line2D [] newline = new Line2D[road.size()];
			for(int i = 0; i < road.size(); i++){
				newline[i] = road.get(i);
			}

			//線分アレンジメント
			double [][] pass = segmentArrangement(newline);

			//パスを修正する
			pass = modi(pass, sign);

			//ダイクストラ
			solve(pass,map.get(s), map.get(e));
		}
	}

	private double[][] modi(double[][] pass, ArrayList<Line2D> sign) {
		int len = pass.length;
		double [][] res = new double[len][len];
		for(int i = 0; i < len; i++){
			for(int j = 0; j < len; j++){
				res[i][j] = pass[i][j];
			}
		}
		for(int i = 0; i < len; i++){
			for(int j = i + 1; j < len; j++){
				if(res[i][j] >=INF) continue;
				Line2D l = new Line2D.Double(topoint.get(i), topoint.get(j));
				for(int k = 0; k < sign.size(); k++){
					Line2D m = sign.get(k);
					if(l.intersectsLine(m)){
						Point2D p1,p2;
						if(l.ptSegDist(m.getP1()) < EPS){
							p1 = m.getP1();
							p2 = m.getP2();
						}
						else{
							p1 = m.getP2();
							p2 = m.getP1();
						}
						Point2D p3 = sub(p2, p1);
						Point2D p4 = sub(l.getP1(), p1);
						double result = anglecos(p3,p4);
						if(result <= 0){
							res[i][j] = INF;
						}

						Point2D p5 = sub(l.getP2(), p1);
						double result2 = anglecos(p3,p5);

						if(result2 <= 0){
							res[j][i] = INF;
						}
					}
				}
			}
		}
		return res;
	}

	//ダイクストラ
	private void solve(double[][] pass, int start, int goal) {
		int n = pass.length;
		PriorityQueue<Open> open = new PriorityQueue<Open>();
		ArrayList<Point2D> temp = new ArrayList<Point2D>();
		temp.add(topoint.get(start));
		open.add(new Open(start, 0.0, temp));
		double [] close = new double[n];
		Arrays.fill(close, INF);
		close[start] = 0.0; 
		ArrayList<Point2D> ans = new ArrayList<Point2D>();
		while(! open.isEmpty()){
			Open now = open.poll();
			// ゴール判定
			if(now.now == goal) {
				ans.addAll(now.list);
				break;
			}
			for(int i = 0; i < n; i++){
				if(pass[now.now][i] >= INF) continue;
				if(i == now.now) continue;
				double nextcost = now.cost + pass[now.now][i];
				if(close[i] <= nextcost) continue;
				ArrayList<Point2D> next = new ArrayList<Point2D>();
				next.addAll(now.list);
				next.add(topoint.get(i));
				close[i] = nextcost;
				open.add(new Open(i, nextcost, next));
			}
		}
		if(ans.size() == 0){
			System.out.println(-1);
		}
		else{
			for(int i = 0; i < ans.size(); i++){
				int x = (int)ans.get(i).getX();
				int y = (int)ans.get(i).getY();
				System.out.println(x + " " + y);
			}
			System.out.println(0);
		}
	}

	private double[][] segmentArrangement(Line2D[] line) {
		//交点リストを求める
		ArrayList<Point2D> intersectionlist = new ArrayList<Point2D>();
		//スタート地点とゴール地点を追加
		intersectionlist.add(s);
		intersectionlist.add(e);
		for(int i = 0; i < line.length; i++){
			//intersectionlist.add(line[i].getP1());
			//intersectionlist.add(line[i].getP2());
			for(int j = i + 1; j < line.length; j++){
				if(line[i].intersectsLine(line[j])){
					intersectionlist.add(intersectPtSS(line[i], line[j]));
				}
			}
		}

		//sortする。重複削除
		Collections.sort(intersectionlist, com);
		for(int i = 1; i < intersectionlist.size(); i++){
			if(issame(intersectionlist.get(i-1), intersectionlist.get(i))){
				intersectionlist.remove(i);
				i--;
			}
		}
		topoint = new ArrayList<Point2D>();
		topoint.addAll(intersectionlist);

		//座標-> グラフ番号を求めるハッシュを生成する（ライブラリとはかんけいない）
		map = new HashMap<Point2D, Integer>();
		for(int i = 0; i < intersectionlist.size(); i++){
			map.put(intersectionlist.get(i), i);
		}

		//交点リストから、線分を通過するもの同士でグラフを生成する。
		int len = intersectionlist.size();
		double [][] res = new double[len][len];
		for(int i = 0 ; i< len; i++){
			Arrays.fill(res[i], INF);
			res[i][i] = 0.0;
		}
		for(int i = 0; i < line.length; i++){
			ArrayList<C> list = new ArrayList<C>();
			for(int j = 0; j < len; j++){
				if(line[i].ptSegDist(intersectionlist.get(j)) < EPS){
					list.add(new C(j, intersectionlist.get(j)));
				}
			}
			Collections.sort(list);
			for(int j = 1; j < list.size(); j++){
				int from = list.get(j-1).ind;
				int to = list.get(j).ind;
				res[from][to] = list.get(j).p.distance(list.get(j-1).p);
				res[to][from] = list.get(j).p.distance(list.get(j-1).p);
			}
		}

		return res;
	}

	private boolean issame(Point2D p1, Point2D p2) {
		if(Math.abs(p1.getX() - p2.getX()) < EPS && Math.abs(p1.getY() - p2.getY()) < EPS ){
			return true;
		}
		return false;
	}

	//線分と線分の交点
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
	private double dot(Point2D p1, Point2D p2){
		return p1.getX() * p2.getX() + p1.getY() * p2.getY();
	}

	private double anglecos(Point2D p1,Point2D p2){
		double a = dot(p1,p2);
		double b = norm(p1);
		double c = norm(p2);
		return a/(b*c);
	}
	private double norm(Point2D p){
		return Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
	}

	public static void main(String[] args) {
		Main obj = new Main();
		obj.doit();
	}
}
