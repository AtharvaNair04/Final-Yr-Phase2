import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	
	Scanner sc = new Scanner(System.in);
	LinkedList<Integer> array = new LinkedList<Integer>();
	public void run() {
			int n = sc.nextInt();
		calc(n);
	}
	
	public void calc(int n){
		int[][] gyouretsu = new int[n][n];
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				gyouretsu[i][j] = 0;
			}
		}
		for(int i = 0; i < n; i++){
			int u = sc.nextInt();
			int k = sc.nextInt();
			for(int j = 0; j < k; j++){
				int v = sc.nextInt();
				gyouretsu[u-1][v-1] = 1;
			}
		}
		int[] ans = new int[n];
		int[] kita = new int[n];
		for(int i = 0; i < n; i++){
			ans[i] = -1;
			kita[i] = -1;
		}
		array.push(0);
		kita[0] = 0;
		rec(gyouretsu, 0, ans, kita);
	
		for(int i = 0; i < n; i++){
			System.out.println(i+1 + " " + ans[i]);
		}
	}
	public void rec(int[][] gyouretsu, int t, int[] ans, int[] kita){
		int s = array.size();
		for(int i = 0; i < s; i++){
			int now = array.get(0);
			ans[now] = t;
			array.remove(0);
			for(int j =0; j <  gyouretsu[now].length; j++){
				if(gyouretsu[now][j] == 1 && kita[j] == -1){
					array.add(j);
					kita[j] = 0;
				}
			}
		}
		if(array.size() != 0){
		rec(gyouretsu, t+1, ans, kita);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
