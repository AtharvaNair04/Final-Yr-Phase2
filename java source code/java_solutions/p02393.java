import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String x = sc.nextLine();
		String[] b = x.split(" ");
		int[] e = new int[3];
		e[0] = Integer.parseInt(b[0]);
		e[1] = Integer.parseInt(b[1]);
		e[2] = Integer.parseInt(b[2]);
		Arrays.sort(e);
		System.out.println(e[0] + " " + e[1] + " " + e[2]);

	}
}
