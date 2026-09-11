import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int n = Integer.parseInt(br.readLine());
		int result = 0;
		int max = 0, min = 1000000;
		int arc = 0;
		int abc = 0;

		String str[] = br.readLine().split(" ");
		int list[] = new int[n];

		for (int i = 0; i < n; i++) {
			list[i] = Integer.parseInt(str[i]);
			max = Math.max(list[i], max);
			min = Math.min(list[i], min);
		}

		Arrays.sort(list);
		int ave = list[list.length / 2 - 1];

		for (int i = ave; i < list[list.length / 2]; i++) {
			/*
			 * for (int j = 0; j < n; j++) { if (i >= list[j]) { arc++; } else
			 * if (i < list[j]) { abc++; } } if (abc == arc) result++; abc = 0;
			 * arc = 0;
			 */
			result++;
		}

		System.out.println(result);
	}
}
