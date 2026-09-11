import java.util.Scanner;
public class Main
{
	public static void main(String[] args) 
	{
		Scanner in=new Scanner(System.in);
		for(;;)
		{
			int z=in.nextInt();
			if(z==0)
				return;
			int max=0;
			int Z=(int)Math.pow(z, 3);
			
			for(int x=1;x*x*x<Z;x++)
				for(int y=1;x*x*x+y*y*y<Z;y++)
				{
					max=Math.max(max,x*x*x+y*y*y);
				}
			System.out.println((int)Math.pow(z, 3)-max);
		}
	}
}
