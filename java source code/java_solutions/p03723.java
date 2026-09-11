import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt(), b = sc.nextInt(), c = sc.nextInt();
		Set<String> s = new HashSet<String>();
		int cnt = 0;
		s.add(""+a+b+c);
		boolean f = false;
		
		while(true) {
			if(a % 2== 1 || b % 2== 1||c % 2== 1)	break;
			int aa = (b+c)/2, bb = (a+c)/2, cc = (a+b)/2;
			a = aa; b = bb; c = cc;
			if(s.contains(""+a+b+c)) {
				f = true;
				break;
			}
			cnt ++;
		}
		
		if(f)
			System.out.println(-1);
		else
			System.out.println(cnt);
		
	}
}
