import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		String a = in.next();
		String b = in.next();
		String c = in.next();

		int o = 0;
		for (int i = 0; i < n; i++) {
			char aa = a.charAt(i);
			char bb = b.charAt(i);
			char cc = c.charAt(i);
			if (aa == bb && bb == cc && cc == aa) {

			} else if (aa == bb || bb == cc || cc == aa) {
				o++;
			} else {
				o += 2;
			}
		}
		in.close();
		System.out.println(o);
	}

}
