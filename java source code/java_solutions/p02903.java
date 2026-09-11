import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int h = sc.nextInt();
		int w = sc.nextInt();
		int a = sc.nextInt();
		int b = sc.nextInt();

		StringBuilder sb = new StringBuilder();


		for (int i = 0; i < b; i++) {
			for (int j = 0; j < a; j++) {
				sb.append(1);
			}

			for (int j = 0; j < w - a; j++) {
				sb.append(0);
			}

			sb.append("\n");
		}

		for (int i = 0; i < h - b; i++) {
			for (int j = 0; j < a; j++) {
				sb.append(0);
			}

			for (int j = 0; j < w - a; j++) {
				sb.append(1);
			}
			sb.append("\n");
		}

		System.out.println(sb.toString());
	}
}
