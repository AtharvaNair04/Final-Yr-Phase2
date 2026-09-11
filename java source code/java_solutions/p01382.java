import java.util.Arrays;
import java.util.Scanner;

//Programming Contest Challenge Book
public class Main{

	int n;
	long res;
	long[] a;
	boolean[] u;
	
	void first(int k){
		u[k] = true;
		for(int i=k-1;i>=0&&2*a[i]>a[k];i--){
			long best = 0;
			for(int j=k;j>=k-5&&j>=0;j--)best+=a[j];
			if(best<=res)break;
			u[i] = true;
			for(int j=i-1;j>=0&&a[i]+a[j]>a[k];j--){
				u[j] = true;
				int f = k-1;
				long r = -1;
				while(f>=2&&r==-1&&res < a[k]+a[i]+a[j]+3*a[f]){
					if(!u[f])r = second(f);
					f--;
				}
				if(r!=-1)res = Math.max(res, a[i]+a[j]+a[k]+r);
				u[j] = false;
			}
			u[i] = false;
		}
		u[k] = false;
	}
	
	long second(int k){
		long sum = 0;
		int c = 0;
		for(int i=k-1;i>=0&&c<2;i--)if(!u[i]){
			c++; sum+=a[i];
		}
		return c<2 || sum<=a[k]?-1:(sum+a[k]);
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		a = new long[n];
		u = new boolean[n];
		res = 0;
		for(int i=0;i<n;i++)a[i]=sc.nextLong();
		Arrays.sort(a);
		for(int i=n-1;i>4;i--)first(i);
		System.out.println(res);
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
