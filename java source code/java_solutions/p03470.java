import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = Integer.parseInt(scanner.nextLine());
		Set<Integer> s = new HashSet<>();
		for (int i = 0; i < n; i++) {
			s.add(Integer.parseInt(scanner.nextLine()));
		}
		System.out.println(s.size());
	}
}
