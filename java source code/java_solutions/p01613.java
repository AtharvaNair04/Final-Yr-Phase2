import java.util.*;
import static java.lang.Math.*;

public class Main {

	int INF = 1 << 28;

	void run() {
		Scanner sc = new Scanner(System.in);
		int[] a = new int[5];
		int m=INF;
		for(int i=0;i<5;i++) a[i] = sc.nextInt()-1;
		for(int i=1;i<=a[0]+1;i++) m = min(m, d(a[1], a[2], i) + d(a[3], a[4], i));
		System.out.println(m);
	}
	
	int d(int n1, int n2, int w) {
		return abs(n1/w - n2/w) + abs(n1%w-n2%w);
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
