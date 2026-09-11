import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

//Dr. Nakamura's Lab.
public class Main{

	int h, w;
	char[][] map;
	int[][] move = {{-1,0},{0,1},{1,0},{0,-1}};
	
	long trans(int i, int j, int c0, int c1, int c2, int c3, int c4, int c5, int s){
		long res = i*w+j;
		res*=100; res+=c0*w+c1;
		res*=100; res+=c2*w+c3;
		res*=100; res+=c4*w+c5;
		res*=10; res+=s;
		return res;
	}
	int dist(int i, int j, int a, int b){
		return Math.abs(i-a)+Math.abs(j-b);
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			h = sc.nextInt(); w = sc.nextInt();
			if((h|w)==0)break;
			map = new char[h][w];
			int panel = 0;
			int si=0, sj=0, gi=0, gj=0;
			int[][] container = new int[3][2];
			int con = 0;
			for(int i=0;i<h;i++){
				map[i] = sc.next().toCharArray();
				for(int j=0;j<w;j++){
					if(map[i][j]=='@'){
						map[i][j] = '.'; si=i; sj=j;
					}
					else if(map[i][j]=='E'){
						map[i][j] = '.'; gi=i; gj=j;
					}
					else if(map[i][j]=='c'){
						map[i][j] = '.';
						container[con][0] = i; container[con][1] = j;
						con++;
					}
					else if(map[i][j]=='w'){
						map[i][j] = (char)(panel+'0');
						panel++;
					}
				}
			}
			Set<Long> u = new HashSet<Long>();
			List<Long> l = new ArrayList<Long>();
			long start = trans(si, sj, container[0][0], container[0][1], container[1][0], container[1][1], container[2][0], container[2][1], 0);
			l.add(start);
			u.add(start);
			int ans = -1, step = 0;
			while(!l.isEmpty()){
				List<Long> next = new ArrayList<Long>();
				for(long v:l){
					int state = (int) (v%10); v/=10;
					int con2 = (int) (v%100); v/=100;
					int con1 = (int) (v%100); v/=100;
					int con0 = (int) (v%100); v/=100;
					int pos = (int) v;
					int c0 = con0/w, c1 = con0%w;
					int c2 = con1/w, c3 = con1%w;
					int c4 = con2/w, c5 = con2%w;
					int i = pos/w, j = pos%w;
//					System.out.println("Step:" + step+"State:"+state +" 1:"+c0+","+c1+" 2:"+c2+","+c3+" 3:"+c4+","+c5+ " POS:"+i+","+j);
//					System.out.println(i+","+j);
					if(i==gi&&j==gj){
						ans = step; next.clear(); break;
					}
					//decide the block move
					for(int z=0;z<8;z++){
						int nc0=c0, nc1=c1, nc2=c2, nc3=c3, nc4=c4, nc5=c5, nstate=state;
//						System.out.println("Pos:"+i+","+j+" B1:"+(z&1)+" B2:"+(z&2)+" B3:"+(z&4));
						if(((z>>0)&1)>0){
							if(dist(i, j, c0, c1)!=1)continue;
							int dy = c0-i, dx = c1-j;
							int y = c0, x = c1;
							for(;;){
								int ny = y+dy, nx = x+dx;
								if(map[ny][nx]=='#'||ny==c2&&nx==c3||ny==c4&&nx==c5){
									nc0 = y; nc1 = x; break;
								}
								if(map[ny][nx]!='.'){
									int id = map[ny][nx]-'0';
									if(((nstate>>id)&1)==0){
										nc0 = nc1 = 0; nstate|=1<<id; break;
									}
								}
								y = ny; x = nx;
							}
						}
						if(((z>>1)&1)>0){
							if(dist(i, j, c2, c3)!=1)continue;
							int dy = c2-i, dx = c3-j;
							int y = c2, x = c3;
							for(;;){
								int ny = y+dy, nx = x+dx;
								if(map[ny][nx]=='#'||ny==c0&&nx==c1||ny==c4&&nx==c5){
									nc2 = y; nc3 = x; break;
								}
								if(map[ny][nx]!='.'){
									int id = map[ny][nx]-'0';
									if(((nstate>>id)&1)==0){
										nc2 = nc3 = 0; nstate|=1<<id; break;
									}
								}
								y = ny; x = nx;
							}
						}
						if(((z>>2)&1)>0){
							if(dist(i, j, c4, c5)!=1)continue;
							int dy = c4-i, dx = c5-j;
							int y = c4, x = c5;
							for(;;){
								int ny = y+dy, nx = x+dx;
								if(map[ny][nx]=='#'||ny==c0&&nx==c1||ny==c2&&nx==c3){
									nc4 = y; nc5 = x; break;
								}
								if(map[ny][nx]!='.'){
									int id = map[ny][nx]-'0';
									if(((nstate>>id)&1)==0){
										nc4 = nc5 = 0; nstate|=1<<id; break;
									}
								}
								y = ny; x = nx;
							}
						}
						for(int k=0;k<4;k++){
							int ni = i+move[k][0], nj = j+move[k][1];
							if(map[ni][nj]=='#')continue;
							if(ni==nc0&&nj==nc1||ni==nc2&&nj==nc3||ni==nc4&&nj==nc5)continue;
							if(map[ni][nj]!='.'){
								int id = map[ni][nj]-'0';
								if(((nstate>>id)&1)>0){
									long ns = trans(ni, nj, nc0, nc1, nc2, nc3, nc4, nc5, nstate);
									if(!u.contains(ns)){
										u.add(ns); next.add(ns);
									}
								}
							}
							else{
								long ns = trans(ni, nj, nc0, nc1, nc2, nc3, nc4, nc5, nstate);
								if(!u.contains(ns)){
									u.add(ns); next.add(ns);
								}
							}
						}
					}
				}
				l = next; step++;
			}
			System.out.println(ans);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
