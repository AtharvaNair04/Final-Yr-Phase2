import java.util.Scanner;

public class Main {
	public static Scanner sc;
	
	public static void main(String [] args){
		sc = new Scanner(System.in);
		long a = sc.nextInt();
		long b = sc.nextInt();
		long c = sc.nextInt();
		
		if(a%2==0 || b%2==0 || c%2==0){
			System.out.println(0);
		} else {
			long e = a*b;
			long d = b*c;
			long f = c*a;
			
			long ans = Math.min(f,Math.min(d,e));
			System.out.println(ans);
		}
	}
}
