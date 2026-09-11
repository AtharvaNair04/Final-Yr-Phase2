import java.util.*;

public class Main {
	static final int LEN = 41;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int N = sc.nextInt();
		long[] A = new long[N];
		for(int i=0; i<N; i++) {
			A[i] = sc.nextLong();
		}
		
		long goal = 0;
		for(int i=2; i<N; i++)
			goal ^= A[i];
		
		System.out.println(solve(A[0], A[1], goal));
		sc.close();
	}
	
	static long solve(long a0, long a1, long goal) {
		if((a0^a1)==goal)
			return 0;
		
		long sum = a0+a1;
		
		int[] bits = new int[LEN];
		boolean carry = false;

		for(int i=LEN-1; i>=0; i--) {
			if(carry) {
				if(bit(sum, i)==0) {
					if(bit(goal, i)==0) {
						bits[i] = 2;
						carry = false;
					} else {
						bits[i] = 1;
					}
				} else {
					if(bit(goal, i)==0) {
						bits[i] = 2;
					} else {
						return -1;
					}
				}
			} else {
				if(bit(sum, i)==0) {
					if(bit(goal, i)==0) {
						bits[i] = 0;
					} else {
						return -1;
					}
				} else {
					if(bit(goal, i)==0) {
						bits[i] = 0;
						carry = true;
					} else {
						bits[i] = 1;
					}
				}
			}
		}
		if(carry)
			return -1;

		long ans = Long.MAX_VALUE;
		for(int i=LEN-1; i>=0; i--) {
			if(bit(a0, i)==1) {
				boolean ok = true;
				long after = 0;
				for(int j=LEN-1; j>=0; j--) {
					if(j>i) {
						if((bit(a0, j)==0 && bits[j]==2)||(bit(a0, j)==1 && bits[j]==0)) {
							ok = false;
							break;
						}
						after |= ((long)(bit(a0, j)))<<j;
					} else if(j==i) {
						if(bits[j]==2) {
							ok = false;
							break;
						}
					} else {
						if(bits[j]>0) {
							after |= 1l<<j;
						}
					}
				}
				if(ok) {
					ans = Math.min(ans, a0-after);
				}
			}
		}
		
		return ans < Long.MAX_VALUE ? (ans==a0 ? -1 : ans) : -1;
	}
	
	static int bit(long a, int b) {
		return (int)((a>>b)&1);
	}
}
