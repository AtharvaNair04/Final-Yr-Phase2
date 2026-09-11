import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        double[] p1 = {0, 0};
        double[] p2 = {100, 0};
        print(p1);
        koch(n, p1, p2);
        print(p2);

	}

	static void koch(int n, double[] p1, double[] p2) {
		if(n == 0)
			return;

		double[] s = { (2*p1[0]+1*p2[0])/3,  (2*p1[1]+1*p2[1])/3 };
		double[] t = { (1*p1[0]+2*p2[0])/3,  (1*p1[1]+2*p2[1])/3 };
		double[] u = { (t[0]-s[0]) * Math.cos(Math.toRadians(60)) - (t[1]-s[1]) * Math.sin(Math.toRadians(60)) + s[0]
						,(t[0]-s[0]) * Math.sin(Math.toRadians(60)) + (t[1]-s[1]) * Math.cos(Math.toRadians(60)) + s[1] };

		koch(n-1, p1, s);
		print(s);
		koch(n-1, s, u);
		print(u);
		koch(n-1, u, t);
		print(t);
		koch(n-1, t, p2);
	}

	private static void print(double[] s) {
		System.out.println(s[0] + " " + s[1]);
	}
}
