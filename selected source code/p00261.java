import java.util.Arrays;
import java.util.Scanner;

public class Main {
	Scanner sc = new Scanner(System.in);

	void run() {

		Node a = new Node("A");
		Node x = new Node("X");
		Node y = new Node("Y");
		Node z = new Node("Z");
		Node w = new Node("W");
		Node b = new Node("B");
		Node desert = new Node("D");
		a.zero = x;
		a.one = y;
		x.zero = desert;
		x.one = z;
		z.one = b;
		z.zero = w;
		w.zero = b;
		w.one = y;
		y.zero = x;
		y.one = desert;
		b.one = x;
		b.zero = y;
		for (;;) {

			String r = sc.next();

			if (r.equals("#"))
				break;
			Node now = a;
			for (int i = 0; i < r.length(); i++) {
				if (r.charAt(i) == '0') {
					now = now.zero;
				} else {
					now = now.one;
				}
				if (now.city.equals("D")) {
					break;
				}
			}
			if (now.city.equals("B")) {
				System.out.println("Yes");
				continue;
			}
			System.out.println("No");
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}

class Node {
	public String city = "";
	public Node zero;
	public Node one;

	public Node(String c) {
		city = c;
	}
}
