import java.util.*;

public class Main {
	public static void main(String[] args) {

		String s = "2017/01/";
		Scanner scanner = new Scanner(System.in);
		s= scanner.next();
		String st = "2018/01/";
		System.out.println(st+s.substring(8));
		scanner.close();
	}

}
