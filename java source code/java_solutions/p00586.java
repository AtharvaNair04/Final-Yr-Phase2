import java.util.*;

public class Main
{
	public static void main(String[] args)
	{
		int a, b;
		Scanner jin = new Scanner(System.in);
		while (jin.hasNextInt())
		{
			a = jin.nextInt();
			b = jin.nextInt();
			System.out.println(a + b);
		}
	}
}
