import java.util.*;
public class Main {

	public void doIt(){
		final int MOD = 100000;
		Scanner sc = new Scanner(System.in);
		int x = sc.nextInt();
		int y = sc.nextInt();
		while(!(x == 0 && y == 0)){
			int [][][] list= new int[y+1][x+1][4];
			//初期値設定
			list[1][1][0] = 1;
			list[1][1][1] = 0;  //実質いらない
			list[1][1][2] = 1;
			list[1][1][3] = 0;  //実質いらない

			//表を埋める
			for(int i=1; i <= y; i++){
				for(int j=1; j <= x; j++){
					if(i == 1 && j == 1)
						continue;
					list[i][j][0] += list[i-1][j][3];
					list[i][j][1] += (list[i-1][j][0] + list[i-1][j][1]) % MOD;
					list[i][j][2] += list[i][j-1][1];
					list[i][j][3] += (list[i][j-1][2] + list[i][j-1][3]) % MOD;
				}
			}
			//合計を計算
			int sum = 0;
			for(int i=0; i < 4; i++){
				sum += list[y][x][i];
			}
			System.out.println(sum % MOD);

			x = sc.nextInt();
			y = sc.nextInt();
		}

	}
	public static void main(String[] args) {
		Main obj = new Main();
		obj.doIt();

	}

}
