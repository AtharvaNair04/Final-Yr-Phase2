import java.util.Scanner;

public class Main {
	static final double EPS = 1.0e-9;
	public static void main(String[] args) {
		doIt();
	}
	static void doIt() {
		Scanner sc = new Scanner(System.in);
		while(true){
			int n = sc.nextInt();
			if(n == 0) break;
			int[] x = new int[n];
			int[] y = new int[n];
			int[] v = new int[n];
			double xx = 0, yy = 0, d = Double.MAX_VALUE, cons = 10, rate = 0.98;
			for (int i = 0; i < n; i++) {
				x[i] = sc.nextInt();
				y[i] = sc.nextInt();
				v[i] = sc.nextInt();
				xx += x[i];
				yy += y[i];
			}
			xx /= n;
			yy /= n;
			double t = 0;
			int idx = 0;
			for (int i = 0; i < n; i++) {
				double tmp = Math.sqrt((xx - x[i])*(xx - x[i]) + (yy - y[i])*(yy - y[i])) / v[i];
				if(t < tmp){
					t = tmp;
					idx = i;
				}
			}
			d = t;
			while(true){
				//System.out.println(xx + " : " + yy + " : " + cons);
				t = 0;
				idx = 0;
				for (int i = 0; i < n; i++) {
					double tmp = Math.sqrt((xx - x[i])*(xx - x[i]) + (yy - y[i])*(yy - y[i])) / v[i];
					if(t < tmp){
						t = tmp;
						idx = i;
					}
				}
				double vv = cons / Math.sqrt((xx - x[idx])*(xx - x[idx]) + (yy - y[idx])*(yy - y[idx]));
				double txx = xx + (x[idx] - xx) * vv;
				double tyy = yy + (y[idx] - yy) * vv;
				if(Math.sqrt((txx - xx)*(txx - xx) + (tyy - yy)*(tyy - yy)) < EPS) break;
				t = 0;
				for (int i = 0; i < n; i++) {
					double tmp = Math.sqrt((txx - x[i])*(txx - x[i]) + (tyy - y[i])*(tyy - y[i])) / v[i];
					if(t < tmp) t = tmp;
				}
				//if(t < d){
					xx = txx;
					yy = tyy;
					d = t;
				//}
				cons *= rate;
			}
			System.out.println(d);
		}		
	}
}
