import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

//Off Balance
public class Main{

	static class P{
		public int id;
		public int[][] piece;
		public List<P> sup;
		public List<P> under;
		public Set<Integer> set;
		public double g;
		public int xl;
		public int xr;
		public P(int id) {
			this.id = id;
			set = null;
			piece = new int[4][2];
			sup = new ArrayList<P>();
			under = new ArrayList<P>();
			g = -1;
			xl = xr = -1;
		}
		public boolean isUnder(int k){
			boolean f = false;
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					if(piece[i][0]+1==ps[k].piece[j][0] && piece[i][1]==ps[k].piece[j][1])f=true;
				}
			}
			return f;
		}
		public boolean isSup(int k){
			boolean f = false;
			for(int i=0;i<4;i++){
				for(int j=0;j<4;j++){
					if(piece[i][0]-1==ps[k].piece[j][0] && piece[i][1]==ps[k].piece[j][1])f=true;
				}
			}
			return f;
		}
		public boolean stable(){
			double xl = getXL();
			double xr = getXR();
			double g = getG();
			return xl < g && g < xr;
		}
		public Set<Integer> getSet(){
			if(set!=null)return set;
			Set<Integer> result = new HashSet<Integer>();
			result.add(this.id);
			for(P p:sup)result.addAll(p.getSet());
			return set = result;
		}
		public double getG(){
			if(g!=-1)return g;
			double x = 0;
			for(int i:getSet()){
				for(int j=0;j<4;j++)
					x+=ps[i].piece[j][1]+0.5;
			}
			return g = x/(4*set.size());
		}
		public int getXL(){
			if(xl!=-1)return xl;
			if(under.isEmpty()){
				int minx = Integer.MAX_VALUE;
				for(int i=0;i<4;i++){
					if(piece[i][0]==h-1)minx = Math.min(minx, piece[i][1]);
				}
				return xl = minx;
			}
			int x = Integer.MAX_VALUE;
			for(int i=0;i<4;i++){
				for(P p:under){
					for(int j=0;j<4;j++){
						if(piece[i][0]+1==p.piece[j][0] && piece[i][1] == p.piece[j][1])x=Math.min(x, piece[i][1]);
					}
				}
			}
			return xl = x;
		}
		public int getXR(){
			if(xr!=-1)return xr;
			if(under.isEmpty()){
				int maxx = Integer.MIN_VALUE;
				for(int i=0;i<4;i++){
					if(piece[i][0]==h-1)maxx = Math.max(maxx, piece[i][1]);
				}
				return xr = maxx+1;
			}
			int x = Integer.MIN_VALUE;
			for(int i=0;i<4;i++){
				for(P p:under){
					for(int j=0;j<4;j++){
						if(piece[i][0]+1==p.piece[j][0] && piece[i][1] == p.piece[j][1])x=Math.max(x, piece[i][1]);
					}
				}
			}
			return xr = x+1;
		}
	}

	static char[][] map;
	static int w;
	static int h;

	static P[] ps;
	static int id;

	static int[][] move = {{1,0},{-1,0},{0,1},{0,-1}};

	static int[][] tmp;
	static int tmpK;
	static void dfs(int i, int j, char ch){
		if(tmpK==4)return;
		if(map[i][j]!=ch)return;
		tmp[tmpK][0] = i;
		tmp[tmpK][1] = j;
		tmpK++;
		map[i][j] = '.';
		for(int k=0;k<4;k++){
			int ni = i+move[k][0];
			int nj = j+move[k][1];
			if(0<=ni&&ni<h&&0<=nj&&nj<w)
				dfs(ni, nj, ch);
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true){
			w = sc.nextInt();
			h = sc.nextInt();
			if((w|h)==0)break;
			map = new char[h][w];
			for(int i=0;i<h;i++)map[i]=sc.next().toCharArray();
			ps = new P[150];
			id = 0;
			for(int i=0;i<h;i++){
				for(int j=0;j<w;j++){
					if(map[i][j]!='.'){
						tmp = new int[4][2];
						tmpK = 0;
						dfs(i, j, map[i][j]);
						ps[id] = new P(id);
						ps[id].piece = tmp;
						id++;
					}
				}
			}
			for(int i=0;i<id;i++){
				for(int j=0;j<id;j++){
					if(i==j)continue;
					if(ps[i].isSup(j)){
						ps[i].sup.add(ps[j]);
					}
					if(ps[i].isUnder(j))ps[i].under.add(ps[j]);
				}
			}
			boolean f = true;
			for(int i=0;i<id;i++){
				if(!ps[i].stable()){
					f = false;
					break;
				}
			}
			System.out.println(f?"STABLE":"UNSTABLE");
		}
	}
}
