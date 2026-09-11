import java.util.*;

class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		long A = sc.nextLong();
		long B = sc.nextLong();
		long K = sc.nextLong();
		
		if (A >= K) {
			System.out.println(A-K + " " + B);
		} else {
			System.out.println(0 + " " + Math.max(0, B-K+A));
		}
	}
}
