import java.util.Scanner;
 
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		long z = sc.nextInt();
		long w = sc.nextInt();
		long[] array = new long[n];
		boolean teban = true; //真ならAの手番
		for(int j = 0;j < n;++j){
			array[j] = sc.nextLong();
		}
		if(n >= 2){
			System.out.println(Math.max(Math.abs(array[n-1] - w), Math.abs(array[n-2] - array[n-1])));
		}else{
			System.out.println(Math.abs(array[n-1] - w));
		}
	}
}
