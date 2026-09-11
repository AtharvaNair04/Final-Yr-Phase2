import java.util.Scanner;
public class Main {
	static long cnt(long n) {
		long count = 0;
		if(n %2 == 0) {
			long k = 10;
			
			while(k <= n) {
				count += n/k;
				k *= 5;
			}
		}
		
		return count;
		
		
		
	}



	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		long  n = stdIn.nextLong();

		long a = cnt(n);
		
		System.out.println(a);

	}

}
