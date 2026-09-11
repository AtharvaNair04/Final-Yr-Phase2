import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static ArrayList<String> allpermutations;
	public static void main(String[] args){
		Scanner cin = new Scanner(System.in);
		long a;
		long b;
		BigInteger aVal;
		BigInteger bVal;
		BigInteger gcd;
		BigInteger lcm;
		while(cin.hasNextLong()){
			a = cin.nextLong();
			b = cin.nextLong();
			aVal = BigInteger.valueOf(a);
			bVal = BigInteger.valueOf(b);
			gcd = aVal.gcd(bVal);
			lcm = (aVal.multiply(bVal)).divide(gcd);
			System.out.println(gcd + " " + lcm);
		}
	}
}
