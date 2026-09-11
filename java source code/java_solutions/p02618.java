import java.io.PrintWriter;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PrintWriter pw = new PrintWriter(System.out);
		Random r = new Random();
		
		long startMilli = System.currentTimeMillis();
		
		final int N = 26;

		int D = sc.nextInt();
		int[] c = new int[N];
		for(int i=0; i<N; i++)
			c[i] = sc.nextInt();
		
		int[][] s = new int[D][N];
		for(int i=0; i<D; i++)
			for(int j=0; j<N; j++)
				s[i][j] = sc.nextInt();
		
		int[] t = new int[D];
		for(int i=0; i<D; i++)
			t[i] = r.nextInt(N);
		
//		int score = 0;
//		int[] last = new int[N];
//		for(int i=0; i<D; i++) {
//			score += s[i][t[i]];
//			last[t[i]]=i+1;
//			for(int j=0; j<N; j++)
//				score -= c[j]*(i+1-last[j]);
//		}
		
		long currentMilli = System.currentTimeMillis();

		while(currentMilli - startMilli < 1900) {
			for(int i=0; i<10000; i++) {
				int d = r.nextInt(D)+1;
				int to = r.nextInt(N);
				int from = t[d-1];
				if(from==to)
					continue;
	
				int scoreDiff = s[d-1][to] + c[to]*(d-getIndex(t, to, d, false))*(getIndex(t, to, d, true)-d);
				scoreDiff -= s[d-1][from] + c[from]*(d-getIndex(t, from, d, false))*(getIndex(t, from, d, true)-d);
				if(scoreDiff>0)
					t[d-1] = to;
			}
			currentMilli = System.currentTimeMillis();
		}
		for(int i=0; i<D; i++)
			pw.println(t[i]+1);

		sc.close();
		pw.close();
	}
	
	static int getIndex(int[] t, int type, int base, boolean isForward) {
		int ans = base;
		int diff = isForward ? 1 : -1;
		ans += diff;
		while(1 <= ans && ans <= t.length) {
			if(t[ans-1]==type)
				return ans;
			ans += diff;
		}
		return ans;
	}
}
