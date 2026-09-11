import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class Main {
	public static void main (String[] args) throws java.lang.Exception{
		int n=0;
		long sum=1;
		ArrayList<Integer> h = new ArrayList<Integer>();

		Scanner scan = new Scanner(System.in);
		n=scan.nextInt();

		for(int i=0; i<n; i++) h.add(scan.nextInt());

		Collections.sort(h);
		Collections.reverse(h);

		for(int i=0; i<n; i++) sum += (long)Math.max((h.get(i) - i/4), 0);

		System.out.println(sum);
	}
}
