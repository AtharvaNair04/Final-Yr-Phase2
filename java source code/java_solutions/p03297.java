import java.util.*;

class Main {
	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);
		int N = in.nextInt();
		in.nextLine();
		for (int i = 0; i < N; i++) {
			long rest=in.nextLong();
			long buy=in.nextLong();
			long min=in.nextLong();
			long add=in.nextLong();
			if(buy>rest||buy>add) {
				System.out.println("No");
				continue;
			} else if(buy<min && add>=buy) {
				System.out.println("Yes");
				continue;
			}
			long dif=gcd(add, buy);
			long restMin = rest%dif;
			if(restMin<min) {
				restMin += dif*((min-restMin)/dif);
			}
			if(restMin<=min) {
				restMin += dif;
			}
			if(restMin<buy) {
				System.out.println("No");
			}else {
				System.out.println("Yes");
			}
		}
		in.close();
	}
	static long gcd (long a, long b) {return b>0?gcd(b,a%b):a;}
}
