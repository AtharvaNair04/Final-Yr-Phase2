import java.io.*;
import java.util.Arrays;

public class Main {

	//@2262  Stopping Problem
	public static void main(String[] args) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String s = reader.readLine();
		String[] sp = s.split(" ");
		int ymax = Integer.parseInt(sp[0]);
		int xmax = Integer.parseInt(sp[1]);
		
		String[] map = new String[ymax];
		
		for(int y = 0; y < ymax; y++) {
			map[y] = reader.readLine();
		}
//		map[0] = "6>--v.";
//		map[1] = ".^--_@";
		
		reader.close();
		
		for(int k = 0;k < 100; k++) { // Å©çJèÔµ
			int x = 0;
			int y = 0;
			int xx = 1;
			int yy = 0;
			int v = 0;
		
		for(int step = 0;; step++) {
			if (step > 1000 * 2 * 40) { // 1000*1000*1000=19.98
//				System.out.printf("NO\n"); // â~µÈ¢
				break;
			}
			char c = map[y].charAt(x);
//			System.out.printf("%d %c\n", v, c);
			
			if (c == '@') {
				System.out.printf("YES\n"); // â~
				return;
			}
			else if ("0123456789".indexOf(c) >= 0) {
				v = "0123456789".indexOf(c);
			}
			else {
				if (c == '?') {
					// _
					int r = (int)Math.floor(Math.random() * 4);
					c = "<>^v".charAt(r);
				}
				switch(c) {
				case '<':
					xx = -1;
					yy = 0;
					break;
				case '>':
					xx = 1;
					yy = 0;
					break;
				case '^':
					xx = 0;
					yy = -1;
					break;
				case 'v':
					xx = 0;
					yy = 1;
					break;
				case '_':
					if (v == 0) {
						xx = 1;
						yy = 0;
					}
					else {
						xx = -1;
						yy = 0;
					}
					break;
				case '|':
					if (v == 0) {
						xx = 0;
						yy = 1;
					}
					else {
						xx = 0;
						yy = -1;
					}
					break;
				case '+':
					v = (v + 1) % 16;
					break;
				case '-':
					v = (v + 15) % 16;
					break;
				case '.':
					break;
				default:
					assert false;
				}
			}
			x = (x + xx + xmax) % xmax;
			y = (y + yy + ymax) % ymax;
		}			
		}
		System.out.printf("NO\n"); // â~µÈ¢
	}

}
