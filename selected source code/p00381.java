import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		int[] s = new int[n];
		int[] t = new int[n];
		
		String ss = sc.next();
		for(int i = 0; i < n; i++) {
			s[i] = ss.charAt(i) - 'a';
		}
		
		String st = sc.next();
		for(int i = 0; i < n; i++) {
			t[i] = st.charAt(i) - 'a';
		}
		
		int[] table = new int[26];
		for(int i = 0;i < 26; i++) {
			table[i] = 0;
		}
		table[s[0]] = 1;
		
		for(int i = 1; i < n - 1; i++) {
			table[s[i]] += table[t[i]];
			table[s[i]] %= 1000000007;
		}
		
		System.out.println(table[t[n-1]]);
	}
}
