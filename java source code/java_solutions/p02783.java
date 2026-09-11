import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan= new Scanner(System.in);
		int h=scan.nextInt();
		int a=scan.nextInt();int c=1;
		while(true)
		{
			
			if(h>a)
			{
				h-=a;
				c++;
			}
			else
			{
				System.out.println(c);
				break;
			}
		}

	}

}
