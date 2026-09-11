import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

//Manhattan Wiring
public class Main{

	int h, w, ai, aj, bi, bj, res, INF = 1<<28, need, c;
	int[] dir;
	int[][] m, dist;
	int[][] d = {{-1,0},{0,1},{1,0},{0,-1}};
	
	boolean ok(int i, int j){
		return 0<=i&&i<h&&0<=j&&j<w;
	}
	
	boolean cancel(int n, int i, int j){
		if(n<3)return false;
		if(dir[n-1]==dir[n-2])return false;
		int K = dir[n-2], N = 0, M = 0;
		int k = n-2;
		while(0<=k && dir[k]==K){
			N++; k--;
		}
		while(0<=k && dir[k]==(dir[n-1]+2)%4){
			M++; k--;
		}
		if(k<0 || N < 2 || M==0)return false;
		for(int x=0;x<M;x++){
			int ni = i+x*d[dir[n-1]][0], nj = j+x*d[dir[n-1]][1];
			for(int y=0;y<N-1;y++){
				ni+=d[(K+2)%4][0]; nj+=d[(K+2)%4][1];
				if(m[ni][nj]!=0)return false;
			}
		}
		return true;
	}
	
	int bfs(){
		for(int[]a:dist)Arrays.fill(a, INF);
		dist[bi][bj] = 0;
		Queue<int[]> q = new LinkedList<int[]>();
		q.add(new int[]{bi, bj});
		while(!q.isEmpty()){
			int[] V = q.poll();
			int pi = V[0], pj = V[1];
			if((bi!=pi || bj!=pj) && m[pi][pj]==3)return dist[pi][pj];
			for(int k=0;k<4;k++){
				int ni = pi+d[k][0], nj = pj+d[k][1];
				if(ok(ni, nj)&&(m[ni][nj]==0 || m[ni][nj]==3)&&dist[ni][nj]==INF){
					dist[ni][nj] = dist[pi][pj]+1;
					q.add(new int[]{ni, nj});
				}
			}
		}
		return INF;
	}
	
	void dfs(int i, int j, int rest, int step){
		if(res<=step+need)return;
		if(rest < need)return;
		if(m[i][j]==2 && (i!=ai || j!=aj)){
			res = Math.min(res, step+bfs()); return;
		}
		if(cancel(step, i, j))return;
		int c = 0;
		for(int k=0;k<4;k++){
			int ni = i+d[k][0], nj = j+d[k][1];
			if(ok(ni, nj)&&m[ni][nj]==-1)c++;
		}
		if(2<=c)return;
		int x = m[i][j];
		m[i][j] = -1;
		for(int k=0;k<4;k++){
			int ni = i+d[k][0], nj = j+d[k][1];
			if(ok(ni, nj)&&(m[ni][nj]==0 || m[ni][nj]==2)){
				dir[step] = k;
				dfs(ni, nj, m[ni][nj]==0?(rest-1):rest, step+1);
			}
		}
		m[i][j] = x;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			h = sc.nextInt(); w = sc.nextInt();
			if((h|w)==0)break;
			m = new int[h][w];
			dist = new int[h][w];
			dir = new int[100];
			for(int i=0;i<h;i++)for(int j=0;j<w;j++){
				m[i][j] = sc.nextInt();
				if(m[i][j]==0)c++;
				if(m[i][j]==2){
					ai = i; aj = j;
				}
				if(m[i][j]==3){
					bi = i; bj = j;
				}
			}
			res = INF;
			need = bfs();
			if(need!=INF){
				need--;
				m[ai][aj] = -1;
				dfs(ai, aj, c, 0);
			}
			System.out.println(res==INF?0:res);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
