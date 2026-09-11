import java.util.Scanner;
import java.util.Stack;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		String S = sc.nextLine();
		int num = 0;
		Stack<Character> stack = new Stack<Character>();
		for(int i = S.length() - 1; i >=0; i--) {
			if(stack.isEmpty()) {
				stack.push(S.charAt(i));
			}else {
				char a = stack.peek();
				if(a != S.charAt(i)) {
					stack.pop();
					num += 2;
				}else {
					stack.push(S.charAt(i));
				}
			}
		}
		System.out.println(num);
    }
}
