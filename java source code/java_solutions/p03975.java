import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int A = sc.nextInt();
		int B = sc.nextInt();
		int t = 0;

		int count = 0;

		for(int i = 0; i < N ;i++){

			t = sc.nextInt();

			if(A <= t && t <B){
			}else{
				count++;
			}

		}



		System.out.println(count);

		sc.close();
	}
}
