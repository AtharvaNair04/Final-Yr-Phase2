import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n, m;
		int a[];

		while (true) {
			n = sc.nextInt();
			m = sc.nextInt();
			if ((n | m) == 0) {
				break;
			}
			a = new int[n];
			while (m-- != 0) {
				String k = sc.next();
				if (k.equals("A")) {
					for (int i = 0; i < n; i++) {
						if (a[i] == 0) {
							a[i] = 1;
							break;
						}
					}
				} else if (k.equals("B")) {
					boolean flag = false;
					for (int i = n - 1; 0 <= i; i--) {
						if (a[i] == 0 && (i - 1 < 0 || a[i - 1] != 1) && (n <= i + 1 || a[i + 1] != 1)) {
							a[i] = 2;
							flag = true;
							break;
						}
					}
					if (!flag) {
						for (int i = 0; i < n; i++) {
							if (a[i] == 0) {
								a[i] = 2;
								break;
							}
						}
					}
				} else if (k.equals("C")) {
					boolean flag = false;
					for (int i = 0; i < n; i++) {
						if (a[i] != 0) {
							if (i + 1 < n && a[i + 1] == 0) {
								a[i + 1] = 3;
								flag = true;
								break;
							} else if (0 <= i - 1 && a[i - 1] == 0) {
								a[i - 1] = 3;
								flag = true;
								break;
							}
						}
					}
					if (!flag) {
						a[n / 2] = 3;
					}
				} else if (k.equals("D")) {
					int max = 0;
					int minId = -1;
					for (int i = 0; i < n; i++) {
						if (a[i] == 0) {
							int tmp1 = 0;
							int tmp2 = 0;
							int id = i + 1;
							while (id < n && a[id] == 0) {
								tmp1++;
								id++;
							}
							if (id == n) {
								tmp1 = 1 << 29;
							}
							id = i - 1;
							while (0 <= id && a[id] == 0) {
								tmp2++;
								id--;
							}
							if (id == -1) {
								tmp2 = 1 << 29;
							}
							if (max < Math.min(tmp1, tmp2)) {
								max = Math.min(tmp1, tmp2);
								minId = i;
							}
						}
					}
					if (minId != -1) {
						a[minId] = 4;
					} else {
						for (int i = 0; i < n; i++) {
							if (a[i] == 0) {
								a[i] = 4;
								break;
							}
						}
					}
				}
			}
			for (int i = 0; i < n; i++) {
				if (a[i] != 0) {
					System.out.print((char)(a[i] + 'A' - 1));
				} else {
					System.out.print("#");
				}
			}
			System.out.println();
		}
	}
}
