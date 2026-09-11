import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String s=scan.nextLine();
		String t="";
		t+=s.charAt(5);
		t+=s.charAt(6);
		int a=Integer.parseInt(t);
		//System.out.println(a);
		if(a<=04) System.out.println("Heisei");
		else System.out.println("TBD");
	}

}
