import java.util.Scanner;


public class Main
{
	public static void main(String[] args) 
	{
		int m,n,p;
		Scanner scanner=new Scanner(System.in);		
		while(true)
		{
			n=scanner.nextInt();
			m=scanner.nextInt();
			p=scanner.nextInt();
			if ((n | m | p) == 0)
				break;
			int [] x=new int[n];
			double money=0.0;
			for(int i=0;i<n;i++)
			{
				x[i]=scanner.nextInt();
				money+=100*x[i];
			}
			money-=money*p/100;
			if(x[m-1]==0)
				System.out.println(0);
			else
				System.out.println((int)(money/x[m-1]));
		}
	}
}
