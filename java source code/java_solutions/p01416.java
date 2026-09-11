import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		new Main().run();
	}
	
	Scanner scanner = new Scanner(System.in);
	int m;
	int[][][] tile = new int[26][2][2];
	boolean[][] cantGet =new boolean[26][2];
	int score = 0;
	
	void run() {
		
		m = scanner.nextInt();
		scanner.nextInt();
		
		for (int color = 0; color < 26; color ++) {
			
			tile[color][0][0] = -1;
			cantGet[color][0] = false;
			cantGet[color][1] = false;
		}
		
		for (int y = 0; y < m; y ++) {
			
			char[] line = scanner.next().toCharArray();
			
			for (int x = 0; x < line.length; x ++) {
				
				if(line[x] != '.') {
					
					int color = line[x] - 'A';
					int index = (tile[color][0][0] == -1) ? 0: 1;
					tile[color][index][0] = x;
					tile[color][index][1] = y;
				}
			}
		}
		
		for (int color = 0; color < 26; color ++) {
			
			if (tile[color][0][0] != -1) {
				
				tryGet(color);
			}
		}
		
		System.out.println(score * 2);
	}
	
	boolean tryGet(int color) {
		
		if (! cantGet[color][0]) {
			
			if ((tile[color][0][0] == tile[color][1][0]
					&& Math.abs(tile[color][0][1] - tile[color][1][1]) == 1)
					|| (tile[color][0][1] == tile[color][1][1]
					&& Math.abs(tile[color][0][0] - tile[color][1][0]) == 1)) {
				
				cantGet[color][0] = true;
				cantGet[color][1] = true;
				return false;
			}
			
			cantGet[color][0] = true;
			if((isEmpty(color, 1, tile[color][0][0], tile[color][0][1], tile[color][1][1])
					&& isEmpty(color, 0, tile[color][1][1], tile[color][0][0], tile[color][1][0]))) {
				
				
				if (tile[color][0][0] != -1) {
					
					tile[color][0][0] = -1;
					score ++;
				}
				return true;
			}
		}
		
		if (! cantGet[color][1]) {
			
			cantGet[color][1] = true;
			if ((isEmpty(color, 1, tile[color][1][0], tile[color][0][1], tile[color][1][1])
					&& isEmpty(color, 0, tile[color][0][1], tile[color][0][0], tile[color][1][0]))) {
				
				
				if (tile[color][0][0] != -1) {
					
					tile[color][0][0] = -1;
					score ++;
				}
				return true;
			}
			cantGet[color][0] = false;
			cantGet[color][1] = false;
		}
		
		return false;
	}
	
	boolean isEmpty(int skip,int axis, int point, int start, int end) {
		
		
		if (start == end) {
			
			return true;
		}
		
		if (start > end) {
			
			int swap = start;
			start = end;
			end = swap;
		}
		
		for (int color = 0; color < 26; color ++) {
			
			if (color == skip
					|| tile[color][0][0] == -1) {
				
				continue;
			}
			
			for (int index = 0; index < 2; index ++) {
				
				if (tile[color][index][1 - axis] == point
						&& tile[color][index][axis] >= start
						&& tile[color][index][axis] <= end
						&& !tryGet(color)) {
					
					return false;
				}
			}
		}
		
		return true;
	}
}
