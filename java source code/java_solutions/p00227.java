import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			int n = sc.nextInt();
			int m = sc.nextInt();
			if (n == 0) {
				break;
			}
			int[] p = new int[n];
			for(int i=0;i<n;i++) {
				p[i] = sc.nextInt();
			}
			Arrays.sort(p);
			int ans = 0;
			for(int i=0;i<n%m;i++) {
				ans += p[i];
			}
			for(int i=0;i<n/m;i++) {
				for(int j=1;j<m;j++) {
					ans += p[n%m+m*i+j];
				}
			}
			System.out.println(ans);
		}
	}

}
