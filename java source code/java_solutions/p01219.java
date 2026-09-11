import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//Private Teacher
public class Main{

	long augumentPath(int v, int t, long f, long[][] cap, boolean[] used){
		if(v==t)return f;
		used[v] = true;
		for(int i=0;i<cap[v].length;i++){
			if(cap[v][i]>0 && !used[i]){
				long d = augumentPath(i, t, Math.min(f, cap[v][i]), cap, used);
				if(d>0){
					cap[v][i] -= d;
					cap[i][v] += d;
					return d;
				}
			}
		}
		return 0;
	}
	
	long maxFlow(int s, int t, long[][] cap){
		long flow = 0;
		int n = cap.length;
		boolean[] used = new boolean[n];
		while(true){
			Arrays.fill(used, false);
			long f = augumentPath(s, t, Long.MAX_VALUE, cap, used);
			if(f==0)return flow;
			flow += f;
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		String[] day = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
		Map<String, Integer> ref = new HashMap<String, Integer>();
		for(int i=0;i<7;i++)ref.put(day[i], i);
		for(;;){
			int n = sc.nextInt();
			long W = sc.nextLong();
			if((n|W)==0)break;
			long[][] cap = new long[n+9][n+9];
			for(int i=1;i<=7;i++)cap[0][i]=W;
			long s = 0;
			for(int i=0;i<n;i++){
				long t = sc.nextLong();
				s += t;
				cap[i+8][n+8] = t;
				int c = sc.nextInt();
				while(c--!=0)cap[ref.get(sc.next())+1][i+8] = W;
			}
			System.out.println(maxFlow(0, n+8, cap)==s?"Yes":"No");
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
