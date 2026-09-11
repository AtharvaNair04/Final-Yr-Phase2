import java.util.Scanner;

public class Main {
	static int N;
	static double[] X, R;
	static boolean check(double y) {
		double l=-100000000;
		double r=100000000;
		for(int i=0; i<N; i++) {
			double a=X[i]-Math.sqrt(R[i]*R[i]-y*y);
			l=Math.max(l, a);
			double b=X[i]+Math.sqrt(R[i]*R[i]-y*y);
			r=Math.min(r, b);
		}
		return (l<r);
	}
	
	public static void main(String[] args) {
		try(Scanner sc = new Scanner(System.in)){
			N=sc.nextInt();
			double minx=0, maxx=1000000000;
			X=new double[N];
			R=new double[N];
			double a=0, b=0;
			double ra=0, rb=0;
			for(int i=0; i<N; i++) {
				double x=sc.nextDouble();
				double r=sc.nextDouble();
				X[i]=x;
				R[i]=r;
				
				maxx=Math.min(maxx, R[i]);
			}
			for(int i=0; i<100; i++) {
				double mid=(maxx+minx)/2.0;
				if(check(mid)) {
					minx=mid;
				}
				else {
					maxx=mid;
				}
			}
			System.out.println(minx);
			
			
		}
	}
}
