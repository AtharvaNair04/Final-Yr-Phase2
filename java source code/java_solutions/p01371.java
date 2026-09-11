import java.util.Scanner;

class Main {
	public static void pr(Object o) {
		System.out.println(o);
	}

	int[][] t;

	int n;




	int[] hash;//ハッシュ。



	int min;

	public void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			n = sc.nextInt();
			min=Integer.MAX_VALUE;
			if (n == 0) {
				break;
			}
			hash=new int[1<<n];
			for(int i=0;i<(1<<n);i++){
				hash[i]=Integer.MAX_VALUE;
			}

			t = new int[n + 1][n + 1];
			for (int i = 0; i < n; i++)
				for (int j = 0; j <= n; j++) {
					t[i][j] = sc.nextInt();
				}

			hash[0]=0;//ステージを順に追加していく動的計画法
			for(int i=0;i<(1<<n);i++){
				for(int stage=0;stage<n;stage++){
					if((i & (1<< stage))!=0){
						continue;
					}
					hash[i+(1<<stage)]=Math.min(hash[i]+t[stage][0],hash[i+(1<<stage)]);

					for(int buki=0;buki<n;buki++){
						if((i & (1<< buki))==0){
							continue;
						}
						hash[i+(1<<stage)]=Math.min(hash[i]+t[stage][buki+1],hash[i+(1<<stage)]);
					}

 				}
			}
			pr(hash[(1<<n)-1]);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
