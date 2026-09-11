import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		
		long n = stdIn.nextLong(); //sの個数
		long m = stdIn.nextLong(); //cの個数
		long ans = 0;
		long ns = n;
		long mc = m;
		
		if(n>=1 && m == 1){
			ans = 0;
		}else {
			
			if(n*2 <= m) { //sの2倍の個数よりcが多い場合
				ans += n;
				mc -= 2*n;
				if(mc>=4) {
					ans += mc/4;
				}
				
			}else if(n <= m){
				ans += mc/2;
			}else { //m < n
				ans += mc/2;
			}
			
			
			
			
		}
		
		
		
		if(ans == 0) {
			System.out.println(ans);
		}else {
			System.out.println(ans);
		}
		
		
	}

}
