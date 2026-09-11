import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		// 入力
		Scanner sc = new Scanner(System.in);
		double xp1 = sc.nextDouble();
		double yp1 = sc.nextDouble();
		Vector2d p1 = new Vector2d(xp1, yp1);
		double xp2 = sc.nextDouble();
		double yp2 = sc.nextDouble();
		Vector2d p2 = new Vector2d(xp2, yp2);
		int q = sc.nextInt();
		ArrayList<Vector2d> p = new ArrayList<>();
		for (int i=0; i<q; i++) {
			double x = sc.nextDouble();
			double y = sc.nextDouble();
			p.add(new Vector2d(x, y));
		}
		sc.close();

		for (int i=0; i<q; i++) {
			System.out.println(solve(p.get(i), p1, p2));
		}

	}

	static class Vector2d {

		Vector2d(Double x, Double y){
			this.x = x;
			this.y = y;
		}

		Double x;
		Double y;

		public String toString() {
			return String.format("%1$.10f %2$.10f", x, y);
		}
	}

	public static Vector2d add(Vector2d v1, Vector2d v2){
		return new Vector2d(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2d sub(Vector2d v1, Vector2d v2){
		return new Vector2d(v2.x - v1.x, v2.y - v1.y);
	}

	public static Double sqNorm(Vector2d v){
		return v.x * v.x + v.y * v.y;
	}

	public static Double product(Vector2d v1, Vector2d v2){
		return v1.x * v2.x + v1.y * v2.y;
	}

	public static Vector2d extend(double s, Vector2d v){
		return new Vector2d(s * v.x, s * v.y);
	}

	public static Vector2d solve(Vector2d p, Vector2d p1, Vector2d p2) {
		Vector2d a = sub(p1, p2);
		Vector2d b = sub(p1, p);
		return add(p1, extend(product(a, b) / sqNorm(a), a));
	}
}
