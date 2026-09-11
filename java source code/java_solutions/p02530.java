import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int taro = 0, hanako = 0;
		int turn = sc.nextInt();
		String d, e;
		for (int i = 0; i < turn; i++) {
			d = sc.next();
			d = d.toLowerCase();
			e = sc.next();
			e = e.toLowerCase();
			int hantei = d.compareTo(e);
			if (hantei < 0) {
				hanako += 3;
			} else if (hantei == 0) {
				taro += 1;
				hanako += 1;
			} else if (hantei > 0) {
				taro += 3;
			}
		}
		System.out.println(taro + " " + hanako);
	}
}
