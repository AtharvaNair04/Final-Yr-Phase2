import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		int m = sc.nextInt();
		int[] h = new int[n];
		for(int i = 0; i < n; i++) {
			h[i] = sc.nextInt();
		}
		int[][] pea = new int[m][2];
		for(int i = 0; i < m; i++) {
			pea[i][0] = sc.nextInt();
			pea[i][1] = sc.nextInt();
		}
		int[] tem = new int[n];
		for(int i = 0; i < n; i++) {
			tem[i] = 0;
		}
		
		
		for(int i = 0; i < m; i++) {
			if(h[pea[i][0]-1] > h[pea[i][1]-1]) {
				tem[pea[i][1]-1] = -1;
			}else if(h[pea[i][0]-1] < h[pea[i][1]-1]) {
				tem[pea[i][0]-1] = -1;
			}else if(h[pea[i][0]-1] == h[pea[i][1]-1]) {
				tem[pea[i][1]-1] = -1;
				tem[pea[i][0]-1] = -1;
			}
		}
		int cnt = 0;
		for(int i = 0; i < n; i++) {
			if(tem[i] == 0) {
				cnt++;
			}
		}
		System.out.println(cnt);
	}
}
