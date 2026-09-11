import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			int n = sc.nextInt();
			int m = sc.nextInt();
			
			if (n == 0 && m == 0) {
				break;
			}
			
			int a[] = new int[n];
			int an = 0;
			int answer = 0;
			int a_min = 1000001;
			int a_second_min = 1000001;
			for (int i = 0; i < n; i++) {
				an = sc.nextInt();
				a[i] = an;
				if (an < a_min) {
					a_second_min = a_min;
					a_min = an;
				} else if (an < a_second_min) {
					a_second_min = an;
				}
				
				for (int j = 0; j < i; j++) {
					if ((a[j] + an) > answer && (a[j] + an) <= m) {
						answer = a[j] + an;
					}
				}
			}
			
//			System.out.println(a_min);
//			System.out.println(a_second_min);
//			
			if ((a_min + a_second_min) > m) {
				System.out.println("NONE");
			} else {
				System.out.println(answer);
			}
			
			
		}
	}
}
