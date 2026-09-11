import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
		int n = sc.nextInt();
		int mulFirst = (int) new Expression(s).value();
		int leftToRight = s.charAt(0) - '0';
		int length = (s.length() + 1) / 2;
		for(int i=1;i<length;i++) {
			int v = s.charAt(i*2) - '0';
			char c = s.charAt(i*2-1);
			leftToRight = c == '+' ? leftToRight + v : leftToRight * v;
		}

		//System.err.println(n + "," + mulFirst + "," + leftToRight);
		if (n == mulFirst) {
			if (n == leftToRight) {
				System.out.println("U");
			}else{
				System.out.println("M");
			}
		}else{
			if (n == leftToRight) {
				System.out.println("L");
			}else{
				System.out.println("I");
			}
		}

		sc.close();
	}
}
class Expression {
	private String expression;
	private int begin = 0;
	public Expression(String expression) {
		this.expression = expression;
	}
	public double value() {
		double num = term();
		while(begin<expression.length()) {
			char c = expression.charAt(begin);
			if (c=='+') {
				begin++;
				num+=term();
			}else if(c=='-') {
				begin++;
				num-=term();
			}else{
				break;
			}
		}
		return num;
	}
	private double term() {
		double num = factor();
		while(begin<expression.length()) {
			char c = expression.charAt(begin);
			if (c=='*') {
				begin++;
				num*=factor();
			}else if(c=='/') {
				begin++;
				num/=factor();
			}else{
				break;
			}
		}
		return num;
	}
	private double factor() {
		char c = expression.charAt(begin);
		if (c=='(') {
			begin++; //(
			double num = value();
			begin++; //)
			return num;
		}else{
			return number();
		}
	}
	private int number() {
		int num = 0;
		while(begin<expression.length()) {
			if (Character.isDigit(expression.charAt(begin))) {
				num*=10;
				num+=expression.charAt(begin)-'0';
				begin++;
			}else{
				break;
			}
		}
		return num;
	}
}
