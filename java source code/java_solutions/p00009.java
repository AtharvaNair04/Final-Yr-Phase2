import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int status[]=new int[1000100];
		status[0]=1;
		status[1]=1;
		for(int i=4;i<=1000000;i=i+2)
		{
			status[i]=1;
		}
		for(int i=3;i*i<=1000000;i=i+2)
		{
			if(status[i]==1)continue;
			for(int j=i*i;j<=1000000;j=j+i*2)
			{
				status[j]=1;
			}
		}
		while(sc.hasNext())
		{
			int a=sc.nextInt();
			int p=0;
			for(int i=1;i<=a;i++)
			{
				if(status[i]==0)p++;
			}
			System.out.println(p);
		}
	
			
		

	}

}
