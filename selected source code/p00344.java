import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

/**
 * Cyclic Sugoroku
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;

		int N = parseInt(br.readLine());
		int[] a = new int[N];

		StringTokenizer st = new StringTokenizer(br.readLine());

		for (int i = 0; i < N; i++) {
			a[i] = parseInt(st.nextToken());
		}

		//
		int[] indeg = new int[N];
		boolean[] used = new boolean[N];

		for (int i = 0; i < N; i++) {
			indeg[(i + a[i]) % N]++;
		}


		for (int i = 0; i < N; i++) {
			int j = i;
			while (!used[j] && indeg[j] == 0) {
				used[j] = true;
				j = (j + a[j]) % N;
				indeg[j]--;
			}
		}

		System.out.println(Arrays.stream(indeg).filter(x -> x != 0).count());
	}
}
