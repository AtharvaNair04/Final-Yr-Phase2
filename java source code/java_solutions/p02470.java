import java.util.*;
import java.io.*;

class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int n = Integer.parseInt(br.readLine());
		double phi = n;
		Set<Integer> set = new HashSet<Integer>();

		for (int i=2; i*i<=n; i++) {
			while (n%i == 0) {
				n /= i;
				set.add(i);
			}
		}
		if (n > 1)
			set.add(n);

		Iterator it = set.iterator();
		while (it.hasNext()) {
			int p = (Integer)it.next();
			phi *= 1-(double)1/p;
		}

		System.out.println((int)phi);

		br.close();
	}
}
