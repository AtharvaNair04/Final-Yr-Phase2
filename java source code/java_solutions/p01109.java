import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int avg, sum, count, n, i;
		while(true){
			avg = 0;sum = 0;count = 0;
			n = sc.nextInt();
			if(n == 0) break;
			int a[] = new int[n];
			for( i  = 0; i < n; i++) {
				a[i]= sc.nextInt();
				sum += a[i];
			}
			avg = sum / n;
			for( i  = 0; i < n; i++) {
				if(a[i] <= avg )	count++;
			}
			System.out.println(count);
		}
	}
}
