import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt();
		int b = sc.nextInt();
		double x = (a+b)/2.;
	
		if((x-(int)x)>=0.5){
			x++;
		}
		System.out.println((int)x);
	}
}
