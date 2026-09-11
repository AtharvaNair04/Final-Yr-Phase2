import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//The Squares
public class Main{

	static class P{
		int i, j, dir;
		public P(int i, int j, int dir) {
			this.i = i;
			this.j = j;
			this.dir = dir;
		}
	}
	
	public static void main(String[] args) {
		int[][] move = {{-1,0},{0,-1},{1,0},{0,1}};
		Scanner sc = new Scanner(System.in);
		while(true){
			int w = sc.nextInt();
			int h = sc.nextInt();
			if((w|h)==0)break;
			char[][] map = new char[h][w];
			List<P> l = new ArrayList<P>();
			for(int i=0;i<h;i++){
				char[] s = sc.next().toCharArray();
				for(int j=0;j<w;j++){
					map[i][j] = s[j];
					if(s[j]!='.'&&s[j]!='#'&&s[j]!='X'){
						map[i][j] = 'P';
						int d = s[j]=='N'?0:s[j]=='W'?1:s[j]=='S'?2:3;
						l.add(new P(i,j,d));
					}
				}
			}
			String ans = "NA";
			int step = 1;
			while(step<=180){
				List<P> next = new ArrayList<P>();
				int[] nd = new int[l.size()];
				for(int i=0;i<l.size();i++){
					P p = l.get(i);
					nd[i] = -1;
					for(int k=3;k<7;k++){
						int ni = p.i+move[(p.dir+k)%4][0];
						int nj = p.j+move[(p.dir+k)%4][1];
						if(map[ni][nj]=='.'||map[ni][nj]=='X'){
							nd[i] = (p.dir+k)%4;
							break;
						}
					}
					if(nd[i]==-1)next.add(p);
				}
				for(int k=1;k<=4;k++){
					int kk = k%4;
					for(int i=0;i<l.size();i++){
						if(nd[i]==kk){
							P p = l.get(i);
							map[p.i][p.j] = '.';
							int ni = p.i+move[kk][0];
							int nj = p.j+move[kk][1];
							if(map[ni][nj]=='X'){
								map[ni][nj]='x';
								continue;
							}
							if(map[ni][nj]=='.'){
								map[ni][nj] = 'P';
								next.add(new P(ni,nj,nd[i]));
							}
							else{
								map[p.i][p.j] = 'P';
								next.add(new P(p.i,p.j,nd[i]));
							}
						}
					}
				}
				l = next;
				if(l.isEmpty()){
					ans = step+"";
					break;
				}
				for(int i=0;i<h;i++)for(int j=0;j<w;j++)if(map[i][j]=='x')map[i][j]='X';
				step++;
			}
			System.out.println(ans);
		}
	}
}
