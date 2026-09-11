import static java.lang.Math.*;
import static java.util.Arrays.*;
import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		try {
			String packageName = new Main().getClass().getPackage().getName();
			System.setIn(new FileInputStream("src/" + packageName + "/input.txt"));
			// System.setOut(new PrintStream(new FileOutputStream("src/" + packageName + "/result.txt")));
		} catch (FileNotFoundException e) {
		} catch (NullPointerException e) {
		}
		new Main().run();
	}

	void tr(Object... os) {
		System.err.println(deepToString(os));
	}

	HashMap<String, Integer> c2i;
	Scanner sc;
	void run() {

		c2i = new HashMap<String, Integer>();
		String[] ss = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
		for (int i = 0; i < ss.length; i++)
			c2i.put(ss[i], i);

		sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T -- > 0) solve();
	}
	void solve() {
		int n = sc.nextInt();
		int m = sc.nextInt();
		int[] T = new int[n];
		int[] S = new int[m];
		for (int i = 0; i < n; i++) T[i] = c2i.get(sc.next());
		for (int i = 0; i < m; i++) S[i] = c2i.get(sc.next());

		boolean ok = false;
		{
			int t = n - 1, s = m - 1;
			while (true) {
				if (t < 0 || t >= n || s < 0) {
					break;
				} else if (T[t] == S[s]) {
					t--;
					s--;
				} else if (T[t] - S[s] == 1 || T[t] - S[s] == -11) {
					t++;
					s--;
				} else if (T[t] - S[s] == -1 || T[t] - S[s] == 11) {
					t -= 2;
					s--;
				} else {
					break;
				}
			}
			if (t == -1 && s == -1) ok = true;
		}
		{
			int t = n - 2, s = m - 1;
			while (true) {
				if (t < 0 || t >= n || s < 0) {
					break;
				} else if (T[t] == S[s]) {
					t--;
					s--;
				} else if (T[t] - S[s] == 1 || T[t] - S[s] == -11) {
					t++;
					s--;
				} else if (T[t] - S[s] == -1 || T[t] - S[s] == 11) {
					t -= 2;
					s--;
				} else {
					break;
				}
			}
			if (t == -1 && s == -1) ok = true;
		}

		System.out.println(ok ? "Yes":"No");
	}
}
