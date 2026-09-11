import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int k = sc.nextInt();
		long[] x = new long[n + 1];
		long[] sum = new long[n + 1];
		sum[0] = 0;
		for(int i = 1; i < n + 1; i++) {
			x[i] = sc.nextLong();
			if(i == 1)
				sum[i] = 0;
			else {
				sum[i] = sum[i - 1] + x[i] - x[i - 1];
			}
		}
		long ans = Long.MAX_VALUE;
		for(int i = 1; k - 1 + i <= n; i++) {
			int tmp = k - 1 + i;
			long anstmp = 0;
			anstmp += sum[tmp] - sum[i];
			anstmp += Math.min(Math.abs(x[i]), Math.abs(x[tmp]));
			ans = Math.min(ans, anstmp);
		}
		System.out.println(ans);

	}

}
