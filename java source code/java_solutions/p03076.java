import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		int [] ryouri = new int[5];
		ryouri[0] = stdIn.nextInt();
		ryouri[1] = stdIn.nextInt();
		ryouri[2] = stdIn.nextInt();
		ryouri[3] = stdIn.nextInt();
		ryouri[4] = stdIn.nextInt();
		
		int [] time = new int[5];
		int cnt = 0;		
			
				for(int k = 1; k <=9; k++) {
					for(int j = 0; j <= 4; j++) {
					if(ryouri[j] % 10 == k) {
						time[cnt++] = ryouri[j];
					}
				}
			}
				for(int i = 0; i < time.length; i++) {
					if(ryouri[i] % 10 == 0) {
						time[cnt++] = ryouri[i];
					}
				}
		
		int ans = 0;
		for(int i = 4; i >= 0; i--) {
			if(i ==0) {
				ans += time[0];
				break;
			}
			while(true) {
				if(time[i]%10==0) {
					break;
				}
				time[i]++;
			}
			
			ans += time[i];
			
			
		}
			
		
		System.out.println(ans);
		
		
		
		
		
	}

}
