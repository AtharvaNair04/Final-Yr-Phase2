import java.io.PrintWriter;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PrintWriter pw = new PrintWriter(System.out);
		int N = sc.nextInt();
		int[] A = new int[1<<N];
		
		for(int i=0; i<1<<N; i++)
			A[i] = sc.nextInt();
		
		int[][] max = new int[2][1<<N];
		Arrays.fill(max[0], -1);
		Arrays.fill(max[1], -1);
		for(int i=0; i<N; i++) {
			if(A[1<<i] > A[0]) {
				max[0][1<<i] = 1<<i;
				max[1][1<<i] = 0;
			} else {
				max[0][1<<i] = 0;
				max[1][1<<i] = 1<<i;
			}
		}
		
		for(int L=2; L<=N; L++) {
			int comb = (1<<L)-1;
			HashSet<Integer> set = new HashSet<>();
			while(comb<(1<<N)) {
				set.clear();
				
				for(int i=0; i<N; i++) {
					if((comb & (1<<i))!=0) {
						set.add(max[0][comb^(1<<i)]);
						set.add(max[1][comb^(1<<i)]);
					}
				}
				set.add(comb);
				Iterator<Integer> it = set.iterator();
				while(it.hasNext()) {
					Integer idx = it.next();
					if(max[0][comb]<0 || A[max[0][comb]] < A[idx]) {
						max[1][comb] = max[0][comb];
						max[0][comb] = idx;
					} else if(max[1][comb]<0 || A[max[1][comb]] < A[idx]) {
						max[1][comb] = idx;
					}
				}
				
				int x = comb & -comb;
				int y = comb+x;
				comb = ((comb & ~y)/ x>>1) | y;
			}
		}
		long ans = 0;
		for(int i=1; i<1<<N; i++) {
			ans = Math.max(ans, A[max[0][i]] + A[max[1][i]]);
			pw.println(ans);
		}
		sc.close();
		pw.close();
	}
}
