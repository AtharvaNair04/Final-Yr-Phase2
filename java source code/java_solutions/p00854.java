import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			int n = sc.nextInt();
			int k = sc.nextInt();
			int m = sc.nextInt();
			if ((n | k | m) == 0)
				break;
			Stone head = new Stone(1);
			Stone tail = head;
			for (int i = 2; i <= n; i++) {
				Stone s = new Stone(i);
				tail.next = s;
				tail = tail.next;
			}
			tail.next = head;
			for (int i = 0; i < m - 1; i++) {
				tail = tail.next;
			}
			deleteNext(tail);
			while (!tail.next.equals(tail)) {
				for (int i = 0; i < k - 1; i++) {
					tail = tail.next;
				}
				deleteNext(tail);
			}
			System.out.println(tail.id);
		}
	}

	static void deleteNext(Stone s) {
		Stone t = s.next;
		s.next = t.next;
	}

}

class Stone {
	int id;
	Stone next;

	Stone(int i) {
		id = i;
	}

	@Override
	public boolean equals(Object s) {
		if (!(s instanceof Stone))
			return false;
		return ((Stone) s).id == id;
	}
}
