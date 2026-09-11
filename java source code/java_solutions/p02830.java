import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String ns = in.nextLine();
		//Scanner ins = new Scanner(System.in);
		String string = in.nextLine();
		int n = Integer.parseInt(ns);
		String s = string.split("\\s+")[0];
		String t = string.split("\\s+")[1];
		StringBuilder sb = new StringBuilder("");
		for(int i=0; i<n; i++) {
			sb.append(s.charAt(i));
			sb.append(t.charAt(i));
		}
		System.out.println(sb);
	}
}
