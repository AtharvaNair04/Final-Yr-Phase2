import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int x = sc.nextInt();
		int[] children = new int[n];

		for(int i = 0; i < n; i++) {
			children[i] = sc.nextInt();
		}
		for(int i = 0; i < n; i++) {
			for(int j = i+1; j < n; j++) {
				if(children[i] > children[j]) {
					int tmp = children[i];
					children[i] = children[j];
					children[j] = tmp;
				}
			}
		}
		int count = 0;
		for(int i = 0; i < n; i++) {
			if((x = x - children[i]) >= 0) {
				count++;
			}else {
				break;
			}
			if(i == n-1) {
				if(x > 0) {
					count--;
				}
			}
		}
		System.out.println(count);
	}
}
