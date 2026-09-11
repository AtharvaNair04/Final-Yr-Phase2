import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

	static int M = (int) Math.pow(4, 12);

	static class Hash {
		static int[] T = new int[M];

		public Hash() {
			Arrays.fill(T, -1);
		}

		static int h1(int key) {
			return key % M;
		}

		static int h2(int key) {
			return 1 + key % (M - 1);
		}

		static int h(int key, int i) {
			return (h1(key) + i * h2(key)) % M;
		}

		static void insert(int key) {
			int i = 0;
			while (true) {
				int j = h(key, i);
				if (T[j] == -1) {
					T[j] = key;
					return;
				} else {
					i++;
				}
			}
		}
		
		static String find(int key) {
			int i = 0;
			while (true) {
				int j = h(key, i);
				if (T[j] == key) {
					return "yes";
				}else if (i >= M || T[j] == -1) {
					return "no";
				} else {
					i++;
				} 
			}
		}
		
	}
	
	public static int keyToInt(String key){
		String s = "";
		for (int i = 0; i < key.length(); i++) {
			char c = key.charAt(i);
			if (c == 'A') {
				s += "1";
			}else if (c == 'C') {
				s += "2";
			}else if (c == 'G') {
				s += 3;
			}else if (c == 'T') {
				s += 4;
			}
		}
		return Integer.parseInt(s);
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int N = scan.nextInt();
		String[][] input = new String[N][2];
		Hash hash = new Hash();

//		for (int i = 0; i < N; i++) {
//			input[i][0] = scan.next();
//			input[i][1]= scan.next();
//		}
//		for (int i = 0; i < N; i++) {
//			if (input[i][0].equals("insert")) {
//				hash.insert(keyToInt(input[i][1]));
//			}else if (input[i][0].equals("find")) {
//				System.out.println(hash.find(keyToInt(input[i][1])));
//			}
//		}
		HashSet<String> hashSet = new HashSet<>();
		for (int i = 0; i < N; i++) {
			String command = scan.next();
			String key = scan.next();
			if (command.equals("insert")) {
				hashSet.add(key);
			}else if (command.equals("find")) {
				if (hashSet.contains(key)) {
					System.out.println("yes");
				}else{
					System.out.println("no");
				}
			}
		}
		
		
		scan.close();
	}

	public static void insert(int[] T, int data) {

	}

}
