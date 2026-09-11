import java.util.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;
	//long INF = 1L << 62;
	double EPS = 1e-10;
	int MOD = 1000000;
	
	int[][] es = {
			{0, 1, 2, 3},
			{0, 1, 2},
			{0, 1, 2, 4},
			{2, 3},
			{0, 4}
			};
	//	|.., .|., ..|, |_||, ||_|
	int len = 5;
	
	void run() {
		Scanner sc = new Scanner(System.in);
		int h = sc.nextInt(), w=sc.nextInt();
		if( w == 1 ) {
			int ret = 1;
			for(int i=0;i<h;i++) ret = ret * 2 % MOD;
			System.out.println(ret);
			return;
		}
		int[][] cnt = new int[2][len]; cnt[0][0] = 1;
		int p = 1;
		for(int i=0;i<=h;i++) {
			fill(cnt[p], 0);
			for(int j=0;j<len;j++) for(int k=0;k<es[j].length;k++) {
				cnt[p][es[j][k]] = (cnt[p][es[j][k]] + cnt[1-p][j]) % MOD;
			}
//			debug(cnt[p]);
			p = 1-p;
		}
//		int ans = ( cnt[1-p][2] + cnt[1-p][3] ) % MOD;
		System.out.println(cnt[1-p][2]);
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
