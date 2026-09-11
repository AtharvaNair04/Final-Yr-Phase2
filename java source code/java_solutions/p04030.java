import java.util.*;
public class Main {
	public static void main(String[] args) {
		Scanner in=new Scanner (System.in);
		String s=in.next();
		String t="";
		for (int i = 0; i < s.length(); i++) {
			if(s.charAt(i)=='0') {
				t=t+s.charAt(i);
			}
			if(s.charAt(i)=='1') {
				t=t+s.charAt(i);
			}
			if(s.charAt(i)=='B'&& t.length()>0) {
				t=t.substring(0,t.length()-1);
			}
		}
		System.out.println(t);
	}
}
