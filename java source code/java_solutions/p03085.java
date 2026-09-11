import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		char in = scanner.next()
						 .charAt(0);
		char c = 0;
		switch (in)
		{
			case 'A':
				c = 'T';
				break;
			case 'T':
				c = 'A';
				break;
			case 'G':
				c = 'C';
				break;
			case 'C':
				c = 'G';
				break;
		}
		System.out.println(c);
	}
}
