import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int sumProfit;
		int sumNumber;
		String str[];
		int count;

		double b = 2.4;
		double c = 2.5;
		double d = 2.6;

		sumProfit = 0;
		sumNumber = 0;
		count = 0;
		while (sc.hasNext()) {
			str = sc.next().split(",");
			sumProfit += Integer.valueOf(str[0]) * Integer.valueOf(str[1]);
			sumNumber += Integer.valueOf(str[1]);
			count++;
		}
		System.out.println(sumProfit);
		System.out.printf("%.0f\n", sumNumber * 1.0 / count);
	}
}
