import java.util.*;
public class Main{
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int k = sc.nextInt();
		int N = sc.nextInt();
		int max = 0;
		int data[] = new int[N];
		int data1[] = new int[N];
		for(int i = 0;i<N;i++) {
			data[i] = sc.nextInt();
		}
		max =Math.abs(k-data[N-1] + data[0]);
		for(int i =0; i<N-1; i++) {
			    max = Math.max (data[i+1]-data[i],max);
		
		}
	
		System.out.println(k-max);
	}

}
