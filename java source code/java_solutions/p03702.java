import java.util.*;

public class Main {
	static int N,A,B;
	static int[] h;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		A = sc.nextInt();
		B = sc.nextInt();
		h = new int[N];
		for(int i=0; i<N; i++)
			h[i] = sc.nextInt();
		
		int min = 0;
		int max = 1<<30;
		while(min+1<max) {
			int mid = (min + max)/2;
			if(ok(mid))
				max = mid;
			else
				min = mid;
		}
		
		System.out.println(max);
		sc.close();
	}
	
	static boolean ok(int x) {
		long ex = 0;
		for(int i=0; i<N; i++) {
			ex += (Math.max(0, h[i]-(long)B*x)+(A-B-1))/(A-B);
		}
		return ex<=x;
	}
}
