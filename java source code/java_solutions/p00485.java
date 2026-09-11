import java.util.Arrays;
import java.util.Scanner;
public class Main
{
	public static void main(String[] args) 
	{
		Scanner in=new Scanner(System.in);
		int M=50000000;
		while(in.hasNext())
		{
			int n=in.nextInt(), m=in.nextInt(),K=in.nextInt();
			double minmax=0;
			int cost[][]=new int[n+1][n+1];
			for(int i=0;i<=n;i++)
				Arrays.fill(cost[i], M);
			for(int i=0;i<=n;i++)
				cost[i][i]=0;
			String cap[]=new String[m];
			for(int i=0;i<m;i++)
			{
				int a=in.nextInt(),b=in.nextInt(),l=in.nextInt();
				cap[i]=a+" "+b+" "+l;
				cost[a][b]=l;
				cost[b][a]=l;
			}
			int fmo=in.nextInt();
			int startmo=fmo;
			if(K>1)
				for(int i=1;i<K;i++)
				{
					int nexmo=in.nextInt();
					cost[fmo][nexmo]=0;
					cost[nexmo][fmo]=0;
					fmo=nexmo;
				}
			int leng[]=new int[n+1];// minimum distance
			int v[] = new int[n+1];// decision flag

			int p=0;
			int start =startmo;
			for(int k=1;k<=n;k++)
			{
				leng[k]=M;
				v[k]=0;
			}
			leng[start]=0;
			for(int j=1;j<=n;j++)
			{
				int min=M;//最小の節点を探す
				for(int k=1;k<=n;k++)
				{
					if(v[k]==0&&leng[k]<min)
					{
						p=k;
						min=leng[k];
					}
				}
				v[p]=1;//確定フラグ
				for(int k=1;k<=n;k++) //pを経由してkに至長さがそれまでの最短経路より小さければ更新
					leng[k]=Math.min(leng[p]+cost[p][k], leng[k]);
			}
			for(int i=0;i<m;i++)
			{
				String ss[]=cap[i].split(" ");
				int a=Integer.valueOf(ss[0]);
				int b=Integer.valueOf(ss[1]);
				int cos=Integer.valueOf(ss[2]);
				double d=(leng[a]+leng[b]+cos)/2.0;
				minmax=Math.max(minmax, d);
			}
			System.out.println(Math.round(minmax));
		}
	}
}
