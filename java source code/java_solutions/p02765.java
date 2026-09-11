import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		// 整数の入力
		int n = sc.nextInt();
		// スペース区切りの整数の入力
		int r = sc.nextInt();
		int i;
		if (n<10) {
			i=r+1000-100*n;
		} else {
			i=r;
		}
		System.out.println(i);
	}
}
