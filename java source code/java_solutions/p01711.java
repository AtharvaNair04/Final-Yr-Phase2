import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);
	static int[] filter = new int[1 << 7];

	public static void main(String[] args) {
		while (sc.hasNext()) {
			char[] input = sc.next().toCharArray();
			if (input.length != filter.length) break;
			for (int j = 0; j < filter.length; ++j) {
				filter[j] = input[j] - '0';
			}
			System.out.println(solve() ? "yes" : "no");
		}
	}

	static boolean solve() {
		int[] outer = new int[5];
		for (int j = 0; j < (1 << 19); ++j) {
			outer[0] = (j & 0x7) << 2;
			outer[1] = ((j >> 3) & 0xF) << 1;
			outer[2] = (j >> 7) & 0x1F;
			outer[3] = (j >> 12) & 0xF;
			outer[4] = (j >> 16) & 0x7;
			int inner = 0;
			inner |= calc(outer, 2, 1);
			inner |= calc(outer, 3, 1) << 1;
			inner |= calc(outer, 1, 2) << 2;
			inner |= calc(outer, 2, 2) << 3;
			inner |= calc(outer, 3, 2) << 4;
			inner |= calc(outer, 1, 3) << 5;
			inner |= calc(outer, 2, 3) << 6;
			if (filter[inner] != ((inner >> 3) & 1)) {
				return false;
			}
		}
		return true;
	}

	static int calc(int[] ar, int x, int y) {
		int key = (ar[y - 1] >> x) & 0x3;
		key |= ((ar[y] >> (x - 1)) & 0x7) << 2;
		key |= ((ar[y + 1] >> (x - 1)) & 0x3) << 5;
		return filter[key];
	}
}
