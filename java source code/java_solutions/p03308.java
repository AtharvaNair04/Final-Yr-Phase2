import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();

		int[] num = new int[N];

		for(int i = 0;i < N;i++){
			num[i]= sc.nextInt();
		}

		int max = num[0];
		int min = num[0];

		for(int i = 0;i < N;i++) {
			if(max < num[i]) {
				max = num[i];
			}
		}

		for(int i = 0;i < N;i++) {
			if(min > num[i]) {
				min = num[i];
			}
		}
		System.out.println(max - min);
	}
}
