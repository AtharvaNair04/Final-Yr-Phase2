import java.util.*;

public class Main {
	int INF = 1 << 24;
	int n;
	int [][] data, work, ans;
	int [] vx = {0,-1,0,1};
	int [] vy = {0,0,1,0};
	boolean flg;
	
	private void doit(){
		Scanner sc = new Scanner(System.in);
		int dataset = sc.nextInt();
		while(dataset-- > 0){
			n = 10;
			data = new int[n][n];
			for(int i = 0 ; i < n; i++){
				for(int j = 0 ; j < n; j++){
					data[i][j] = sc.nextInt();
				}
			}
			work = new int[n][n];
			ans = new int[n][n];
			flg = false;
			dfs(0);
			
			for(int i = 0 ; i < n; i++){
				for(int j = 0 ; j < n; j++){
					System.out.print(ans[i][j]);
					if(j != 9){
						System.out.print(" ");
					}
					else{
						System.out.println();
					}
				}
			}
		}
	}

	private void dfs(int deep) {
		if(flg) return;
		if(deep == 10){
			boolean res = check();
			if(! res) return;
			flg = true;
			for(int i = 0 ; i < n; i++){
				for(int j = 0 ; j < n; j++){
					ans[i][j] = work[i][j];
				}
			}
			return ;
		}
		for(int i = 0 ; i <= 1; i++){
			work[0][deep] = i;
			for(int j = 0; j < vx.length; j++){
				int x = vx[j] + deep;
				int y = vy[j];
				if(! isOK(x,y)) continue;
				data[y][x] = data[y][x] ^ i;
			}
			dfs(deep + 1);
			for(int j = 0; j < vx.length; j++){
				int x = vx[j] + deep;
				int y = vy[j];
				if(! isOK(x,y)) continue;
				data[y][x] = data[y][x] ^ i;
			}
		}
	}

	private boolean isOK(int x, int y) {
		if(0 <= x && x < n && 0 <= y && y < n){
			return true;
		}
		return false;
	}

	private boolean check() {
		int [][] temp = new int[n][n];
		for(int i = 0 ; i < n;i++){
			for(int j = 0 ; j < n; j++){
				temp[i][j] = data[i][j];
			}
		}
		
		for(int i = 1; i < n; i++){
			for(int j = 0 ; j < n; j++){
				if(temp[i-1][j] == 1){
					work[i][j] = 1;
					temp[i-1][j] = 0;
					for(int k = 0; k < vx.length; k++){
						int x = vx[k] + j;
						int y = vy[k] + i;
						if(! isOK(x,y)) continue;
						temp[y][x] = temp[y][x] ^ 1;
					}
				}
				else{
					work[i][j] = 0;
				}
			}
		}
		for(int i = 0 ; i < n; i++){
			if(temp[9][i] == 1){
				return false;
			}
		}
		return true;
	}

	public static void main(String [] args){
		new Main().doit();
	}
}
