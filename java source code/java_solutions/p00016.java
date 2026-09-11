import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in).useDelimiter("[\n ,]");
		int a, b, angle = 0;
		double ax = 0.0, by = 0.0;
		while (true) {
			a = sc.nextInt();
			b = sc.nextInt();

			if (a == 0 & b == 0)
				break;
			ax += Math.sin((Math.PI * angle) / 180) * a;
			by += Math.cos((Math.PI * angle) / 180) * a;
			angle += b;
		}
		System.out.printf("%d\n%d\n", (int) ax, (int) by);
		sc.close();
	}
}
