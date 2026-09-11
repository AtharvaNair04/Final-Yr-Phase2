import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int [] a=new int[5];
		int c=0;
		for(int i=0;i<5;i++)
		{
			a[i]=scan.nextInt();
			if(a[i]==0) c=i;
		}
		System.out.println(c+1);

	}

}
