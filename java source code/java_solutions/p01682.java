import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()) {
			String ss = sc.nextLine();
			if (ss.equals("#")) {
				break;
			}
			ss = ss.replace(" ", "");
			char[] s = ss.toCharArray();
			int n = s.length;
			int i = 0;
			long num = -1;
			Deque<Long> stack = new ArrayDeque<Long>();
			stack.offer(-1L);
			while(i < n) {
				if (s[i] == 'S') {
					i+=2;
					stack.push(-1L);
					continue;
				}
				if (Character.isDigit(s[i])) {
					num = 0;
					while(i<n) {
						if (Character.isDigit(s[i])) {
							num*=10;
							num+=s[i]-'0';
							i++;
						}else{
							break;
						}
					}
				}
				if ((i < n) && (i+2 >= n || s[i+2] == '>')) {
					long b = stack.pop();
					if (b >= 0) {
						if (num < 32) {
							num = b >> num;
						}else{
							num = 0;
						}
					}
					num = (num * num) % 1000000007;
					//stack.push(num);
					i++;
				}
				if (i >= n || (i+2 < n && s[i+2] != '>')) {
					long b = stack.pop();
					if (b >= 0) {
						if (num < 32) {
							num = b >> num;
						}else{
							num = 0;
						}
					}
					stack.push(num);
					i+=2;
				}
			}
			System.out.println(num);
		}
	}

}
