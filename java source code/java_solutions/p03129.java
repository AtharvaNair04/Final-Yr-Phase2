import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int K = sc.nextInt();
		int max = N/2;
		if(N%2 > 0) {
			max++;
		}
		if(max >= K) System.out.println("YES");
		else System.out.println("NO");
		
	}
	

}
