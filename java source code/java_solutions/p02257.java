import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int primeNum = 0;

		for(int i = 0; i < n; i++) {

			int num = sc.nextInt();

			if(isPrime(num)) {
				primeNum++;
			}
		}
		System.out.println(primeNum);
	}

	static boolean isPrime(int num) {

		if(num == 2) {
			return true;
		}

		if(num % 2 == 0) {
			return false;
		}

		int i = 3;
		while(i <= Math.sqrt(num)) {

			if(num % i == 0) {
				return false;
			}

			i += 2;
		}

		return true;
	}
}
