import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//International Party
public class Main{

	int n, m, INF = 1<<29, min;
	boolean[] u, v, res;
	boolean[][] e;
	
	boolean ok(){
		v = new boolean[m];
		return dfs(0)==m;
	}
	
	int dfs(int k){
		if(v[k])return 0;
		int res = 1;
		v[k] = true;
		for(int i=0;i<n;i++){
			if(!u[i]||!e[k][i])continue;
			for(int j=0;j<m;j++)if(e[j][i])res += dfs(j);
		}
		return res;
	}
	
	void f(int k, int d){
		if(5<d||min<=d)return;
		if(ok()){
			min = d;
			res = u.clone(); return;
		}
		for(int i=k;i<n;i++){
			u[i] = true;
			f(i+1, d+1);
			u[i] = false;
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(boolean h = true;;h=false){
			n = sc.nextInt(); m = sc.nextInt();
			if((n|m)==0)break;
			if(!h)System.out.println();
			Map<String, Integer> ref = new HashMap<String, Integer>();
			String[] s = new String[n];
			for(int i=0;i<n;i++){
				s[i] = sc.next();
				ref.put(s[i], i);
			}
			e = new boolean[m][n];
			for(int i=0;i<m;i++){
				int k = sc.nextInt();
				while(k--!=0)e[i][ref.get(sc.next())] = true;
			}
			u = new boolean[n];
			min = INF;
			f(0, 0);
			if(min==INF)System.out.println("Impossible");
			else {
				System.out.println(min);
				for(int i=0;i<n;i++)if(res[i])System.out.println(s[i]);
			}
		}
	}
	
	void debug(Object...o){
//		System.out.println(Arrays.deepToString(o));
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
