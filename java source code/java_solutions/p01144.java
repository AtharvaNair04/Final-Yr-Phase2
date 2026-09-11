import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	
	Scanner sc = new Scanner(System.in);
	public void run() {
		while(sc.hasNext()){
			int n = sc.nextInt();
			int m = sc.nextInt();
			if(n == 0 && m == 0) break;
			calc(n, m);
		}
	}
	public void calc(int n, int m){
		int[] list = new int[11];
		for(int i = 0; i < n; i++){
			int d = sc.nextInt();
			int p = sc.nextInt();
			list[p] = list[p] + d;
		}
		for(int i = 10; i > 0; i--){
			if(m == 0) break;
			else if(list[i] <= m){
				m = m - list[i];
				list[i] = 0;
			}
			else{
				list[i] = list[i] - m;
				break;
			}
		}
		int sum = 0;
		for(int i = 0; i < 11; i++){
			sum = sum + list[i] * i;
		}
		System.out.println(sum);
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
