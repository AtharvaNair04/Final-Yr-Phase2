import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO ?????????????????????????????????????????????
		new Main().start();
	}
	int[] matchTo;
	boolean[] vis;
	ArrayList<Integer>[] g;
	@SuppressWarnings("unchecked")
	void start(){
		Scanner in = new Scanner(System.in);
		int x = in.nextInt();
		int y = in.nextInt();
		int e = in.nextInt();
		int[] xs = new int[e];
		int[] ys = new int[e];
		g = new ArrayList[x + y];
		for(int i = 0; i < x + y; i++){
			g[i] = new ArrayList<>();
		}
		for(int i = 0; i < e; i++){
			xs[i] = in.nextInt();
			ys[i] = in.nextInt() + x;
			g[xs[i]].add(ys[i]);
			g[ys[i]].add(xs[i]);
		}
		vis = new boolean[x + y];
		matchTo = new int[x + y];
		Arrays.fill(matchTo, -1);
		int match = 0;
		for(int i = 0; i < x; i++){
			Arrays.fill(vis, false);
			if(dfs(i))match++;
		}
		System.out.println(match);
		in.close();
	}
	boolean dfs(int v){
		if(v < 0)
			return true;
		for(int u : g[v]){
			if(vis[u])
				continue;
			vis[u] = true;
			if(dfs(matchTo[u])){
				matchTo[u] = v;
				matchTo[v] = u;
				return true;
			}
		}
		return false;
	}

}
