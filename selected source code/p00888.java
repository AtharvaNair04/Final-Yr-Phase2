import java.awt.geom.*;
import java.util.*;

public class Main {
	int INF = 1 << 24;
	final double EPS = 1.0e-08;
	int n;
	
	class State implements Comparable<State>{
		int now1, now2;
		double cost;
		public State(int now1, int now2, double cost) {
			this.now1 = now1;
			this.now2 = now2;
			this.cost = cost;
		}
		@Override
		public int compareTo(State o) {
			if(this.cost < o.cost ) return -1;
			if(this.cost > o.cost)	return 1;
			return 0;
		}
	}
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			n = sc.nextInt();
			if(n == 0) break;
			Point2D [] data = new Point2D[n];
			HashSet<Integer> ylist = new HashSet<Integer>();
			for(int i = 0 ; i < n; i ++){
				int x = sc.nextInt();
				int y = sc.nextInt();
				ylist.add(y);
				data[i] = new Point2D.Double(x,y);
			}
			int [] yylist = new int[ylist.size()];
			int ind = 0;
			for(int y: ylist){
				yylist[ind++] = y;
			}
			Arrays.sort(yylist);

			ArrayList<Point2D> plist = new ArrayList<Point2D>();
			plist.add(data[0]);
			for(int i = 1 ; i < n; i++){
				Line2D l1 = new Line2D.Double(data[i], data[i-1]);
				int a = angle(l1);
				if(a > 0){
					for(int j = 0 ; j < yylist.length; j++){
						int y = yylist[j];
						if(between(l1, y)){
							double x = getX(l1, y);
							plist.add(new Point2D.Double(x, y));
						}
					}
				}
				else if(a < 0){
					for(int j = yylist.length - 1; j >= 0; j--){
						int y = yylist[j];
						if(between(l1, y)){
							double x = getX(l1, y);
							plist.add(new Point2D.Double(x, y));
						}
					}
				}
				plist.add(data[i]);
			}
			double ans = solve(plist);
			System.out.printf("%f\n",ans);
		}
	}
	
	private double getX(Line2D l1, int y) {
		double x0 = l1.getX1();
		double x1 = l1.getX2();
		double y0 = l1.getY1();
		double y1 = l1.getY2();
		double res = x0 + (x1 - x0) * (y - y0) / (y1 - y0);
		return res;
	}

	private int angle(Line2D l1) {
		if(Math.abs(l1.getY1() - l1.getY2()) < EPS){
			return 0;
		}
		double a = (l1.getY2() - l1.getY1()) / (l1.getX2() - l1.getX1());
		if(a < -EPS){
			return -1;
		}
		return 1;
	}

	private boolean between(Line2D l1, int y) {
		if(l1.getY1() <= y && y <= l1.getY2()){
			return true;
		}
		if(l1.getY2() <= y && y <= l1.getY1()){
			return true;
		}
		return false;
	}

	private double solve(ArrayList<Point2D> plist) {
		double ans = INF;
		int len = plist.size();
		PriorityQueue<State> open = new PriorityQueue<State>();
		open.add(new State(0, len - 1, 0.0));
		boolean [][] close = new boolean[len][len];
		close[0][len-1] = true;
		while(! open.isEmpty()){
			State now = open.poll();
			if(now.now1 == now.now2){
				ans = Math.min(ans, now.cost);
				break;
			}
			
			for(int i = -1 ; i <= 1; i++){
				for(int j = -1; j <= 1; j++){
					int next1 = now.now1 + i;
					int next2 = now.now2 + j;
					if(next1 < 0 || next2 > len-1)continue;
					if(i == 0 && j == 0)continue;
					if(Math.abs(plist.get(next1).getY() - plist.get(next2).getY()) < EPS){
						double value = plist.get(now.now1).distance(plist.get(next1));
						double value2 = plist.get(now.now2).distance(plist.get(next2));
						double nextcost = now.cost + value + value2;
						if(close[next1][next2]) continue;
						close[next1][next2] = true;
						open.add(new State(next1, next2, nextcost));
					}
				}
			}
		}
		return ans;
	}

	public static void main(String [] args){
		new Main().doit();
	}
}
