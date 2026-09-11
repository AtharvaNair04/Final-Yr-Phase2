import java.util.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;
	//long INF = 1L << 62;
	double EPS = 1e-10;

	void run() {
		Scanner sc = new Scanner(System.in);
		long[] st = new long[31]; st[0] = 1;
		for(int i=1;i<=30;i++) {
			st[i] += st[i-1];
			if(i-2>=0) st[i] += st[i-2];
			if(i-3>=0) st[i] += st[i-3];
		}
		
		for(;;) {
			int n = sc.nextInt();
			if(n == 0) break;
			System.out.println(((st[n]+9) / 10 + 364) / 365);
		}
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
