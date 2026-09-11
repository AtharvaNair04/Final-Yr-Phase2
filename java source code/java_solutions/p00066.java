import java.util.Scanner;

public class Main 
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		
		for(; scanner.hasNext();)
		{
			String input = scanner.next();			
			String[][] map = new String[3][3];
			
			for(int i=0; i<input.length(); i++)
			{
				map[i/3][i%3] = input.substring(i, i+1);
			}
			
			String output = "d";
			for(int i=0; i<3; i++)
			{
				int count = 1;
				for(int j=0; j<3; j++)
				{
					if(map[i][j].equals("s"))
					{
						break;
					}
					if(j > 0)
					{
						if(map[i][j].equals(map[i][j-1]))
						{
							count ++;
						}
						else {
							break;
						}
					}
				}
				
				if(count == 3)
				{
					output = map[i][0];
					break;
				}
			}
			
			
			for(int i=0; i<3; i++)
			{
				int count = 1;
				for(int j=0; j<3; j++)
				{
					if(map[j][i].equals("s"))
					{
						break;
					}
					if(j > 0)
					{
						if(map[j][i].equals(map[j-1][i]))
						{
							count ++;
						}
						else {
							break;
						}
					}
				}
				
				if(count == 3)
				{
					output = map[0][i];
					break;
				}
			}
			
			int count = 1;
			for(int i=0; i<3; i++)
			{
				if(map[i][i].equals("s"))
				{
					break;
				}
				if(i > 0)
				{
					if(map[i][i].equals(map[i-1][i-1]))
					{
						count ++;
					}
					else {
						break;
					}
				}
			}			
			if(count == 3)
			{
				output = map[0][0];
			}
			
			
			count = 1;
			for(int i=0; i<3; i++)
			{
				int j = 2-i;
				if(map[i][j].equals("s"))
				{
					break;
				}
				if(i > 0)
				{
					if(map[i][j].equals(map[i-1][j+1]))
					{
						count ++;
					}
					else {
						break;
					}
				}
			}
			
			if(count == 3)
			{
				output = map[0][2];
			}
			
			System.out.println(output);
		}
	}
}
