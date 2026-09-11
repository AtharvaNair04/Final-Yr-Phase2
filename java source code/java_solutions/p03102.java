import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int M = sc.nextInt();
		int C = sc.nextInt();sc.nextLine();
		String[] B = sc.nextLine().split(" ");
		String[] A = new String[N];
		int AB = 0, cnt = 0;
		String[] AM;
		for(int i = 0;i < N; i++) {
			A[i] = sc.nextLine();
			AB = 0;
			for(int j = 0; j < M; j++) {
				AM = A[i].split(" ");
				AB += Integer.parseInt(AM[j]) * Integer.parseInt(B[j]);
				//System.out.println(Integer.parseInt(AM[j]) + " " + Integer.parseInt(B[j]));
			}
			if(AB + C > 0) cnt++;
		}

		sc.close();
		System.out.println(cnt);
	}

}
