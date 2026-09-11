import java.util.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;

public class Main {

	int INF = 1 << 28;

	void run() {
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter("\n");
		for(;sc.hasNext();) {
			String[] str = sc.nextLine().split(" ");
			if(str[0].charAt(0) == '0') break;
			int A = 0;
			int bj = 0;
			for(int i=0;i<str.length;i++) {
				int val = min(10, Integer.parseInt(str[i]));
				if(val != 1) bj += val;
				else A++;
			}
			
			if( bj > 21 ) {
				System.out.println(0); 
			}
			else if( A == 0) {
				System.out.println(bj);
			}
			else {
				int tmp = bj;
				bj = 0;
				for(int i=0;i<=A;i++) {
//				debug(tmp);
					if(tmp + i + 11*(A-i) > 21) continue;
					bj = max(bj, tmp + i + 11*(A-i));
				}
				System.out.println(bj);
			}
			
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}
}
