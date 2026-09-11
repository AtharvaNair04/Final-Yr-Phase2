import java.awt.geom.*;
import java.io.*;
import java.util.*;
public class Main {
	
	//0115 start
	//0130 cording end
	
	int n;
	int [][][] data;
	boolean ans ;
	int [] vx = {0,0,1,0,-1};
	int [] vy = {0,1,0,-1,0};
	int [] dx = {0,0,1,1};
	int [] dy = {0,1,0,1};
	HashSet<String> close;
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		while(true){
			n = sc.nextInt();
			if(n == 0)break;
			data = new int[n][4][4];
			for(int i  =0; i < n; i++){
				for(int j = 0; j < 4; j++){
					for(int k = 0; k < 4; k++){
						data[i][j][k] = sc.nextInt();
					}
				}
			}
			
			ans = false;
			boolean first = false;
			close = new HashSet<String>();
			for(int i = 0; i < dx.length; i++){
				if(data[0][1 + dy[i]][1 + dy[i]] == 1){
					first = true;
					break;
				}
			}
			if(first){
				System.out.println(0);
				continue;
			}
			dfs(1,1,1,6,6,6,6);
			
			System.out.println(ans ? 1 : 0);
		}
	}

	int max = 0;
	
	private void dfs(int deep, int x, int y, int lt, int lb, int rt, int rb) {
		if(deep == n){
			ans = true;
			return;
		}
		
		for(int i = 0; i < vx.length; i++){
			for(int j = 1; j <= 2; j++){
				int xx = x + vx[i] * j;
				int yy = y + vy[i] * j;
				if(! isOK(xx, yy))continue;
				boolean flg = true;
				for(int  k = 0; k < dx.length;k++){
					int xxx = xx + dx[k];
					int yyy = yy + dy[k];
					if(data[deep][yyy][xxx] == 1){
						flg = false;
						break;
					}
				}
				if(! flg)continue;
				
				int nlt = lt-1, nlb = lb-1, nrt = rt-1, nrb = rb-1;
				if(xx == 0 && yy == 0){
					nlt = 7;
				}
				if(xx == 0 && yy == 2){
					nlb = 7;
				}
				if(xx == 2 && yy == 0){
					nrt = 7;
				}
				if(xx == 2 && yy == 2){
					nrb = 7;
				}
				
				if(nlt == 0 || nlb == 0 || nrt == 0 || nrb == 0) continue;
				String next = (deep+1) + " " + xx+yy+nlt+nlb+nrt+nrb;
				if(close.contains(next)) continue;
				close.add(next);
				dfs(deep +1, xx,yy, nlt, nlb, nrt, nrb);
			}
		}
	}

	private boolean isOK(int xx, int yy) {
		if(0<= xx && xx < 3 && 0<= yy && yy < 3){
			return true;
		}
		return false;
	}

	public static void main(String [] args){
		new Main().doit();
	}
}
