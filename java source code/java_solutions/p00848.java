import static java.lang.Math.*;
import static java.util.Arrays.*;

import java.util.*;
import java.io.*;

public class Main {
	int n, k, m;
	ArrayList<Integer> ps;
	int N = 1120;
	int mem[][][];
	
	void run() {
		Scanner sc = new Scanner(System.in);
		prime();
		for(;;) {
			n = sc.nextInt(); k = sc.nextInt();
			if((n|k) == 0) break;
			mem = new int[n+1][k+1][m+1];
			for(int[][] a: mem) for(int[] b: a) fill(b, -1);
			System.out.println(solve(0, 0, 0));
		}
	}
	
	int solve(int sum, int num, int i) {
		if( i == m+1 || sum > n || num > k ) return 0;
		if( mem[sum][num][i] >= 0 ) return mem[sum][num][i];
		if( k == num ) {
			if( sum == n ) return mem[sum][num][i] = 1;
		}
		
//		debug(sum, num, i);
		int cnt = 0;
		for(int j=i;j<m && sum+ps.get(j)<=n;j++) cnt += solve(sum+ps.get(j), num+1, j+1);
		return mem[sum][num][i] = cnt;
	}
	
	void prime() {
		boolean[] p = new boolean[N+1];
		ps = new ArrayList<Integer>();
		for(int i=2;i<=N;i++) if(!p[i]) {
			for(int j=2*i;j<=N;j+=i) p[j] = true;
			ps.add(i);
		}
		m = ps.size(); 
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
