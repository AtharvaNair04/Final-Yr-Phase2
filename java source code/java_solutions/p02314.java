import java.util.Scanner;


class Main{
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int price=sc.nextInt();		//114514円
		int syurui=    sc.nextInt();	//効果の種類　11種類
            	int[] kouka=new int[syurui];    //硬貨の値段 810 1919 931 893 114 514 4545 36 24 170 74
		int INF=1919810;
		for(int i=0; i<syurui; i++) {
			kouka[i]=sc.nextInt();
		}

		int[][] dp=new int[syurui+1][200000];

		for(int i=0; i<200000; i++) {
			for(int j=0; j<=syurui; j++) {
				dp[j][i]=0;
			}
		}


		for(int i=1; i<200000; i++) {
			dp[0][i]=INF;
		}
		for(int i=1; i<=syurui; i++) {
			for(int j=0; j<200000; j++) {
				if(j>=kouka[i-1]) {
					dp[i][j]=Math.min(dp[i-1][j], dp[i][j-kouka[i-1]]+1);
				}
				else {
					dp[i][j]=dp[i-1][j];
				}
			}
		}

		if(dp[syurui][price]>=INF) {
			System.out.println("Impossible.");
		}
		else {
			System.out.println(dp[syurui][price]);
		}
	}
}
