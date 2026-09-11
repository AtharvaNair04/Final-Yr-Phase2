import java.util.*;
import static java.lang.Math.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int K = sc.nextInt();
		int[] P = new int[N];
		int[] C = new int[N];
		
		for(int i=0; i<N; i++)
			P[i] = sc.nextInt()-1;
		for(int i=0; i<N; i++)
			C[i] = sc.nextInt();
		
		boolean[] visited = new boolean[N];
		
		long ans = Long.MIN_VALUE;
		for(int i=0; i<N; i++) {
			if(!visited[i]) {
				ArrayList<Integer> list = new ArrayList<>();
				int cur = i;
				while(!visited[cur]) {
//					list.add(cur);
					list.add(C[cur]);
					visited[cur] = true;
					cur = P[cur];
				}
				ans =max(ans, solve(list, K));
			}
		}
		
		System.out.println(ans);
		sc.close();
	}
	
	static long solve(ArrayList<Integer> list, int K) {
		int L = list.size();
		long[] score = new long[L+L+1];
        int idx = 1;
        for (int i : list) {
        	score[idx++] = i;
        }
 
        for (int i : list) {
        	score[idx++] = i;
        }
 
 
        for (int i = 1; i <= L + L; i++) {
        	score[i] += score[i - 1];
        }
//		for(int i=0; i<L+L; i++) {
//			score[i+1] = score[i] + list.get(i%L);
//		}
//		long[][] score = new long[L][L+1];
//		for(int i=0; i<L; i++) {
//			for(int j=0; j<L; j++) {
//				score[i][j+1] = score[i][j]+C[list.get((i+j)%L)];
//			}
//		}
		
		long ans = Long.MIN_VALUE;
//		for(int i=0; i<L; i++) {
//			for(int j=1; j<=((K-1)%L)+1; j++) {
////				ans = Math.max(ans, score[i][j]);
//				ans = Math.max(ans, score[i+j] - score[i]);
//			}
//		}
////		if(score[0][L]>0) {
////			ans += score[0][L]*((K-1)/L);
////		}
//		if(score[L]>0) {
//			ans += score[L]*((K-1)/L);
//		}
		
        long div = K / L;
        int rem = K % L;
        long fullSum = score[min(L, K)];
		
        for (int i=0; i<L; i++) {
            int j = i + min(L, K);
            long sum = Long.MIN_VALUE;
            for(int p = i, cnt = 1; p < j; p++, cnt++) {
                long psum = score[p+1]-score[i];
                sum = max(sum, psum);
                if(cnt <= rem) sum = max(sum, (div * fullSum) + psum);
                else if(div > 0) sum = max(sum, ((div - 1) * fullSum) + psum);
            }
 
            ans = max(ans, sum);
        }
		
		return ans;
	}
	
    static long solve2(ArrayList<Integer> g, int[] C, int k) {
        int n = g.size();
        long[] arr = new long[n + n + 1];
        int idx = 1;
        for (int i : g) {
            arr[idx++] = i;
        }
 
        for (int i : g) {
            arr[idx++] = i;
        }
 
        long maxVal = Long.MIN_VALUE;
 
        for (int i = 1; i <= n + n; i++) {
            arr[i] += arr[i - 1];
        }
 
        long fullSum = arr[min(n, k)];
 
        long div = k / n;
        int rem = k % n;
 
        for (int i = 1; i <= n; i++) {
            int j = i + min(n, k) - 1;
            long sum = Long.MIN_VALUE;
            for(int p = i, cnt = 1; p <= j; p++, cnt++) {
                long psum = getSum(arr, i, p);
                sum = max(sum, psum);
                if(cnt <= rem) sum = max(sum, (div * fullSum) + psum);
                else if(div > 0) sum = max(sum, ((div - 1) * fullSum) + psum);
            }
 
            maxVal = max(maxVal, sum);
        }
        return maxVal;
    }
 
    static long getSum(long[] arr, int i, int j) {
        return arr[j+1] - arr[i];
    }
 
    static long maxSubArraySum(long a[], int start, int end) {
        long maxSoFar = Long.MIN_VALUE, maxEndingHere = 0;
        for (int i = start; i <= end; i++) {
            maxEndingHere = maxEndingHere + a[i];
            if (maxSoFar < maxEndingHere)
                maxSoFar = maxEndingHere;
            if (maxEndingHere < 0)
                maxEndingHere = 0;
        }
        return maxSoFar;
    }

}
