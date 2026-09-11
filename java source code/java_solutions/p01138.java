import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Main {

	public static void main(String[] args)
	{
		
		Scanner sca = new Scanner(System.in);
		while(true)
		{
			int n = sca.nextInt();
			if(n == 0) break;
			
			int[] start = new int[n];
			int[] end = new int[n];
			int[] used = new int[n];
			int i, j, k;
			
			for(i = 0;i < n;i++)
			{
				String text;
				int h, m, s;
				
				text = sca.next();
				h = (text.charAt(0) - '0') * 10 + (text.charAt(1) - '0');
				m = (text.charAt(3) - '0') * 10 + (text.charAt(4) - '0');
				s = (text.charAt(6) - '0') * 10 + (text.charAt(7) - '0');
				start[i] = h * 60 * 60 + m * 60 + s;
				
				text = sca.next();
				h = (text.charAt(0) - '0') * 10 + (text.charAt(1) - '0');
				m = (text.charAt(3) - '0') * 10 + (text.charAt(4) - '0');
				s = (text.charAt(6) - '0') * 10 + (text.charAt(7) - '0');
				end[i] = h * 60 * 60 + m * 60 + s;
				
				used[i] = 0;
			}
			
			for(i = n;i > 0;i--)
			{
				for(j = 0;j < (i - 1);j++)
				{
					if(start[j] > start[j + 1])
					{
						int tmp;
						tmp = start[j];   start[j] = start[j + 1];   start[j + 1] = tmp;
						tmp = end[j];   end[j] = end[j + 1];   end[j + 1] = tmp;
					}
				}
			}
			
			int train = 0;
			while(true)
			{
				int t = 0;
				for(i = 0;i < n;i++)
				{
					if(used[i] == 1) continue;
					if(t <= start[i])
					{
						used[i] = 1;
						t = end[i];
					}
				}
				train++;
				
				for(i = 0;i < n;i++)
					if(used[i] == 0) break;
				if(i == n) break;
			}
			
			System.out.println(train);
		}
	}
}
