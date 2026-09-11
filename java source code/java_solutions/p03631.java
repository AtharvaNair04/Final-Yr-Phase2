import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		while(scan.hasNext()){
			int num = scan.nextInt();
			
			int b = num%1000/100;;
			int s = num%1000%100/10;
			int g = num%1000%100%10;
			
			if(num == g*100+s*10+b){
				System.out.println("Yes");
				System.out.println();
			}
			else{
				System.out.println("No");
				System.out.println();
			}
		}
	}
}
