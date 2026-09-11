import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		int n = scnr.nextInt();
		long result;
		if (n % 2 == 0) {
			result = n;
		} else {
			result = n * 2;
		}
		System.out.println(result);
	}
}
