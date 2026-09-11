import java.util.*;
public class Main 
{
	public static void main(String[] args) 
	{
		Scanner stdIn = new Scanner(System.in);
		while(stdIn.hasNext())
		{
			int n = stdIn.nextInt();
			if(n == 0)
			{
				break;
			}
			List<Integer> list = new ArrayList<Integer>();
			for(int i = 0; i < n; ++i)
			{
				int a = stdIn.nextInt();
				list.add(a);
			}
			int ohajiki = 32;
			int pos = 0;
			boolean endFlag = false;
			while(!endFlag)
			{
				ohajiki -= (ohajiki - 1) % 5;
				System.out.println(ohajiki);
				ohajiki -= list.get(pos);
				if(ohajiki <= 0)
				{
					endFlag = true;
					System.out.println("0");
					continue;
				}
				else
				{
					System.out.println(ohajiki);
				}
				++pos;
				if(pos >= n)
				{
					pos = 0;
				}
			}
		}
	}
}
