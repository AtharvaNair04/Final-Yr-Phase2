import java.util.Scanner;

//Super Star
public class Main{

	double[] smallestSphere(double[][] p){
		int n = p.length;
		double[] pos = new double[3];
		for(int i=0;i<n;i++)for(int j=0;j<3;j++)pos[j]+=p[i][j];
		for(int j=0;j<3;j++)pos[j]/=n;
		double ratio = 0.5;
		while(ratio > 1e-8){
			for(int t=0;t<50;t++){
				double max = -1;
				int id = -1;
				for(int i=0;i<n;i++){
					double d = 0;
					for(int j=0;j<3;j++)d+=(pos[j]-p[i][j])*(pos[j]-p[i][j]);
					if(max < d){
						max = d; id = i;
					}
				}
				double dx = p[id][0]-pos[0], dy = p[id][1]-pos[1], dz = p[id][2]-pos[2];
				pos[0]+=dx*ratio;
				pos[1]+=dy*ratio;
				pos[2]+=dz*ratio;
			}
			ratio/=2;
		}
		return pos;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			int n = sc.nextInt();
			if(n==0)break;
			double[][] p = new double[n][3];
			for(int i=0;i<n;i++)for(int j=0;j<3;j++)p[i][j]=sc.nextDouble();
			double[] c = smallestSphere(p);
			double res = 0;
			for(int i=0;i<n;i++){
				double d = 0;
				for(int j=0;j<3;j++)d+=(c[j]-p[i][j])*(c[j]-p[i][j]);
				res = Math.max(res, d);
			}
			System.out.printf("%.8f\n", Math.sqrt(res));
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
