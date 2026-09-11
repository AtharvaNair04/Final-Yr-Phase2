import java.util.*;
public class Main {

	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		String day[]={"sat","sun","mon","tue","wed","thu","fri"};
		System.out.println(day[(in.nextInt()+5)%7]);
	}

}
