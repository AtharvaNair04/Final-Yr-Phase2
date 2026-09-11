import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

	static ArrayList<Integer> ans = new ArrayList<>();
	static int l;

	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str[] = br.readLine().split("");

		if (str.length == 2) {
			if (str[0].equals(str[1])) {
				System.out.println((1) + " " + (2));
				return;
			}
		} else {
			for (int i = 0; i < str.length - 3; i++) {
				if (str[i].equals(str[i + 1]) || str[i].equals(str[i + 2]) || str[i + 1].equals(str[i + 2])) {
					System.out.println((i + 1) + " " + (i + 3));
					return;
				}
			}
		}
		System.out.println("-1 -1");

		/*
		 * String str[] = br.readLine().split(" "); int n = Integer.parseInt(str[0]); l
		 * = Integer.parseInt(str[1]);
		 * 
		 * int a[] = new int[n]; str = br.readLine().split(" ");
		 * 
		 * for (int i = 0; i < n; i++) { a[i] = Integer.parseInt(str[i]); }
		 * 
		 * int min = 0; int minN = 0;
		 * 
		 * for (int i = 0; i < n - 1; i++) { int sum = a[i] + a[i + 1]; if (sum >= l) {
		 * System.out.println("Possible"); for (int j = 0; j < i; j++) {
		 * System.out.println(j + 1); } for (int j = n - 2; j > i; j--) {
		 * System.out.println(j + 1); } System.out.println(i + 1); System.exit(0); } }
		 * 
		 * System.out.println("Impossible");
		 */
	}
	/*
	 * static boolean isPossible(int a[], int add) { int min = Integer.MAX_VALUE;
	 * int minN = 0; int sum = 0;
	 * 
	 * if (a.length == 1) return true;
	 * 
	 * for (int i = 0; i < a.length; i++) { if (min >= a[i]) { min = a[i]; minN = i;
	 * } sum += a[i]; }
	 * 
	 * if (sum < l) return false;
	 * 
	 * int lSum = 0; int rSum = 0;
	 * 
	 * int lSize = 0; int rSize = 0;
	 * 
	 * if (minN == 0) minN = 1;
	 * 
	 * for (int i = 0; i < minN; i++) { lSum += a[i]; lSize++; }
	 * 
	 * for (int i = minN; i < a.length; i++) { rSum += a[i]; rSize++; }
	 * 
	 * // System.out.println(lSize + " " + rSize);
	 * 
	 * if ((rSum < l && rSize > 1) || (lSum < l && lSize > 1)) return false;
	 * 
	 * int lA[]; int rA[];
	 * 
	 * lA = new int[lSize]; rA = new int[rSize]; System.arraycopy(a, 0, lA, 0,
	 * lSize); System.arraycopy(a, minN, rA, 0, rSize);
	 * 
	 * ans.add(minN + add);
	 * 
	 * // System.out.println(minN + " " + lSum + " " + rSum);
	 * 
	 * if (isPossible(lA, 0) && isPossible(rA, minN + add)) return true; return
	 * false; }
	 */
}
