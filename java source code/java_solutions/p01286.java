import java.util.Arrays;
import java.util.Scanner;

//Luigi's Tavern
public class Main{

	int augumentPath(int v, int t, int f, int[][] cap, boolean[] used){
		if(v==t)return f;
		used[v] = true;
		for(int i=0;i<cap[v].length;i++){
			if(cap[v][i]>0 && !used[i]){
				int d = augumentPath(i, t, Math.min(f, cap[v][i]), cap, used);
				if(d>0){
					cap[v][i] -= d;
					cap[i][v] += d;
					return d;
				}
			}
		}
		return 0;
	}
	
	int maxFlow(int s, int t, int[][] cap){
		int flow = 0;
		int n = cap.length;
		boolean[] used = new boolean[n];
		while(true){
			Arrays.fill(used, false);
			int f = augumentPath(s, t, Integer.MAX_VALUE, cap, used);
			if(f==0)return flow;
			flow += f;
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			int h = sc.nextInt(), w = sc.nextInt(), c = sc.nextInt(), m = sc.nextInt(), nw = sc.nextInt(), nc = sc.nextInt(), nm = sc.nextInt();
			if(h<0)break;
			int n = h+w+c+m, NW = n, NC = n+1, NM = n+2, N = n+3, S = 2*N, T = 2*N+1;
			int[][] cap = new int[2*N+2][2*N+2];
			for(int i=0;i<n;i++)cap[i+N][i] = 1;
			cap[NW+N][NW] = nw;
			cap[NC+N][NC] = nc;
			cap[NM+N][NM] = nm;
			for(int i=0;i<h;i++){
				cap[S][i+N] = 1; cap[i][NW+N] = 1;
			}
			for(int i=0;i<w;i++){
				int k = sc.nextInt();
				while(k--!=0){
					int id = sc.nextInt()-1;
					cap[id][h+i+N] = 1;
				}
				cap[h+i][NC+N] = 1;
			}
			for(int i=0;i<c;i++){
				int k = sc.nextInt();
				while(k--!=0){
					int id = sc.nextInt()-1+h;
					cap[id][i+h+w+N] = 1;
				}
				cap[h+w+i][NM+N] = 1;
			}
			for(int i=0;i<m;i++){
				int k = sc.nextInt();
				while(k--!=0){
					int id = sc.nextInt()-1+h+w;
					cap[id][i+h+w+c+N] = 1;
				}
				cap[i+h+w+c][T] = 1;
			}
//			cap[NW][NC+N] = nw;
			for(int i=0;i<c;i++)cap[NW][i+h+w+N] = 1;
//			cap[NC][NM+N] = nc;
			for(int i=0;i<m;i++)cap[NC][i+h+w+c+N] = 1;
			cap[NM][T] = nm;
			System.out.println(maxFlow(S, T, cap));
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
