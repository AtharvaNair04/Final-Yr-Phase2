import java.util.*;
public class Main {
	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		while(true) {
			int n = stdIn.nextInt();
			if(n == 0) {
				break;
			}
			int y = stdIn.nextInt();
			int[] b = new int[n];
			int[] r = new int[n];
			int[] t = new int[n];
			double[] ans = new double[100];
			for(int i = 0; i < n; i++) {
				b[i] = stdIn.nextInt();
				r[i] = stdIn.nextInt();
				t[i] = stdIn.nextInt();
			}
			for(int i = 0; i < n; i++) {
				if(t[i] == 1) {
					ans[b[i]-1] = 10000.0*(1.0 + (double)y * (double)r[i] * 0.01);
				}
				else {
					double tmp = 1.0;
					for(int j = 0; j < y; j++) {
						tmp *= 1.0 + (double)r[i]*0.01;
					}
					ans[b[i]-1] = 10000*tmp;
				}
			}
			double max = 0;
			int id = 0;
			for(int i = 0; i < 100; i++) {
				if(ans[i] > max) {
					max = ans[i];
					id = i+1;
				}
			}
			System.out.println(id);
		}
		
	}
}
