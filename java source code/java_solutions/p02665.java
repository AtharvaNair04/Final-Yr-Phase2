import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int[] A = new int[N+1];
		
		for(int i=0; i<=N; i++) {
			A[i] = sc.nextInt();
		}
		
		System.out.println(solve(N, A));
		sc.close();
	}
	
	static long solve(int N, int[] A) {
		if(A[0]>0) {
			if(N==0 && A[0]==1)
				return 1;
			else
				return -1;
		}
		
		long sum = 0;
		for(int i=0; i<=N; i++) {
			sum += A[i];
		}
	
		
		long leaf = 1;
		long node = 1;

		for(int i=0; i<N; i++) {
			leaf = Math.min(leaf*2, sum);
			node += leaf;
			if(leaf < A[i+1]) {
				return -1;
			}
			leaf -= A[i+1];
			sum -= A[i+1];
		}
	
		return node;
	}
}
