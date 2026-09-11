import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
public class Main
{
	public static void main(String args[])throws IOException {
		BufferedReader br  =new BufferedReader(new InputStreamReader(System.in));
		int n =Integer.parseInt(br.readLine());
		long a[]=new long[0];
		a=Arrays.stream(br.readLine().split("\\s+")).mapToLong(Long::parseLong).toArray();
	    PriorityQueue<Long> npq = new PriorityQueue<>(Comparator.naturalOrder());
	    PriorityQueue<Long> rpq = new PriorityQueue<>(Comparator.reverseOrder());
	    long[] s = new long[n+1];
	    long[] t = new long[n+1];
	    long su = 0L;
	    long tu = 0L;
	    for (int i=0;i<n;i++) {
	      npq.offer(a[i]);
	      su += a[i];
	      rpq.offer(a[3*n-1-i]);
	      tu += a[3*n-1-i];
	    }
	    s[0] = su;
	    t[n] = tu;
	    for (int i=n;i<2*n;i++) {
	      npq.offer(a[i]);
	      su += a[i]-npq.poll();
	      s[i-n+1] = su;
	      rpq.offer(a[3*n-1-i]);
	      tu += a[3*n-1-i]-rpq.poll();
	      t[2*n-i-1] = tu;
	    }
	    long m = s[0]-t[0];
	    for (int i=0;i<n+1;i++) {
	      if (m < s[i]-t[i]) m = s[i]-t[i];
	    }
	    System.out.println(m);
	
	}
}
