import java.util.Scanner;
 
public class Main {
 
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
 
		String a = sc.next();
		
		long len = a.length();
		
		long[] f = new long[26];
		
		for(int i = 0; i < 26; i++) {
			f[i] = 0;
		}
		
		for(int i = 0; i < len; i++) {
			f[a.charAt(i)-'a']++;
		}
		
		long sum = 0;
		for(long x:f) {
			sum += x * (x - 1) / 2;
		}
 
		System.out.println(len*(len - 1) / 2 - sum + 1);
		
	}
}
