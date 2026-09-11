import java.util.*;
import java.io.*;

class Main {
	public static void main(String... args) {
		int M = 1_000_000_007;
		String S = IN.next();
		
		long a   = 0;
		long ab  = 0;
		long abc = 0;
		long t   = 1;

		for (int i = 0; i < S.length(); i++) {
			switch (S.charAt(i)) {
				case 'A':
					a = (a + t) % M;
					break;
				case 'B':
					ab = (ab + a) % M;
					break;
				case 'C':
					abc = (abc + ab) % M;
					break;
				default:
					abc = (abc * 3 + ab) % M;
					ab  = (ab * 3 + a) % M;
					a   = (a * 3 + t) % M;
					t   = (t * 3) % M;
			}
		}
		puts(abc);
		flush();
	}
	static final Scanner IN = new Scanner(System.in);
	static final PrintWriter OUT = new PrintWriter(System.out);
	static <T> void puts(T arg) { OUT.println(arg); }
	static void flush() { OUT.flush(); }
}
