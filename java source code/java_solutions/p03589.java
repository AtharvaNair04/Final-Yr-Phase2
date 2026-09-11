import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	Main m = new Main();
	m.answer();
    }

    private Scanner scan = new Scanner(System.in);
    private static final int MAX = 3500;
    private int N;

    public Main() {
	N = Integer.parseInt(scan.next());
	scan.close();
    }

    public final void answer() {
	for (int h = Integer.max(1, N/4); h <= MAX; h++) {
	    if(4*h-N > 0) {
		for (int n = Integer.max(1, N*h/(4*h-N)); n <= MAX; n++) {
		    long a = (long)N*h*n;
		    long b = (long)4*h*n-(long)N*(n+h);
		    if(b > 0 && a > b && a%b == 0) {
			long w = a/b;
			System.out.println(String.valueOf(h) + ' ' + n + ' ' + w);
			return;
		    }
		}
	    }
	}
    }
}
