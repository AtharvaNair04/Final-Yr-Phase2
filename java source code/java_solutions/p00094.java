import java.util.Scanner;

public class Main{

	public static void main(String[] args) {
		Scanner sc  =new Scanner(System.in);
		int a = sc.nextInt();
		int b = sc.nextInt();
		double ans = (a * b) /3.305785;

//		BigDecimal x = new BigDecimal(ans);
//		BigDecimal y = x.setScale(6, BigDecimal.ROUND_DOWN);

		System.out.println(ans);

	}

}
