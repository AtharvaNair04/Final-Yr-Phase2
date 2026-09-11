import java.util.Scanner;
 
public class Main {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int[] num = new int[5];
		for(int i=0; i<5; i++) {
			num[i]=sc.nextInt();
		}
		int k=sc.nextInt();
		if(num[4]-num[0]>k) System.out.println(":(");
		else System.out.println("Yay!");
	}
}
