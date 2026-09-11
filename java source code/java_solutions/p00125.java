import java.util.Scanner;

public class Main{
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int sYear, sMonth, sDay;
		int eYear, eMonth, eDay;

		while (true) {
			sYear  = sc.nextInt();
			sMonth = sc.nextInt();
			sDay   = sc.nextInt();
			eYear  = sc.nextInt();
			eMonth = sc.nextInt();
			eDay   = sc.nextInt();
			if (sYear == -1) {
				break;
			}
			System.out.println(g(sYear, sMonth, sDay, eYear, eMonth, eDay));
		}
	}

	public static int g(int sy, int sm, int sd, int ey, int em, int ed) {
		int startDay;
		int endDay;

		startDay = (sy-1)*365 + (sy-1)/400*97 + (sy-1)%400/4 - (sy-1)%400/100;
		if (1 < sm) {
			startDay += 31;
		}
		if (2 < sm) {
			if (sy % 4 == 0 && (sy % 100 != 0 || sy % 400 == 0)) {
				startDay += 29;
			} else {
				startDay += 28;
			}
		}
		if (3 < sm) {
			startDay += 31;
		}
		if (4 < sm) {
			startDay += 30;
		}
		if (5 < sm) {
			startDay += 31;
		}
		if (6 < sm) {
			startDay += 30;
		}
		if (7 < sm) {
			startDay += 31;
		}
		if (8 < sm) {
			startDay += 31;
		}
		if (9 < sm) {
			startDay += 30;
		}
		if (10 < sm) {
			startDay += 31;
		}
		if (11 < sm) {
			startDay += 30;
		}
		startDay += sd;

		endDay = (ey-1)*365 + (ey-1)/400*97 + (ey-1)%400/4 - (ey-1)%400/100;
		if (1 < em) {
			endDay += 31;
		}
		if (2 < em) {
			if (ey% 4 == 0 && (ey % 100 != 0 || ey % 400 == 0)) {
				endDay += 29;
			} else {
				endDay += 28;
			}
		}
		if (3 < em) {
			endDay += 31;
		}
		if (4 < em) {
			endDay += 30;
		}
		if (5 < em) {
			endDay += 31;
		}
		if (6 < em) {
			endDay += 30;
		}
		if (7 < em) {
			endDay += 31;
		}
		if (8 < em) {
			endDay += 31;
		}
		if (9 < em) {
			endDay += 30;
		}
		if (10 < em) {
			endDay += 31;
		}
		if (11 < em) {
			endDay += 30;
		}
		endDay += ed;

		return endDay - startDay;
	}
}
