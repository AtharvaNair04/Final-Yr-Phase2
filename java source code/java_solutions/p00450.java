import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		int x;
		int a[];
		int mostRightColor;
		int index;
		int count;

		n = sc.nextInt();
		while (n != 0) {
			a = new int[n];
			count = 0;
			index = 0;
			mostRightColor = -1;
			for (int i = 0; i < n; i++) {
				x = sc.nextInt();
				if ((i + 1) % 2 == 0) {
					if (mostRightColor == x) {
						a[index - 1]++;
					} else {
						if (index == 1) {
							a[index - 1]++;
						} else {
							a[index - 2] += a[index - 1] + 1;
							index--;
						}
						mostRightColor = x;
					}
				} else {
					if (mostRightColor == x) {
						a[index - 1]++;
					} else {
						a[index++] = 1;
						mostRightColor = x;
					}
				}
			}
			int i = (mostRightColor + (index % 2) + 1) % 2;
			for (; i < index; i += 2) {
				count += a[i];
			}
			System.out.println(count);
			n = sc.nextInt();
		}
	}
}
