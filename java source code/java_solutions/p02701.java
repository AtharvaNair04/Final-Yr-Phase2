import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		int sum = 1;
		String[] s = new String[n];
		for(int i = 0; i < n; i++) {
			s[i] = sc.next();
		}
		java.util.Arrays.sort(s);
		
		for(int i= 1; i < n; i++) {
			if(!s[i].equals(s[i-1])) {
				sum++;
			}
		}
		System.out.println(sum);
	}
	
}
