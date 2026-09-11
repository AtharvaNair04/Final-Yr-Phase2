import java.util.Scanner;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int[] a = new int [N];
		for (int i = 0; i < N; i++) {
			a[i] = sc.nextInt();
		}
		Arrays.sort(a);
		long total = 0;
		int cen = (N-1)/2;
		if (N == 2)
			total = a[1] - a[0];
		
		else if (N % 2 == 0) {
			total += a[cen+1] - a[cen];
			for (int i = 0; i < cen; i++) {
				total += 2* (a[N-1-i] - a[i]);
			}
		}
		else {
		int total1 = 0;
		int total2 = 0;
		for ( int i = 0; i < cen-1; i++) {
			total += 2 * (a[N-1-i] - a[i]);
		}
		total1 += a[cen+1] + a[cen+1] - a[cen] - a[cen-1];
		total2 += a[cen] + a[cen+1] - a[cen-1] - a[cen-1];
		total += Math.max(total1, total2);
		}
		
		System.out.println(total);
		
	}
}
