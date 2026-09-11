import java.io.*;
import java.util.*;
import java.lang.*;

public class Main{
	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;
	
	int max(int a,int b){
		return Math.max(a,b);
	}
	
	String nextToken(){
		while(st==null||!st.hasMoreTokens()){
			try{
				st=new StringTokenizer(br.readLine());
			}catch (Exception e){
				eof=true;
				return null;
			}
		}
		return st.nextToken();
	}
	String nextString(){
		try{
			return br.readLine();
		}catch (IOException e){
			eof=true;
			return null;
		}
	}
	int nextInt() throws IOException{
		return Integer.parseInt(nextToken());
	}
	long nextLong() throws IOException{
		return Long.parseLong(nextToken());
	}
	double nextDouble() throws IOException{
		return Double.parseDouble(nextToken());
	}
	
	Main() throws IOException{
		br=new BufferedReader(new InputStreamReader(System.in));
		out=new PrintWriter(System.out);
		solve();
		out.close();
	}
	public static void main(String[] args) throws IOException{
		new Main();
	}
	
	final int inf=1000000;
	int a[]=new int[1010];
	int N;
	
	ArrayList<Integer> vals=new ArrayList<Integer>();
	int dp[][]=new int[1010][257];
	public class edge{
		int to,cost;
		edge(){}
		edge(int a,int b){
			to=a;cost=b;
		}
	};
	int dp2[][]=new int[1010][1010];
	edge G[]=new edge[1010*1010];
	int sz[]=new int[1010];
	int c;
	
	boolean check(int x){
		while(x%2==0) x/=2;
		return x==1;
	}
	int put(int mi){
		int res=0;
		c=0;
		vals=new ArrayList<Integer>();
		for(int i=0;i<N;i++){
			if(a[i]%mi==0&&check(a[i]/mi)) vals.add(a[i]/mi);
		}
		int n=vals.size();
		if(n==0) return 0;
		int m=1;
		while(m*2<=n) m*=2;
		for(int le=0;le<n;le++){
			for(int i=le;i<=n;i++) for(int j=0;j<=256;j++) dp[i][j]=-inf;
			dp[le][0]=0;
			int prev=-1;
			sz[le]=0;
			for(int ri=le;ri<n;ri++){
				for(int j=0;j<256;j++){
					int ac=j&(-j);
					int v=vals.get(ri);
					if(v<=ac||j==0){
						dp[ri+1][j+v]=max(dp[ri+1][j+v],dp[ri][j]+1);
					}
					dp[ri+1][j]=max(dp[ri+1][j],dp[ri][j]);
				}
			}
			for(int j=1;j<=256;j*=2){
				res=max(res,dp[n][j]);
			}
			for(int ri=le+1;ri<=n;ri++){
				if(dp[ri][256]==-inf) continue;
				if(prev<dp[ri][256]){
					prev=dp[ri][256];
					sz[le]++;
					G[c]=new edge(ri,dp[ri][256]);
					c++;
				}
			}
		}
		for(int i=0;i<=n;i++) for(int j=0;j<=m;j++) dp2[i][j]=-inf;
		dp2[0][0]=0;
		int id=0;
		for(int i=0;i<n;i++){
			for(int j=0;j<=m;j++){
				for(int k=0;k<sz[i];k++){
					int to=G[id+k].to;
					int cost=G[id+k].cost;
					dp2[to][j+1]=max(dp2[to][j+1],dp2[i][j]+cost);
				}
				dp2[i+1][j]=max(dp2[i+1][j],dp2[i][j]);
			}
			id+=sz[i];
		}
		for(int i=1;i<=n;i*=2) res=max(res,dp2[n][i]);
		return res;
	}
	int solve_(){
		int res=0;
		for(int i=1;i<=500;i+=2){
			int tmp=put(i);
			res=max(res,tmp);
		}
		return res;
	}
	void solve() throws IOException{
		while(true){
			N=nextInt();
			if(N==0) return;
			for(int i=0;i<N;i++) a[i]=nextInt();
			int ans=solve_();
			out.println(ans);
		}
	}
}
