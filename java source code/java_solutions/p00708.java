import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	Scanner sc = new Scanner(System.in);
	public void run() {
		while(sc.hasNext()){
			int n = sc.nextInt();
			if(n == 0 )break;
			else calc(n);
			
		}
	}
	double xs[];
	double ys[];
	double zs[];
	double rs[];
	double distance[];
	int count;
	public void calc(int n){
		xs = new double[n];
		ys = new double[n];
		zs = new double[n];
		rs = new double[n];
		for(int i = 0; i < n; i++){
			xs[i] = sc.nextDouble();
			ys[i] = sc.nextDouble();
			zs[i] = sc.nextDouble();
			rs[i] = sc.nextDouble();			
		}
		
		distance = new double[n];
		Arrays.fill(distance, Double.MAX_VALUE);
		
		double ans = 0;
		
		distance[0] = -1;
		count = 1;	
		update(0, n);
		
		while(count < n){
			int small = -1;
			for(int k = 0; k < n; k++){
				if(distance[k] != -1 && (small == -1 || distance[k] < distance[small])){
					small = k;
				}
			}
			ans += distance[small];
			distance[small] = -1;
			count++;
			update(small, n);
		}
		System.out.printf("%.3f\n", ans);
	}
	
	public void update(int a, int n){
		for(int i = 0; i < n; i++){
			if(distance[i] != -1){
				double d = dist(a, i);
				if(d == 0){
					distance[i] = -1;
					count++;
					update(i, n);
				}
				else if(d < distance[i]) distance[i] = d;
			}
		}
	}

	public double dist(int a, int b){
		double d = Math.sqrt(
				  (xs[a]-xs[b]) * (xs[a]-xs[b]) +
				  (ys[a]-ys[b]) * (ys[a]-ys[b]) +
				  (zs[a]-zs[b]) * (zs[a]-zs[b])
				);
		d = d - rs[a] - rs[b];
		if(d < 0) d = 0;
		return d;
	}
	public static void main(String[] args) {
		new Main().run();
	}
}
