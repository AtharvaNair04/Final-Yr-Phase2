import java.util.Scanner;

//Lost in Space
public class Main{

	void run(){
		Scanner sc = new Scanner(System.in);
		int t = sc.nextInt();
		double RATIO = 1e-4;
		while(t--!=0){
			double qr = sc.nextDouble(), rp = sc.nextDouble(), pq = sc.nextDouble();
			double r1 = qr/rp, r2 = rp/pq, r3 = pq/qr;
			int n = sc.nextInt();
			double[] x = new double[n+1], y = new double[n+1], z = new double[n+1];
			for(int i=1;i<=n;i++){
				x[i] = sc.nextDouble(); y[i] = sc.nextDouble(); z[i] = sc.nextDouble();
			}
			for(int p=1;p<=n;p++)for(int q=1;q<=n;q++)for(int r=1;r<=n;r++){
				if(p==q||q==r||r==p)continue;
				double QR = Math.sqrt(Math.pow(x[q]-x[r], 2)+Math.pow(y[q]-y[r], 2)+Math.pow(z[q]-z[r], 2));
				double RP = Math.sqrt(Math.pow(x[p]-x[r], 2)+Math.pow(y[p]-y[r], 2)+Math.pow(z[p]-z[r], 2));
				double PQ = Math.sqrt(Math.pow(x[q]-x[p], 2)+Math.pow(y[q]-y[p], 2)+Math.pow(z[q]-z[p], 2));
				double R1 = QR/RP, R2 = RP/PQ, R3 = PQ/QR;
				double d1 = Math.abs(r1-R1), d2 = Math.abs(r2-R2), d3 = Math.abs(r3-R3);
				if(d1<RATIO && d2<RATIO && d3<RATIO){
					System.out.println(p+" "+q+" "+r);break;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
