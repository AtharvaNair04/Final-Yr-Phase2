import java.util.*;

public class Main {
	
	public static void main(String args[]){
		Scanner scr = new Scanner(System.in);
		int t = Integer.parseInt(scr.nextLine());
		for(int step=0; step<t; step++){
			String[] str = scr.nextLine().split(" ", 0);
			int n = Integer.parseInt(str[0]);
			int k = Integer.parseInt(str[1]);
			int[] difs = new int[n-1];
			String[] strs = scr.nextLine().split(" ", 0);
			for(int i=1; i<n; i++){
				difs[i-1] = Integer.parseInt(strs[i]) - Integer.parseInt(strs[i-1]);
			}
			//xs[0] = Integer.MAX_VALUE;
			System.out.println(Setter(k, difs));
        }
		scr.close();
	}
	
	private static int Setter(int k, int[] arr){
		if(k>arr.length) return 0;
		Arrays.sort(arr);
		int len=0;
		for(int i=0; i<=arr.length-k; i++){
			len+=arr[i];
		}
		return len;
	}
	
}
