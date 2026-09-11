import java.util.Arrays;
import java.util.Scanner;
public class Main
{
	public static void main(String arg[])
	{
		Scanner in=new Scanner(System.in);
		int M=5000001;
		for(;;)
		{
			int c=in.nextInt();//チケットの数
			int n=in.nextInt(),m=in.nextInt();//バス停の数,道の数
			int s=in.nextInt(),d=in.nextInt();//スタート&ゴール
			if((c|n|m|s|d)==0)
				return;
			int cost[][]=new int[n+1][n+1];
			for(int i=0;i<=n;i++)
				Arrays.fill(cost[i], M);
//			for(int q[]: cost) {
//				Arrays.fill(q, M);
//			}
			for(int i=1;i<=n;i++)
				cost[i][i]=0;
			for(int i=0;i<m;i++)
			{
				int a=in.nextInt();
				int b=in.nextInt();
				int f=in.nextInt();
				cost[a][b]=f;
				cost[b][a]=f;
			}
			int p=0,w=0;
			int v[][] = new int[n+1][c+1];// decision flag
			int leng[][]=new int[n+1][c+1];// minimum distance
			for(int i=0;i<=n;i++)
				for(int k=0;k<=c;k++)
				{
					leng[i][k]=M;
					v[i][k]=0;
				}
			leng[s][c]=0;
			for(;;)
			{
				int min=M;//最小の節点を探す
				for(int l=1;l<=n;l++)
					for(int k=0;k<=c;k++)
					{
						if(v[l][k]==0&&leng[l][k]<min)
						{
							p=l;
							w=k;
							min=leng[l][k];
						}
					}
				if(min==M)
					break;
				v[p][w]=1;//確定フラグ
				for(int l=1;l<=n;l++) //pを経由してlに至る長さがそれまでの最短経路より小さければ更新
				{
					leng[l][w]=Math.min(leng[l][w], leng[p][w]+cost[p][l]);
					if (w>0)
						leng[l][w-1]=Math.min(leng[l][w-1], leng[p][w]+cost[p][l]/2);
				}
			}
			int ans=M;
			for(int i=0;i<=c;i++)
				ans=Math.min(ans, leng[d][i]);
			System.out.println(ans);
		}
	}
}
