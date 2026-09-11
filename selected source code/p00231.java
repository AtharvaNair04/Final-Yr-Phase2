import java.util.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;

public class Main {

	int INF = 1 << 28;

	void run() {
		Scanner sc = new Scanner(System.in);
		for(;;) {
			int n = sc.nextInt();
			if(n == 0) break;
			PriorityQueue<Man> que = new PriorityQueue<Man>();
			
			for(int i=0;i<n;i++) {
				int w = sc.nextInt();
				que.add(new Man(true, w, sc.nextInt()));
				que.add(new Man(false, w, sc.nextInt()));
			}
			boolean flg = true;
			int w=0;
			for(;!que.isEmpty();) {
				Man man = que.remove();
				w += man.on? man.w:-man.w;
				if(w>150) {
					flg = false;
					break;
				}
			}
			if(flg) System.out.println("OK");
			else System.out.println("NG");
		}
	}
	
	class Man implements Comparable<Man> {
		boolean on;
		int w;
		int t;
		Man(boolean on, int w, int t) {
			this.on = on;
			this.w = w;
			this.t = t;
		}
		
		@Override
		public int compareTo(Man o) {
			// TODO 自動生成されたメソッド・スタブ
			if(t==o.t) return on? 1:-1;
			return t-o.t;
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}
}
