import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

//Village
public class Main{

	double[] x, y;
	
	void run(){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		double R = sc.nextDouble(), EPS = 1e-8;
		double cos1 = Math.cos(1*Math.PI/180), sin1 = Math.sin(1*Math.PI/180);
		x = new double[n]; y = new double[n];
		for(int i=0;i<n;i++){
			double X = sc.nextDouble(), Y = sc.nextDouble();
			x[i] = cos1*X-sin1*Y;
			y[i] = sin1*X+cos1*Y;
		}
		Integer[] V = new Integer[n];
		for(int i=0;i<n;i++)V[i]=i;
		Arrays.sort(V, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return (int)Math.signum(x[o1]-x[o2]);
			}
		});
		int[] v = new int[n];
		for(int i=0;i<n;i++)v[i]=V[i];
		int res = 0;
		boolean[] u = new boolean[n];
		for(int i=0;i<n;i++){
			int idx = v[i];
			if(u[idx])continue;
			u[idx] = true;
			res++;
			double lx = x[idx]-R-EPS, rx = x[idx]+R+EPS;
			int l = 0, r = n;
			while(r-l>1){
				int m = (l+r)/2;
				if(lx<=x[v[m]])r=m;
				else l=m;
			}
			int k1 = l;
			l = 0; r = n;
			while(r-l>1){
				int m = (l+r)/2;
				if(rx<x[v[m]])r=m;
				else l=m;
			}
			int k2 = r;
			if(k2==r)k2--;
			for(int j=k1;j<=k2;j++){
				int k = v[j];
				if(u[k])continue;
				if(Math.sqrt((x[idx]-x[k])*(x[idx]-x[k]) + (y[idx]-y[k])*(y[idx]-y[k])) < R+EPS){
					u[k] = true;
				}
			}
		}
		System.out.println(res);
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
