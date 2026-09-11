import java.util.Scanner;
public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);


		String[] s = stdIn.next().split("");

		int[] a = new int[s.length+1];

		for(int i = 0; i < s.length; i++) {
			if(s[i].equals("<")) {
				a[i+1] = a[i]+1;
			}
		}

		for(int i = s.length-1; i>=0; i--) {
			if(s[i].equals(">")){
				a[i] = Math.max(a[i+1]+1, a[i]);
			}
		}

		long sum = 0;
		for(int i = 0; i < a.length; i++) {
			sum += a[i];
		}

		System.out.println(sum);
	}

}
