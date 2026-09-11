import java.util.*;

public class Main {
	int w, h, min;
	char[][] cmap;
	int[][] imap, ncost;
	ArrayList<int[]> list;
	
	int[] a = new int[]{0,0,1,-1};
	int[] b = new int[]{1,-1,0,0};
	
	void check(int[] perm){
		int[] perms = new int[list.size()];
		for(int i=0;i<list.size()-2;i++){
			perms[i+1] = perm[i]+1;
		}
		perms[list.size()-1] = list.size()-1;

		int sum = 0;
		for(int i=0;i<list.size()-1;i++){
			int cc = ncost[perms[i]][perms[i+1]];
			if(cc==0){
				calcCost(perms[i], perms[i+1]);
				cc = ncost[perms[i]][perms[i+1]];
			}
			sum += cc;
		}
		min = Math.min(min, sum);
	}
	
	void makeperm(int k, int[] perm, boolean[] flag){
		if(k==list.size()-2){
			check(perm);
		}else{
			for(int i=0;i<list.size()-2;i++){
				if(flag[i]==true) continue;
				perm[k] = i;
				flag[i] = true;
				makeperm(k+1, perm, flag);
				flag[i] = false;
			}
		}
	}
	
	
	void calcCost(int sid, int gid){
		ArrayDeque<Integer> y = new ArrayDeque<Integer>();
		ArrayDeque<Integer> x = new ArrayDeque<Integer>();
		int sy = list.get(sid)[0];
		int sx = list.get(sid)[1];
		int gy = list.get(gid)[0];
		int gx = list.get(gid)[1];
		y.offer(sy);
		x.offer(sx);
		
		int[][] cost = new int[h][w];
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				cost[i][j] = Integer.MAX_VALUE;
			}
		}
		cost[sy][sx] = 0;
		while(!y.isEmpty()){
			int ty = y.poll();
			int tx = x.poll();
			for(int i=0;i<4;i++){
				int ny = ty + a[i];
				int nx = tx + b[i];
				if(ny>=0 && ny<h && nx>=0 && nx<w && cmap[ny][nx]!='#'){
					if(cost[ny][nx] > cost[ty][tx]+imap[ty][tx]){
						cost[ny][nx] = cost[ty][tx]+imap[ty][tx];
						y.offer(ny);
						x.offer(nx);
					}
				}
			}
		}
		ncost[sid][gid] = cost[gy][gx];
	}
	
	void mark(int y, int x){
		int ty = Math.max(0, y-2);
		int ey = Math.min(h-1, y+2);
		int tx = Math.max(0, x-2);
		int ex = Math.min(w-1, x+2);
		
		for(int i=ty;i<=ey;i++){
			for(int j=tx;j<=ex;j++){
				imap[i][j] = Math.max(imap[i][j], 2);
			}
		}
		
		ty = Math.max(0, y-1);
		ey = Math.min(h-1, y+1);
		tx = Math.max(0, x-1);
		ex = Math.min(w-1, x+1);
		for(int i=ty;i<=ey;i++){
			for(int j=tx;j<=ex;j++){
				imap[i][j] = Math.max(imap[i][j], 3);
			}
		}
		
		imap[y][x] = 4;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		
		w = sc.nextInt();
		h = sc.nextInt();
		cmap = new char[h][w];
		imap = new int[h][w];
		list = new ArrayList<int[]>();
		int sy = -1;
		int sx = -1;
		int gy = -1;
		int gx = -1;
		for(int i=0;i<h;i++){
			String s = sc.next();
			for(int j=0;j<w;j++){
				char c = s.charAt(j);
				cmap[i][j] = c;
				if(c=='S'){
					sy = i;
					sx = j;
				}else if(c=='G'){
					gy = i;
					gx = j;
				}else if(c=='M'){
					list.add(new int[]{i, j});
				}else if(c=='#'){
					mark(i, j);
				}
			}
		}
		list.add(0, new int[]{sy,sx});
		list.add(new int[]{gy,gx});
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				if(imap[i][j]==0) imap[i][j] = 1;
			}
		}
		ncost = new int[list.size()][list.size()];
		min = Integer.MAX_VALUE;
		
		makeperm(0, new int[list.size()-2], new boolean[list.size()-2]);
		
		System.out.println(min);
	}
	
	public static void main(String[] args) {
		new Main().run();
	}	
}
