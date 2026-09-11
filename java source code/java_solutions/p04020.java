import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		int n = stdIn.nextInt();
		long[] a = new long[n];
		
		for(int i = 0; i < n; i++) {
			a[i] = stdIn.nextLong();
		}
		
		long x = 0;
		long sum = 0;
		
		for(int i = 0; i < n; i++) {
			
			if(a[i] == 0) {
				sum += x/2;
				x = 0;
				continue;
			}
			if(i == n-1) {
				x += a[i];
				sum += x/2;
				break;
			}
			
			x += a[i];
			
		}
		System.out.println(sum);
		
		
	}

}
