import java.util.*;
import java.io.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;
	//long INF = 1L << 62;
	double EPS = 1e-10;
	int MAX = 1000;
	
	int[] month = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
	
	void run() {
		Scanner sc = new Scanner(System.in);
		for(;;) {
			int n = sc.nextInt();sc.nextLine();
			if(n == 0) break;
			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
			int[] ans = new int[MAX];
			for(int i=0;i<n;i++) {
				String[] sp = sc.nextLine().split("[/ :]");
//				debug(sp);
				
				if(sp[4].equals("I")) {
					int id = Integer.parseInt(sp[5]);
					int t = ( month[Integer.parseInt(sp[0])-1] + Integer.parseInt(sp[1]) ) * 24 * 60 + Integer.parseInt(sp[2]) * 60 + Integer.parseInt(sp[3]);
					map.put(id, t );
				}
				
				else {
					int id = Integer.parseInt(sp[5]);
					int t = ( month[Integer.parseInt(sp[0])-1] + Integer.parseInt(sp[1]) ) * 24 * 60 + Integer.parseInt(sp[2]) * 60 + Integer.parseInt(sp[3]);
					if(id == 0) {
						for(Map.Entry<Integer, Integer> entry: map.entrySet()) if(entry.getKey() != 0){
							ans[entry.getKey()] += t - max(map.get(id), map.get(entry.getKey()));
						}
					}
					else {
						if(map.containsKey(0)) {
							ans[id] += t - max(map.get(id), map.get(0));
						}
					}
					map.remove(id);
				}
			}
			
			int ret = 0;
			for(int i=0;i<MAX;i++) ret = max(ans[i], ret);
			System.out.println(ret);
		}
	}
	
	class V {
		int t, id;
		V(int t, int id) {
			this.t = t;
			this.id = id;
		}
		
		public int hashCode() {
			return t * id;
		}
		
		public boolean equals(Object o) {
			if(o instanceof V) {
				V v = (V) o;
				return t == v.t && id == v.t;
			}
			return false;
		}
	}
	
	

	void debug(Object... os) {
		System.err.println(Arrays.deepToString(os));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
