import java.util.*;

public class Main {

	int h, w;
	char[][] board;
	char[] directions = { 'U', 'L', 'D', 'R' };
	int[] directionX = { -1, 0, 1, 0 };
	int[] directionY = { 0, -1, 0, 1 };
	Stack<Character> solution = new Stack<Character>();
	
	int countLeaves() {
		int sum = 0;
		for (int i=0; i<board.length; i++){
			for (int j=0; j<board[0].length; j++){
				if (board[i][j] == 'o'){
					sum++;
				}
			}
		}
		return sum;
	}

	boolean dfs(int x, int y, int d) { // x: height; y: width
		int i = x, j = y;
		board[x][y] = '.';
		if (countLeaves() == 0){
			return true;
		}
		d--;
		for (int rep = 0; rep < 4; rep++) {
			i = x;
			j = y;
			d++;
			d %= 4;
			if (rep == 2)
				continue;
			while (i < board.length && i >= 0 && j < board[0].length && j >= 0) {
				i += directionX[d];
				j += directionY[d];
				if (!(i < board.length && i >= 0 && j < board[0].length && j >= 0)){
					break;
				}
				if (board[i][j] == 'o') {
					solution.push(directions[d]);
					if (dfs(i, j, d))
						return true;
					else
						break;
					
				}
			}
		}

		board[x][y] = 'o';
		solution.pop();
		return false;
	}

	public void solve() {
		Scanner in = new Scanner(System.in);
		h = in.nextInt();
		w = in.nextInt();
		String[] sBoard = new String[h];
		board = new char[h][w];
		for (int i = 0; i < h; i++) {
			sBoard[i] = in.next();
			board[i] = sBoard[i].toCharArray();
		}
		int d = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if (board[i][j] == 'U' || board[i][j] == 'L'
						|| board[i][j] == 'D' || board[i][j] == 'R') {
					if (board[i][j] == 'U')
						d = 0;
					else if (board[i][j] == 'L')
						d = 1;
					else if (board[i][j] == 'D')
						d = 2;
					else
						d = 3;
					board[i][j] = '.';
					dfs(i, j, d);
				}
			}
		}

		in.close();
		
		Object res[] = solution.toArray();
		for (Object c : res){
			System.out.print(c);
		}
		System.out.println();
		
	}

	public static void main(String[] args) {
		new Main().solve();
	}

}
