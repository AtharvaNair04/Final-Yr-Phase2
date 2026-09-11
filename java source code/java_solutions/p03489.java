import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in=new Scanner(System.in);
		int num=in.nextInt();
		int[] arr=new int[num];
		
		for(int i=0;i<num;i++)
		{
			arr[i]=in.nextInt();
		}
		
		Map<Integer,Integer> map=new HashMap<>();
		
		for(int i=0;i<num;i++)
		{
			if(map.containsKey(arr[i])==true)
			{
				map.put(arr[i], map.get(arr[i])+1);
			}
			else
			{
				map.put(arr[i], 1);
			}
		}
		
		int count=0;
		
		for(Map.Entry<Integer, Integer> el:map.entrySet())
		{
			int key=el.getKey();
			int value=el.getValue();
			
			if(value>key)
			{
				count=count+(value-key);
			}
			else if(value<key)
			{
				count=count+value;
			}
		}
		
		System.out.println(count);

	}

}
