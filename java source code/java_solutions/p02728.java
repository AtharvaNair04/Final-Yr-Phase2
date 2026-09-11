import java.io.*;
import java.util.*;
public class Main{
	static long fastpow(long n,long ti) {
		 if (ti == 0) 
		        return 1l;
		if(ti%2==0) {
			long y=fastpow(n, ti/2);
			long k=y*y;
			k%=mod;
			return k;
		}
		else {
			long y=fastpow(n, ti/2);
			long k=((n*y)%mod)*y;
			k%=mod;
			return k;
		}
	}
	static long mod=(long)1e9+7;
	static LinkedList<Integer>[]adj;
	static int[]sub;
	static void preDfs(int i,int par) {
		sub[i]++;
		for(int j:adj[i]) {
			if(j!=par) {
				preDfs(j, i);
				sub[i]+=sub[j];
			}
		}
	}
	static long modInv(long i) {
		return fastpow(i, mod-2);
	}
	static long[]ans;
	static void dfs(int i,int par) {
		ans[i]=fac[sub[i]-1];
		for(int j:adj[i]) {
			if(j!=par) {
				dfs(j, i);
				long mul=(ans[j]*modInv(fac[sub[j]]))%mod;
				ans[i]=(ans[i]*mul)%mod;
			}
		}
		
	}
	static long[]finalAns;
	static void dfs2(int i,int par) {
		finalAns[i]=ans[i];
		for(int j:adj[i]) {
			if(j!=par) {
				changeRoot(i, j);
				dfs2(j, i);
				changeRoot(j, i);
			}
		}
	}
	static void changeRoot(int u,int v) {
		//u is old root , v is new root
		int oldSubU=sub[u];
		int newSubU=sub[u]-sub[v];
		
		int oldSubV=sub[v];
		int newSubV=oldSubU;
		
		long oldAnsV=ans[v];
		
		long mul=(fac[newSubU-1])*modInv(fac[oldSubU-1])%mod;
		ans[u]=(ans[u]*mul)%mod;
		ans[u]=(ans[u]*fac[oldSubV])%mod;
		ans[u]=(ans[u]*modInv(oldAnsV))%mod;
		
		ans[v]=(ans[v]*(fac[newSubV-1]))%mod;
		ans[v]=(ans[v]*modInv(fac[oldSubV-1]))%mod;
		
		mul=(ans[u]*modInv(fac[newSubU]))%mod;
		ans[v]=(ans[v]*mul)%mod;
		
		sub[u]=newSubU;
		sub[v]=newSubV;
	}
	static long[]fac;
	public static void main(String[] args) throws Exception{
		MScanner sc=new MScanner(System.in);
		PrintWriter pw=new PrintWriter(System.out);
		int n=sc.nextInt();
		fac=new long[n+1];
		fac[0]=1;for(int i=1;i<=n;i++)fac[i]=(fac[i-1]*i)%mod;
		adj=new LinkedList[n];
		for(int i=0;i<n;i++)adj[i]=new LinkedList<>();
		for(int i=0;i<n-1;i++) {
			int x=sc.nextInt()-1,y=sc.nextInt()-1;
			adj[x].add(y);
			adj[y].add(x);
		}
		sub=new int[n];
		ans=new long[n];
		preDfs(0, -1);
		dfs(0, -1);
		finalAns=new long[n];
		dfs2(0, -1);
		for(int i=0;i<n;i++)pw.println(finalAns[i]);
		pw.flush();
	}
	static class MScanner {
		StringTokenizer st;
		BufferedReader br;
		public MScanner(InputStream system) {
			br = new BufferedReader(new InputStreamReader(system));
		}
 
		public MScanner(String file) throws Exception {
			br = new BufferedReader(new FileReader(file));
		}
 
		public String next() throws IOException {
			while (st == null || !st.hasMoreTokens())
				st = new StringTokenizer(br.readLine());
			return st.nextToken();
		}
		public int[] intArr(int n) throws IOException {
	        int[]in=new int[n];for(int i=0;i<n;i++)in[i]=nextInt();
	        return in;
		}
		public long[] longArr(int n) throws IOException {
	        long[]in=new long[n];for(int i=0;i<n;i++)in[i]=nextLong();
	        return in;
		}
		public int[] intSortedArr(int n) throws IOException {
	        int[]in=new int[n];for(int i=0;i<n;i++)in[i]=nextInt();
	        shuffle(in);
	        Arrays.sort(in);
	        return in;
		}
		public long[] longSortedArr(int n) throws IOException {
	        long[]in=new long[n];for(int i=0;i<n;i++)in[i]=nextLong();
	        shuffle(in);
	        Arrays.sort(in);
	        return in;
		}
		static void shuffle(int[]in) {
			for(int i=0;i<in.length;i++) {
				int idx=(int)(Math.random()*in.length);
				int tmp=in[i];
				in[i]=in[idx];
				in[idx]=tmp;
			}
		}
		static void shuffle(long[]in) {
			for(int i=0;i<in.length;i++) {
				int idx=(int)(Math.random()*in.length);
				long tmp=in[i];
				in[i]=in[idx];
				in[idx]=tmp;
			}
		}
		public Integer[] IntegerArr(int n) throws IOException {
	        Integer[]in=new Integer[n];for(int i=0;i<n;i++)in[i]=nextInt();
	        return in;
		}
		public Long[] LongArr(int n) throws IOException {
	        Long[]in=new Long[n];for(int i=0;i<n;i++)in[i]=nextLong();
	        return in;
		}
		public String nextLine() throws IOException {
			return br.readLine();
		}
 
		public int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
 
		public double nextDouble() throws IOException {
			return Double.parseDouble(next());
		}
 
		public char nextChar() throws IOException {
			return next().charAt(0);
		}
 
		public long nextLong() throws IOException {
			return Long.parseLong(next());
		}
 
		public boolean ready() throws IOException {
			return br.ready();
		}
 
		public void waitForInput() throws InterruptedException {
			Thread.sleep(3000);
		}
	}
	static void addX(int[]in,int x) {
		for(int i=0;i<in.length;i++)in[i]+=x;
	}
	static void addX(long[]in,int x) {
		for(int i=0;i<in.length;i++)in[i]+=x;
	}
}
