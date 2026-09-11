import java.util.Scanner;

class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		int l;
		int a, b, c, d;
		int dayJ = 0, dayM = 0;
		int day;
		
		l = scan.nextInt();
		a = scan.nextInt();
		b = scan.nextInt();
		c = scan.nextInt();
		d = scan.nextInt();
		
		if(a%c != 0){
			dayJ = a / c;
			dayJ++;
		}
		else dayJ = a / c;

		if(b%d != 0){
			dayM = b / d;
			dayM++;
		}
		else dayM = b / d;
		
		if(dayJ >= dayM)day = dayJ;
		else day = dayM;
		
		System.out.println((l - day));
	}
}
