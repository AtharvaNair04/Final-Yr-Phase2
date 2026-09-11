import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();//パスタを食べる期間
		int K = sc.nextInt();//決まってる日数
		int x[][] = new int[6][N];
		int m[] = new int[N];
		int sum = 0;
		int i,y;
		int z = 10000;//割る数
		
		for(i = 0; i < K; i++){
			y = sc.nextInt();//決まってる日
			m[y-1] = sc.nextInt();//メニュー
		}
		if(m[0] == 0){
			x[1][0] = 1;
			x[3][0] = 1;
			x[5][0] = 1;
		}
		else if(m[0] == 1){
			x[1][0] = 1;
		}
		else if(m[0] == 2){
			x[3][0] = 1;
		}
		else if(m[0] == 3){
			x[5][0] = 1;
		}
		for(i = 1; i < N; i++){
			if(m[i] == 1){
				x[0][i] = x[1][i-1];
				x[1][i] = (x[2][i-1] + x[3][i-1] + x[4][i-1] + x[5][i-1])%z;
			}
			else if(m[i] == 2){
				x[2][i] = x[3][i-1];
				x[3][i] = (x[0][i-1] + x[1][i-1] + x[4][i-1] + x[5][i-1])%z;
			}
			else if(m[i] == 3){
				x[4][i] = x[5][i-1];
				x[5][i] = (x[0][i-1] + x[1][i-1] + x[2][i-1] + x[3][i-1])%z;
			}
			else{
				x[0][i] = x[1][i-1];
				x[1][i] = (x[2][i-1] + x[3][i-1] + x[4][i-1] + x[5][i-1])%z;
				x[2][i] = x[3][i-1];
				x[3][i] = (x[0][i-1] + x[1][i-1] + x[4][i-1] + x[5][i-1])%z;
				x[4][i] = x[5][i-1];
				x[5][i] = (x[0][i-1] + x[1][i-1] + x[2][i-1] + x[3][i-1])%z;
			}
		}
		sum = x[0][N-1] + x[1][N-1] + x[2][N-1] + x[3][N-1] + x[4][N-1] + x[5][N-1];
		System.out.println(sum%10000);
	}

}
