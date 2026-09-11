import java.util.Scanner;

public class Main {
	static class Pos {
		double x, y;

		Pos(double x, double y) {
			this.x = x;
			this.y = y;
		}

		static Pos vector(Pos a, Pos b) {
			return new Pos(b.x - a.x, b.y - a.y);
		}

		static double cross(Pos a, Pos b) {
			return a.x * b.y - a.y * b.x;
		}
	}

	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n;
		Pos p[] = new Pos[3];
		Pos x;
		Pos y;

		n = sc.nextInt();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 3; j++) {
				p[j] = new Pos(sc.nextDouble(), sc.nextDouble());
			}
			x = new Pos(sc.nextDouble(), sc.nextDouble());
			y = new Pos(sc.nextDouble(), sc.nextDouble());

			boolean in1 = true;
			boolean in2 = true;
			for (int j = 0; j < 3; j++) {
				Pos v1 = Pos.vector(p[j], p[(j + 1) % 3]);
				Pos v2 = Pos.vector(p[j], p[(j + 2) % 3]);
				Pos v3 = Pos.vector(p[j], x);
				double cross1 = Pos.cross(v1, v2);
				double cross2 = Pos.cross(v1, v3);
				if (cross1 * cross2 <= 0.0) {
					in1 = false;
				}
			}
			for (int j = 0; j < 3; j++) {
				Pos v1 = Pos.vector(p[j], p[(j + 1) % 3]);
				Pos v2 = Pos.vector(p[j], p[(j + 2) % 3]);
				Pos v3 = Pos.vector(p[j], y);
				double cross1 = Pos.cross(v1, v2);
				double cross2 = Pos.cross(v1, v3);
				if (cross1 * cross2 <= 0) {
					in2 = false;
				}
			}

			if (in1 != in2) {
				System.out.println("OK");
			} else {
				System.out.println("NG");
			}
		}
	}
}
