import java.util.Scanner;

public class Main {
	
	Scanner sc = new Scanner(System.in);
	public void run() {
		int n = sc.nextInt();
		if(n != 0) calc(n);
		while(n != 0 && sc.hasNext()){
			n = sc.nextInt();
			if(n == 0) break;
			System.out.println();
			calc(n);
		}
	}
	
	public void calc(int n){
		int ten = sc.nextInt();
		int ten5 = sc.nextInt();
		int hund = sc.nextInt();
		int hund5 = sc.nextInt();
		
		int sumMon = ten * 10 + ten5 * 50 + hund * 100 + hund5 * 500;
		int returnMon = sumMon - n;
		
		int retTen = 0;
		int retTen5= 0;
		int retHund = 0;
		int retHund5 = 0;
		
		while(returnMon >= 500){
			returnMon = returnMon - 500;
			retHund5++;
		}
		while(returnMon >= 100){
			returnMon = returnMon - 100;
			retHund++;
		}
		while(returnMon >= 50){
			returnMon = returnMon - 50;
			retTen5++;
		}
		while(returnMon >= 10){
			returnMon = returnMon - 10;
			retTen++;
		}
		
		if(retTen < ten) {
			System.out.print("10 ");
			System.out.println(ten-retTen);
		}
		if(retTen5 < ten5) {
			System.out.print("50 ");
			System.out.println(ten5-retTen5);
		}
		if(retHund < hund) {
			System.out.print("100 ");
			System.out.println(hund-retHund);
		}
		if(retHund5 < hund5) {
			System.out.print("500 ");
			System.out.println(hund5-retHund5);
		}
	}
	public static void main(String[] args) {
		new Main().run();
	}
}
