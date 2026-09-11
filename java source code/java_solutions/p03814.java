import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String s=scan.nextLine();
		int c=-1,z=0;
		for(int i=0;i<s.length();i++) {
			if(s.charAt(i)=='A' && c==-1) {
				c=i;
			}
			if(s.charAt(i)=='Z') {
				z=i;
			}
		}
		System.out.println(z-c+1);
	}

}
