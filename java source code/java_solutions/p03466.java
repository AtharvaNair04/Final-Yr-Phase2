import java.util.*;
 
public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int Tests = sc.nextInt();
		for (int test = 0; test < Tests; test ++) {
			long a = sc.nextInt();
			long b = sc.nextInt();
			long l = sc.nextInt();
			long r = sc.nextInt();
			long len = (Math.max(a, b) - 1) / (Math.min(a, b) + 1) + 1;
			long lena, lenb;
			if (a >= len * b) {
				lena = a + b; lenb = 0;
			} else if (b >= len * a) {
				lena = 0; lenb = a + b;
			} else {
				lena = (len * a - b) / (len - 1);
				lenb = (len * b - a) / (len - 1);
			}
			boolean B = a - lena + lena / (len + 1) - lenb / (len + 1) == 0;
			for (long i = l - 1; i < r; ++ i) {
				char pc;
				if (i < lena) {
					if (i % (len + 1) == len) pc = 'B';
					else pc = 'A';
				}
				else if (i >= a + b - lenb) {
					if ((a + b - i - 1) % (len + 1) != len) pc = 'B';
					else pc = 'A';
				}
				else if (B) pc = 'B';
				else pc = 'A';
				System.out.print(pc);
			}
			System.out.println();
		}
	}
}
