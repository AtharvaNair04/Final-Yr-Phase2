import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

//Alien's Counting
public class Main{

	Set<Integer>[] e, rev, in, out;
	int N, M;
	long MOD = 1000000007;
	
	int[] scc;
	boolean[] u;
	List<Integer> list;
	
	long[][] dp;
	
	void dfs(int k){
		u[k] = true;
		for(int v:e[k])if(!u[v])dfs(v);
		list.add(k);
	}
	void rdfs(int k, int id){
		u[k] = true;
		scc[k] = id;
		for(int v:rev[k])if(!u[v])rdfs(v, id);
	}

	long get(int k){
		if(dp[k][0]!=-1)return (dp[k][0]+dp[k][1])%MOD;
		if(out[k].isEmpty()){
			dp[k][0] = dp[k][1] = 1;
			return 2;
		}
		dp[k][0] = dp[k][1] = 1;
		for(int v:out[k]){
			dp[k][1]*=get(v);
			dp[k][1]%=MOD;
			dp[k][0]*=dp[v][0];
			dp[k][0]%=MOD;
		}
		return (dp[k][0]+dp[k][1])%MOD;
	}
	
	@SuppressWarnings("unchecked")
	void run(){
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		M = sc.nextInt();
		e = new Set[N];
		rev = new Set[N];
		for(int i=0;i<N;i++){
			e[i] = new HashSet<Integer>();
			rev[i] = new HashSet<Integer>();
		}
		while(M--!=0){
			int s = sc.nextInt()-1, t = sc.nextInt()-1;
			e[s].add(t);
			rev[t].add(s);
		}
		list = new ArrayList<Integer>();
		u = new boolean[N];
		for(int i=0;i<N;i++)if(!u[i])dfs(i);
		Arrays.fill(u, false);
		int ID = 0;
		scc = new int[N];
		for(int i=list.size()-1;i>=0;i--){
			int j = list.get(i);
			if(u[j])continue;
			rdfs(j, ID);
			ID++;
		}
		in = new Set[ID];
		out = new Set[ID];
		for(int i=0;i<ID;i++){
			in[i] = new HashSet<Integer>();
			out[i] = new HashSet<Integer>();
		}
		for(int i=0;i<N;i++){
			for(int v:rev[i]){
				if(scc[i]==scc[v])continue;
				out[scc[i]].add(scc[v]);
				in[scc[v]].add(scc[i]);
			}
		}
		dp = new long[ID][2];
		for(long[]d:dp)Arrays.fill(d, -1);
		long res = 1;
		for(int i=0;i<ID;i++)if(in[i].isEmpty()){
			res*=get(i);
			res%=MOD;
		}
		System.out.println(res);
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
