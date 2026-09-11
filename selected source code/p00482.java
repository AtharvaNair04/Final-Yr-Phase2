import java.util.Scanner;

//JOI Flag
public class Main{

	void run(){
		int MOD = 100000;
		char[] ch = {'J','O','I'};
		Scanner sc = new Scanner(System.in);
		int h = sc.nextInt(), w = sc.nextInt();
		int MASK = (1<<w)-1, mask = 1<<(w-1);
		int ALL = 1;
		char[][] map = new char[h][];
		for(int i=0;i<h;i++){
			map[i]=sc.next().toCharArray();
			for(int j=0;j<w;j++)if(map[i][j]=='?')ALL=(ALL*3)%MOD;
		}
		int[] ref = new int[15000], rev = new int[1<<w];
		int ID = 0;
		for(int S=0;S<1<<w;S++){
			if((S&1)>0)continue;
			boolean ok = true;
			for(int j=0;j+1<w;j++)if(((S>>j)&1)>0&&((S>>(j+1))&1)>0)ok = false;
			if(ok){
				rev[S] = ID;
				ref[ID++] = S;
			}
		}
		int[][][] dp = new int[2][ID][2];
		int X = 0;
		dp[1][0][0] = 1;
		for(int i=0;i<h;i++)for(int j=0;j<w;j++){
			for(int k=0;k<ID;k++)for(int J=0;J<2;J++)dp[X][k][J] = 0;
			for(int k=0;k<ID;k++)for(int J=0;J<2;J++){
				int S = ref[k];
				int D = dp[1-X][k][J];
				if(D==0)continue;
				if(map[i][j]!='?'){
					if((S&mask)>0&&map[i][j]=='I')continue;
					int ns = S;
					if(J==1&&map[i][j]=='O')ns++;
					ns<<=1;
					dp[X][rev[ns&MASK]][j==w-1?0:map[i][j]=='J'?1:0] = (dp[X][rev[ns&MASK]][j==w-1?0:map[i][j]=='J'?1:0]+D)%MOD;
				}
				else{
					for(int c=0;c<3;c++){
						char use = ch[c];
						if(use=='J'){
							int ns = S<<1;
							dp[X][rev[ns&MASK]][j==w-1?0:1] = (dp[X][rev[ns&MASK]][j==w-1?0:1]+D)%MOD;
						}
						else if(use=='O'){
							int ns = S;
							if(J==1)ns++;
							ns<<=1;
							dp[X][rev[ns&MASK]][0] = (dp[X][rev[ns&MASK]][0]+D)%MOD;
						}
						else{
							int ns = S<<1;
							if((S&mask)>0)continue;
							dp[X][rev[ns&MASK]][0] = (dp[X][rev[ns&MASK]][0]+D)%MOD;
						}
					}
				}
			}
			X = 1-X;
		}
		int res = 0;
		for(int k=0;k<ID;k++)for(int J=0;J<2;J++)res=(res+dp[1-X][k][J])%MOD;
		System.out.println((ALL-res+MOD)%MOD);
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
