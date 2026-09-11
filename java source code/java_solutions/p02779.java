import java.util.*;

class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		Set<Integer> A = new HashSet<>();
		String ans = "NO";
		
		for (int i=0; i<N; i++) {
			A.add(sc.nextInt());
		}
		if (N == A.size()) {
			ans = "YES";
		}
		System.out.println(ans);
	}
}
