import java.util.*;

public class Main {
	public static void main(String []args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int t = sc.nextInt();
		int[] frags = new int[n];
		int i = 0;
		while(sc.hasNext()) {
			frags[i++] = sc.nextInt();
		}
		Main soln = new Main();
		System.out.println(soln.jdg(t, frags));
	}

	private long jdg(int t, int[] frags) {
		long res = (long)t;
		if(frags.length==1) return res;
		int dlt = 0;
		for(int i=0; i<frags.length-1; i++) {
			dlt = frags[i+1]-frags[i];
			res += (long)((dlt<t)? dlt:t);
		}
		return res;
	}
}
