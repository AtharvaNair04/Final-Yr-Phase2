import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		age_difference();
	}

	private static void age_difference() {
		Scanner sc = new Scanner(System.in);
		int[] d = new int[6];

			for (int i = 0; i < 6; i++) {
				d[i] = sc.nextInt();
			}


		LocalDate d1 = LocalDate.of(d[0], d[1], d[2]);
		LocalDate d2 = LocalDate.of(d[3], d[4], d[5]);

		int age_diff = Math.abs(Period.between(d1, d2).getYears());

		if (d[1] == d[4] && d[2] == d[5]) {
			System.out.println(age_diff);
		} else {
			System.out.println(age_diff + 1);
		}
		sc.close();

	}

}
