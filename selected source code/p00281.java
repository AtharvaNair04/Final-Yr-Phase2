import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * Computation of Salary
 * PCK2013 ??¬???2
 */
public class Main {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String[] words;

		line = br.readLine();

		int N, M;
		N = parseInt(line.substring(0, line.indexOf(' ')));
		M = parseInt(line.substring(line.indexOf(' ') + 1));

		Map<Integer, Map<Integer, Integer>> map = new HashMap<>();

		for (int i = 1; i <= N; i++) map.put(i, new HashMap<>());

		while ((line = br.readLine()) != null && !line.isEmpty()) {
			if (line.equals("0 0 0")) break;
			StringTokenizer st = new StringTokenizer(line);
			int s, t, e;
			s = parseInt(st.nextToken());
			t = parseInt(st.nextToken());
			e = parseInt(st.nextToken());
			map.get(s).put(t, e);
		}

		int L = parseInt(br.readLine());
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < L; i++) {
			int[] b = new int[M + 1];
			StringTokenizer st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= M; j++) {
				b[j] = parseInt(st.nextToken());
			}

			List<String> list = new ArrayList<>();

			for (int j = 1; j <= N; j++) {
				int c = 0;
				for (int t : map.get(j).keySet()) {
					c += b[t] * map.get(j).get(t);
				}
				list.add(String.valueOf(c));
			}

			sb.append(String.join(" ", list)).append('\n');
		}

		System.out.print(sb.toString());
	}
}
