import java.io.*;
import java.util.*;

class Main {

	public static void main(String[] args) throws Exception {
		final Scanner sc = new Scanner(System.in);
		long N = sc.nextLong();
		char[] S = sc.next().toCharArray();
		final char OPEN = '(';
		final char CLOSE = ')';

		StringBuilder a = new StringBuilder();
		int o = 0;
		int c = 0;
		for (int i = 0; i < N; i++) {
			if (S[i] == OPEN) {
				o++;
			} else {
				o--;
				if (o < 0) {
					a.append(OPEN);
					o++;
				}
			}
		}
		a.append(S);

		while (o-- > 0) {
			a.append(CLOSE);
		}

		System.out.println(a.toString());
	}
}
