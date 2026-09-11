import java.*;
import java.math.BigInteger;
import java.util.Scanner;
public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		BigInteger num1 = new BigInteger("1");
		BigInteger result = new BigInteger("1");
		while(n>0) {
			num1 = BigInteger.valueOf(n);
			result = result.multiply(num1);
			n--;
		}
		System.out.println(result);
	}
}
