import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt();
		int b = sc.nextInt();
		char WHITE = '.';
		char BLACK = '#';

		char[][] map = new char[100][100];

		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				if(i < 50) {
					map[i][j] = WHITE;
				} else {
					map[i][j] = BLACK;
				}
			}
		}

		int count = 0;
		for (int i = 0; i < a - 1; i++) {
			int column = 99 - ((i / 50) * 2);
			int row = i % 50 * 2;
			map[column][row] = WHITE;
		}

		for (int i = 0; i < b - 1; i++) {
			int column = (i / 50) * 2;
			int row = i % 50 * 2;
			map[column][row] = BLACK;
		}


		System.out.println("100 100");
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println("");
		}


	}

}
