import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		char map[][] = new char[50][50];
		int r = sc.nextInt();
		int c = sc.nextInt();
		sc.nextLine();
		for(int y = 0; y < r; y++) {
			String s = sc.nextLine();
			for(int x = 0; x < c; x++) {
				map[y][x] = s.charAt(x);
			}
		}
		
		int minCount = Integer.MAX_VALUE;
		int count;
		for(int w_line = 0; w_line < r - 2; w_line++) {
			count = 0;
			
			for(int line = 0; line <= w_line; line++) {
				count += difCharLineCount(map[line], 'W', c);
			}
			
			int t_cnt = count;
			
			for(int r_line = w_line + 2; r_line < r; r_line++) {
				for(int line = w_line + 1; line < r_line; line++) {
					count+= difCharLineCount(map[line], 'B', c);
				}
					
				for(int line = r_line; line < r; line++) {
					count+= difCharLineCount(map[line], 'R', c);
				}

				if (count < minCount) {
					minCount = count;
				} 
				count = t_cnt;
			}
			
		}

		System.out.println(minCount);
	}



private static int difCharLineCount(char[] chars, char targetChar, int limit) {
		int ret = 0;
		for(int x = 0; x < limit; x++) {
			if (chars[x] != targetChar) {
				ret++;
			}
		}
		return ret;
	}
}
