import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		Scanner cd =new Scanner(System.in);
		
		int x = cd.nextInt();
		int y = cd.nextInt();
		int z = cd.nextInt();
		
		int num = (x-z)/(y+z);
		System.out.println(num);
	}

}
