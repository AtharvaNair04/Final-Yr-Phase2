import java.util.Scanner;


public class Main
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		
		for(; ;)
		{
			int n = scanner.nextInt();
			if(n == 0)
			{
				break;
			}
			
			int count = 0;
			int[] s = new int[n];
			int[] c = new int[n];
			int[] p = new int[n];
			boolean is_input = true;
			for(; ;)
			{
				int[] num_cnt = new int[100];
				for(int i=0; i<n; i++)
				{
					if(is_input)
					{
						s[i] = scanner.nextInt();
					}
					else
					{
						s[i] = c[i];
					}
					
					num_cnt[s[i]-1] ++;
				}
				
				
				for(int i=0; i<n; i++)
				{
					c[i] = num_cnt[s[i]-1];
				}
				
				boolean is_loopend = true;
				for(int i=0; i<n; i++)
				{
					if(is_input)
					{
						if(n == s[0])
						{
							if(s[0] != s[i])
							{
								is_loopend = false;
							}
						}
						else
						{
							is_loopend = false;
						}
					}
					else
					{
						if(p[i] != c[i])
						{
							is_loopend = false;
						}
					}
				}
				
				if(is_loopend)
				{
					System.out.println(count);
					for(int i=0; i<n; i++)
					{
						System.out.print((i>0?" ":"") + c[i]);
					}
					System.out.println("");
					break;
				}
				
				count ++;
				is_input = false;
				for(int i=0; i<n; i++)
				{
					p[i] = c[i];
				}
			}
		}
	}
}
