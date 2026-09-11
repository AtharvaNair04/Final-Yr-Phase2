import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		init(1300000);
		Scanner sc = new Scanner(System.in);
		while(true) {
			int n = sc.nextInt();
			if (n==0) {
				break;
			}
			if (isPrime(n) || n == 1) {
				System.out.println(0);
			}else{
				int i = n+1;
				while(!isPrime(i)) {
					i++;
				}
				int higherp = i;
				i = n-1;
				while(!isPrime(i)) {
					i--;
				}
				int lowerp = i;
				System.out.println(higherp-lowerp);
			}
		}
	}

	private static long max_number;
	private static int max_number_sieve;
	private static boolean[] is_not_prime;
	private static ArrayList<Integer> prime = new ArrayList<Integer>();
	public static void init(long maxnum) {
		max_number = maxnum;
		max_number_sieve = (int) Math.sqrt(max_number);
		is_not_prime = new boolean[max_number_sieve+1];
		is_not_prime[0] = is_not_prime[1] = true;
		for(int i=2;i*i<=max_number_sieve;i++) {
			if (!is_not_prime[i]) {
				int j = 2;
				while(i*j<=max_number_sieve) {
					is_not_prime[i*j] = true;
					j++;
				}
			}
		}
		for(int i=2;i<=max_number_sieve;i++) {
			if(!is_not_prime[i]) {
				prime.add(i);
			}
		}
	}
	public static boolean isPrime(long n) {
		if (n>max_number) {
			return false;
		}
		if (n<=max_number_sieve) {
			return !is_not_prime[(int) n];
		}else{
			for(int p:prime) {
				if (n%p==0 && n!=p) {
					return false;
				}
			}
			return true;
		}
	}
	public static int countPrime(long first,long last) {
		if (first>last) {
			return 0;
		}
		if (last>max_number) {
			return -1;
		}
		int num = 0;
		for(int i=(int) first;i<=last;i++) {
			if (isPrime(i)) {
				num++;
			}
		}
		return num;
	}
	public static ArrayList<Integer> primeFactor(long n) {
		ArrayList<Integer> factor = new ArrayList<Integer>();
		for(int p:prime) {
			while(n%p==0) {
				n/=p;
				factor.add(p);
			}
		}
		return factor;
	}
	public static ArrayList<Factor> primeFactorExp(long n) {
		ArrayList<Factor> factor = new ArrayList<Factor>();
		if (n==0) {
			return factor;
		}
		for(int p:prime) {
			int q = 0;
			while(n%p==0) {
				n/=p;
				q++;
			}
			if (q>=1) {
				factor.add(new Factor(p,q));
			}
		}
		return factor;
	}
}
class Factor {
	int base;
	int exp;
	public Factor(int base,int exp) {
		this.base = base;
		this.exp = exp;
	}
	public String toString() {
		if (exp==1) {
			return Integer.toString(base);
		}else{
			return base + "^" + exp;
		}
	}
}
