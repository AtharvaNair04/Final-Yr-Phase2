import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		int max = Integer.MIN_VALUE;
		
		int n = stdIn.nextInt();
		
		int k = stdIn.nextInt();
		
		int index = Integer.MAX_VALUE;
		
		int [] p = new int[n];
		
		for(int i = 0; i < n; i++) {
			p[i] = stdIn.nextInt();
		}
		
		/*for(int i = 0; i < n-k+1; i++) {  //ここTLE
			int sum = 0;
			for(int j = i; j < i+k; j++) {
				sum += p[j];
			}
			
			if(max < sum) {
				max = sum;
				index = i;
			}
			
		}*/
		int sum = 0;
		
		for(int i = 0; i < k; i++) {
			sum += p[i];
		}
		index = k-1;
		
		max = Math.max(sum, max);
		
		int right = 0;		
		for(int i = k; i < n; i++) {
			
			sum -= p[i-k];
			sum += p[i];
			
			if(max < sum) {
				max = sum;
				index = i;
			}
			
		}
		
		
		double ans = 0;
		
		for(int i = 0; i < k; i++) {
			
			ans += p[index--]/2.0+0.5;
			
			
		}
		
		System.out.println(ans);
		
	}

}
