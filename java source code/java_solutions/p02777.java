import java.util.*;

class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		String S = sc.next();
		String T = sc.next();
		
		int A = sc.nextInt();
		int B = sc.nextInt();
		
		String U = sc.next();
		
		int ansA = A;
		int ansB = B;
		
		if (S.equals(U)) {
			ansA = A-1;
		} else if (T.equals(U)) {
			ansB = B-1;
		}
		System.out.println(ansA + " " + ansB);
	}
}
