import java.util.Scanner;

public class Main {

	static int N;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		sc.close();

		dfs("", 'a');
	}

	public static void dfs(String s, char max) {
		if (s.length() == N) {
			System.out.println(s);
		} else {
			for (char c = 'a'; c <= max; c++) {
				dfs(s + c, ((c == max) ? (char) (max + 1) : max));
			}
		}
	}
}
