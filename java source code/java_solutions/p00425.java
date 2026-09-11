import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int n;
		int north, south, west, east, top, under, swap;
		String order;
		int sum = 1;
		
		top   = 1;
		south = 2;
		east  = 3;
		west  = 4;
		north = 5;
		under = 6;
		
		n = scanner.nextInt();
		while (n != 0) {
			sum = 1;
			top   = 1;
			south = 2;
			east  = 3;
			west  = 4;
			north = 5;
			under = 6;
			for (int i = 0; i < n; i++) {
				order = scanner.next();
				if (order.equals("North")) {
					swap = north;
					north = top;
					top = south;
					south = under;
					under = swap;
					sum += top;
				} else if (order.equals("South")) {
					swap = north;
					north = under;
					under = south;
					south = top;
					top = swap;
					sum += top;
				} else if (order.equals("West")) {
					swap = top;
					top = east;
					east = under;
					under = west;
					west = swap;
					sum += top;
				} else if (order.equals("East")) {
					swap = top;
					top = west;
					west = under;
					under = east;
					east = swap;
					sum += top;
				} else if (order.equals("Right")) {
					swap = north;
					north = west;
					west = south;
					south = east;
					east = swap;
					sum += top;
				} else if (order.equals("Left")) {
					swap = north;
					north = east;
					east = south;
					south = west;
					west = swap;
					sum += top;
				}
			}
			System.out.println(sum);
			n = scanner.nextInt();
		}
	}
}
