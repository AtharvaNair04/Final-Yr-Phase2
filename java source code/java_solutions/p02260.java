import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] arr = new int[n];
		for(int i=0;i<arr.length;i++){
			arr[i] = sc.nextInt();
		}
		int c=0;
		for(int i=0;i<arr.length;i++){
			int mini = i;
			for(int j=i;j<arr.length;j++){
				if(arr[j]<arr[mini])
					mini = j;
			}
			if(i!=mini){
				int tmp = arr[i];
				arr[i] = arr[mini];
				arr[mini] = tmp;
				c++;
			}
		}
		for(int i=0;i<arr.length;i++){
			if(i!=arr.length-1)
				System.out.print(arr[i]+" ");
			else
				System.out.println(arr[i]);
		}
		System.out.println(c);
	}
}
