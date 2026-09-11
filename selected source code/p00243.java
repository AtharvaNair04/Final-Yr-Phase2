import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

//Filling Game
public class Main{
	
	int h, w, N, res;
	int[][] a, id;
	int[][] d = {{-1,0},{0,1},{1,0},{0,-1}};
	Set<Integer>[] adj;
	int[] col, bfs;
	boolean[] u;
	
	boolean ok(int i, int j){
		return 0<=i&&i<h&&0<=j&&j<w;
	}
	
	void fill(int i, int j, int ID, int col){
		a[i][j] = -1;
		id[i][j] = ID;
		for(int k=0;k<4;k++){
			int ni = i+d[k][0], nj = j+d[k][1];
			if(ok(ni, nj)&&a[ni][nj]==col)fill(ni, nj, ID, col);
		}
	}
	
	@SuppressWarnings("unchecked")
	void f(int depth, int sum){
//		System.out.println("Depth:"+depth+" Sum:"+sum+" N:"+N);
		if(sum==N){
			res = depth; return;
		}
		Arrays.fill(bfs, 1<<29);
		Queue<Integer> q = new LinkedList<Integer>();
		for(int i=0;i<N;i++)if(u[i]){
			bfs[i] = 0; q.add(i);
		}
		int max = 0;
		while(!q.isEmpty()){
			int v = q.poll();
			for(int x:adj[v]){
				if(bfs[x]==1<<29){
					bfs[x] = bfs[v]+1; q.add(x);
					max = Math.max(max, bfs[x]);
				}
			}
		}
		if(res<=depth+max)return;
		Set<Integer>[] nx = new Set[3];
		for(int i=0;i<3;i++)nx[i]=new HashSet<Integer>();
		for(int i=0;i<N;i++)if(u[i]){
			for(int x:adj[i]){
				if(!u[x])nx[col[x]].add(x);
			}
		}
		for(int c=0;c<3;c++)if(!nx[c].isEmpty()){
			for(int x:nx[c])u[x]=true;
			f(depth+1, sum+nx[c].size());
			for(int x:nx[c])u[x]=false;
		}
	}
	
	@SuppressWarnings("unchecked")
	void run(){
		Scanner sc = new Scanner(System.in);
		adj = new Set[150];
		for(;;){
			w = sc.nextInt(); h = sc.nextInt();
			if((w|h)==0)break;
			a = new int[h][w];
			id = new int[h][w];
			for(int i=0;i<h;i++)for(int j=0;j<w;j++){
				char c = sc.next().charAt(0);
				a[i][j] = c=='R'?0:c=='G'?1:2;
			}
			N = 0;
			col = new int[h*w];
			for(int i=0;i<h;i++)for(int j=0;j<w;j++)if(a[i][j]!=-1){
				col[N] = a[i][j];
				fill(i, j, N, a[i][j]);
				N++;
			}
			for(int i=0;i<N;i++)adj[i] = new HashSet<Integer>();
			for(int i=0;i<h;i++)for(int j=0;j<w;j++){
				for(int k=0;k<4;k++){
					int ni = i+d[k][0], nj = j+d[k][1];
					if(ok(ni, nj)&&id[i][j]!=id[ni][nj])adj[id[i][j]].add(id[ni][nj]);
				}
			}
//			for(int i=0;i<N;i++){
//				System.out.print("N:"+i+" |");
//				for(int x:adj[i])System.out.print(x+" ");
//				System.out.println();
//			}
//			printid();
			res = N;
			u = new boolean[N];
			u[0] = true;
			bfs = new int[N];
			f(0, 1);
			System.out.println(res);
		}
	}
	
	void printid(){
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++)System.out.print(id[i][j]+" ");
			System.out.println();
		}
		System.out.println();
	}
	
	void debug(Object...o){
		System.out.println(Arrays.deepToString(o));
	}
	
/*
10 10
B R G R B R G B R G
G B R B G B R G B R
R G B G B G B R G B
B R G R G R G B R G
G B R G R G B R G B
R G B R G B R B R G
B R G B R G B R G B
G B R G B R G B R G
B R G B G B R G B R
R B R G B G B R G B
0 0
 */
	
	public static void main(String[] args) {
		new Main().run();
	}
}
