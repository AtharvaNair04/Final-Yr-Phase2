import java.util.Scanner;

public class Main {

	Scanner sc = new Scanner(System.in);

	void run() {
		String a = sc.next();
		int cnt = 0;
		int A, B;
		int x, y;
		String res = "";
		for (int i = 0; i < a.length(); i++) {
			if (i == 0) {
				A = 0;
				B = Integer.valueOf(a);
				if ((A + B) % 2 == 0 && (A - B) % 2 == 0) {
					x = (A + B) / 2;
					y = -(A - B) / 2;
					if(x < 0 || y < 0) {
						continue;
					}
					B = x + y;
					res = B + "";
					if (res.equals(a)) {
						cnt++;
					}
				}
			} else {
				A = Integer.valueOf(a.substring(0, i));
				B = Integer.valueOf(a.substring(i, a.length()));
				if ((A + B) % 2 == 0 && (A - B) % 2 == 0) {
					x = (A + B) / 2;
					y = -(A - B) / 2;
					if(x < 0 || y < 0) {
						continue;
					}
					A = x - y;
					B = x + y;
					res = A + "" + B;
					if (res.equals(a)) {
						cnt++;
					}
				}
			}
		}
		System.out.println(cnt);
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
