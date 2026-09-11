import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Scanner stdIn = new Scanner(System.in);
		int x = stdIn.nextInt();
		int y = stdIn.nextInt();
		
		if (x < y) {
			if(x==0)
				System.out.println(y);
			if(y==0)
				System.out.println(-x);
			if (x > 0 || y < 0)
				System.out.println(y - x);
			if (x < 0 && y > 0) {
				if ((-x) >= y){
					System.out.println(-x-y+1);
				}
				else
					System.out.println(x+y+1);
			}
		}
		if (y<x) {
			if(x==0)
				System.out.println(1-y);
			if(y==0)
				System.out.println(1+x);
			if (y >0 || x < 0)
				System.out.println(2+x-y);
			if (y < 0 && x > 0) {
				if ((-y) >= x)
					System.out.println(1-x - y);
				else
					System.out.println(1 + x +y);
			}
		}

	}
}
