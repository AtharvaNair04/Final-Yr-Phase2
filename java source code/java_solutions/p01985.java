import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.StringTokenizer;

class Main{
	static int[] color;
	static boolean[][] rinsetu;
	static int N;
	static int M;
	static boolean dou=false;
	public static void main(String[] args) {
		SC sc=new SC(System.in);
		while(true) {
			dou=false;
			N=sc.nextInt();
			M=sc.nextInt();
			if(N*M==0) {
				System.exit(0);
			}
			rinsetu=new boolean[N+1][N+1];
			color=new int[N+1];
			for(int i=0; i<=N; i++) {
				for(int j=0; j<=N; j++) {
					rinsetu[i][j]=false;
				}
				color[i]=-1;		//初期値　花子太郎軍団は0次郎は1とかで
			}
			int s=0; int g=0;
			for(int i=0; i<M; i++) {
				s=sc.nextInt();
				g=sc.nextInt();
				rinsetu[s][g]=true;
				rinsetu[g][s]=true;
			}
			color[1]=0;
			for(int i=1; i<=N; i++) {
				if(color[i]==-1 && rinsetu[1][i]) {
					dfs(1,i);
				}
			}
			int tarohanako=0;
			int jiro=0;
			for(int i=1; i<=N; i++) {
				if(color[i]==0) {
					tarohanako++;
				}
				else if(color[i]==1) {
					jiro++;
				}
			}
			if(dou==false) {
				for(int i=1; i<=N; i++) {
					for(int j=1; j<=N; j++) {
						if(rinsetu[i][j]) {
							if(color[i] == color[j] && i!=j) {
								dou=true;
								break;
							}
						}
					}
					if(dou) {
						break;
					}
				}
			}
			if(dou==false) {
				if(tarohanako%2==0 && jiro%2==0 && tarohanako-jiro!=0) {
					pl(2);	//2色で塗り分けられ、どちらの領域も奇数の時、太郎花子軍団と次郎はどっちかを選べる　ただし2つの領域の数が同じ時、判別不能
					pl(Math.min(tarohanako/2, jiro/2));
					pl(Math.max(tarohanako/2, jiro/2));
				}
				else if(tarohanako%2==0 && jiro%2==0 && tarohanako-jiro==0) {
					pl(1);	//2色で塗り分けられ、どちらの領域も奇数の時、太郎花子軍団と次郎はどっちかを選べる　ただし2つの領域の数が同じ時、判別不能
					pl(Math.min(tarohanako/2, jiro/2));
				}
				else if(tarohanako%2==1 && jiro%2==1) {
					pl(0);	//どっちも奇数なら分けられないのでダメ
				}
				else {
					pl(1);	//↑のどちらでもないなら1個
					if(tarohanako%2==0) {
						pl(tarohanako/2);
					}
					else if(jiro%2==0) {
						pl(jiro/2);
					}
				}
			}
			else {
				pl(0);
			}
		}
	}
	static void dfs(int s,int t) {
		if(color[s]==0) {
			if(color[t]==-1) {//未探索なら
				color[t]=1;//隣接しないようにする
				for(int j=1; j<=N; j++) {	//DFS
					if(rinsetu[t][j] && color[j]==-1) {		//グラフが連結なので、任意のグラフの頂点から辺をたどってすべての頂点に到達が可能
						dfs(t,j);		//もう訪問したところは訪問しない
					}
				}
			}
			else if(color[t]==1) {	//すでに支配下なら一応隣接するところも調べておく
				for(int j=1; j<=N; j++) {
					if(rinsetu[t][j] && color[j]==-1) {
						dfs(t,j);
					}
				}
			}
			else if(color[t]==0) {
				dou=true;
			}
		}
		else if(color[s]==1) {
			if(color[t]==-1) {//未探索なら
				color[t]=0;//隣接しないようにする
				for(int j=1; j<=N; j++) {	//DFS
					if(rinsetu[t][j] && color[j]==-1) {		//グラフが連結なので、任意のグラフの頂点から辺をたどってすべての頂点に到達が可能
						dfs(t,j);		//もう訪問したところは訪問しない
					}
				}
			}
			else if(color[t]==0) {	//すでに支配下なら一応隣接するところも調べておく
				for(int j=1; j<=N; j++) {
					if(rinsetu[t][j] && color[j]==-1) {
						dfs(t,j);
					}
				}
			}
			else if(color[t]==1) {
				dou=true;
			}
		}
	}
	static class SC {
		private BufferedReader reader = null;
		private StringTokenizer tokenizer = null;
		public SC(InputStream in) {
			reader = new BufferedReader(new InputStreamReader(in));
		}
		public String next() {
			if (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
			return tokenizer.nextToken();
		}
		public int nextInt() {
			return Integer.parseInt(next());
		}
		public long nextLong() {
			return Long.parseLong(next());
		}
		public double nextDouble() {
			return Double.parseDouble(next());
		}
		public String nextLine() {
			try {
				return reader.readLine();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}
	public static void pl(Object o) {
		System.out.println(o);
	}
	public static void p(Object o) {
		System.out.print(o);
	}
}
