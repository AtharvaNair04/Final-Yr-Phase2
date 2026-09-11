import java.util.*;

class Main{
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int h = scan.nextInt();
		int w = scan.nextInt();
		scan.nextLine();
		int[][] arr = new int[h][w];
		for(int i = 0; i < h; i++) {
			String[] line = scan.nextLine().split(" ");
			for(int j = 0; j < w; j++) {
				arr[i][j] = Integer.parseInt(line[j]);
			}
		}
		int[][] ansArr = new int[h][w];
		int ans = 0;
		for(int i = 0; i < h; i++) ansArr[i][0] = (arr[i][0]==0)?1:0;
		for(int i = 0; i < w; i++) ansArr[0][i] = (arr[0][i]==0)?1:0;
		for(int i = 1; i < h; i++) {
			for(int j = 1; j < w; j++) {
				ansArr[i][j] = (arr[i][j]==0)?Math.min(Math.min(ansArr[i-1][j],ansArr[i][j-1]),ansArr[i-1][j-1])+1:0;
			}
		}
		for(int[] _arr : ansArr) for(int num : _arr) ans = Math.max(ans,num);
		System.out.println(ans*ans);
	}
}
