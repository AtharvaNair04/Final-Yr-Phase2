import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Main {
	public static void main(String[] args) {
		List<Integer> A = new ArrayList<Integer>();
		Scanner scan = new Scanner(System.in);
		int q = scan.nextInt();
		int i = 0;
		while (i < q) {
			i++;

			int Cmd = scan.nextInt();
			switch (Cmd) {
			case 0:
				int x = scan.nextInt();
				pushBack(A, x);
				break;
			case 1:
				int p = scan.nextInt();
				randomAccess(A, p);
				break;
			case 2:
				popBack(A);
				break;
			default:
				break;
			}
		}
	}
	private static void pushBack(List<Integer> A, int x) {
		A.add(x);
	}
	private static void randomAccess(List<Integer> A, int p) {
		System.out.println(A.get(p));
	}
	private static void popBack(List<Integer> A) {
		A.remove(A.size() - 1);
	}
}
