import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Bange Hills Tower
 */
public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		int N, t;
		N = sc.nextInt();
		t = sc.nextInt();

		BigDecimal tan = new BigDecimal(0);

		for (int i = 0; i < N; i++) {
			BigDecimal x, h, _tan;
			x = new BigDecimal(sc.next());
			h = new BigDecimal(sc.next());
			_tan = h.divide(x, 10, BigDecimal.ROUND_HALF_UP);
			if (tan.compareTo(_tan) < 0) {
				tan = _tan;
			}
		}

		System.out.println(tan.multiply(new BigDecimal(t)).toString());
	}
}
