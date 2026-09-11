import java.util.Scanner;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int num = 0;
		
		for (int i = 0; i < Math.sqrt(Math.pow(100000, 2)) + 1; i++) {
			if (N == i * (i+1)/2) {
				System.out.println("Yes");
				num = i;
				System.out.println(num + 1);
				break;
			}
		}
		if (num == 0)
		System.out.println("No");
		else {
			int[][] s = new int [num+1][num];
			int count = 1;
			for (int i = 0; i < num;i++) {
				for (int j = 0; j< num-i; j++) {
					s[i][i + j] = count;
					s[i + j][i] = count;
					count++;
				}
				s[num][i] = s[i][i];
			}
			for (int i = 0; i < num + 1; i++) {
				System.out.print(num + " ");
				for (int j = 0; j < num; j++) {
					System.out.print(s[i][j] + " ");
				}
				System.out.println();
			}
			
		}
	}
}
