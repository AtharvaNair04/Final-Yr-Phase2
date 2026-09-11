import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

//Moats around the Castle
public class Main{

	static int N, M;
	static boolean safe(int i, int j){
		return 0<=i&&i<M&&0<=j&&j<N;
	}
	static int[][] d;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int[][] move = {{0,1},{0,-1},{1,0},{-1,0}};
		while(true){
			int n = sc.nextInt();
			int m = sc.nextInt();
			if((n|m)==0)break;
			N = n+2;
			M = m+2;
			char[][] map = new char[M][N];
			for(int i=0;i<M;i++)map[i][0]=map[i][N-1]='.';
			for(int j=0;j<N;j++)map[0][j]=map[M-1][j]='.';
			for(int i=1;i<=m;i++){
				char[] s = sc.next().toCharArray();
				for(int j=0;j<n;j++)map[i][j+1]=s[j];
			}
			d = new int[M][N];
			for(int i=0;i<M;i++)Arrays.fill(d[i], Integer.MAX_VALUE);
			d[0][0] = 0;
			PriorityQueue<int[]> q = new PriorityQueue<int[]>(M, new Comparator<int[]>() {
				public int compare(int[] o1, int[] o2) {
					return d[o1[0]][o1[1]]-d[o2[0]][o2[1]];
				}
			});
			q.add(new int[]{0,0});
			int ans = Integer.MAX_VALUE;
			while(!q.isEmpty()){
				int[] a = q.poll();
				int i = a[0];
				int j = a[1];
				if(map[i][j]=='&'){
					ans = Math.min(ans, d[i][j]);
				}
				for(int k=0;k<4;k++){
					int ni = i+move[k][0];
					int nj = j+move[k][1];
					if(safe(ni,nj)){
						int cost = d[i][j] + (map[i][j]=='#'&&map[ni][nj]!='#'?1:0);
						if(cost < d[ni][nj]){
							d[ni][nj] = cost;
							q.add(new int[]{ni,nj});
						}
					}
				}
			}
			System.out.println(ans);
		}
	}
}
