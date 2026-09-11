import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

/**
 * Walking in JOI Kingdom
 */
public class Main {

	static final int EAST = 1;
	static final int WEST = -1;
	static final long NONSTOP = (long) Math.pow(10, 18) + 1;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		String[] words;

		words = br.readLine().split(" ");

		int N, Q;
		long T;
		N = parseInt(words[0]);
		T = parseLong(words[1]);
		Q = parseInt(words[2]);

		long[] pos = new long[N];
		int[] dir = new int[N];
		long[] stop = new long[N];

		for (int i = 0; i < N; i++) {
			words = br.readLine().split(" ");
			pos[i] = parseLong(words[0]);
			dir[i] = parseInt(words[1]) == EAST ? EAST : WEST;
			stop[i] = NONSTOP;
		}

		//solve
		for (int i = 1; i < N - 1; i++) {
			if (dir[i] == EAST) {
				if (dir[i + 1] == WEST) {
					stop[i] = stop[i + 1] = (pos[i + 1] - pos[i]) / 2;
				}
			} else {
				if (dir[i - 1] == EAST) {
					stop[i - 1] = stop[i] = (pos[i] - pos[i - 1]) / 2;
				}
			}
		}

		for (int i = 0; i < N; i++) {
			if (stop[i] == NONSTOP) {
				if (dir[i] == EAST) {
					for (int j = i + 1; j < N; j++) {
						if (stop[j] != NONSTOP) {
							stop[i] = pos[j] - pos[i] + stop[j];
							break;
						}
					}
				} else {
					for (int j = i - 1; j >= 0; j--) {
						if (stop[j] != NONSTOP) {
							stop[i] = pos[i] - pos[j] + stop[j];
							break;
						}
					}
				}
			}
		}

		for (int i = 0; i < Q; i++) {
			int q = parseInt(br.readLine()) - 1;
			if (T < stop[q]) {
				System.out.println(pos[q] + dir[q] * T);
			} else {
				System.out.println(pos[q] + dir[q] * stop[q]);
			}
		}
	}
}
