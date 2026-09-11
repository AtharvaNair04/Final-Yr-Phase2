import java.io.PrintWriter;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PrintWriter pw = new PrintWriter(System.out);
		
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
			t[i] = sc.nextInt()-1;
		
		int score = 0;
		int[] last = new int[N];
		for(int i=0; i<D; i++) {
			score += s[i][t[i]];
			last[t[i]]=i+1;
			for(int j=0; j<N; j++)
				score -= c[j]*(i+1-last[j]);
		}

		int M = sc.nextInt();
		for(int i=0; i<M; i++) {
			int d = sc.nextInt();
			int to = sc.nextInt()-1;
			
			int from = t[d-1];

			score += s[d-1][to] + c[to]*(d-getIndex(t, to, d, false))*(getIndex(t, to, d, true)-d);
			score -= s[d-1][from] + c[from]*(d-getIndex(t, from, d, false))*(getIndex(t, from, d, true)-d);
			pw.println(score);
			t[d-1] = to;
		}

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
