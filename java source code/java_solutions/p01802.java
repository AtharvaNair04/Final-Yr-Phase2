import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
public class Main {
	public static void main(String[] args) {
		try(Scanner sc = new Scanner(System.in)) {
			while(true) {
				int d = sc.nextInt();
				int e = sc.nextInt();
				double min = 10000000;
				if (d==0 && e==0)	break;
				for(int x=0; x<=d; x++) {
					for(int y=0; y<=d; y++) {
						if(x+y == d) {
							double zure = Math.abs(Math.sqrt(x*x+y*y)-e);
							if(zure < min)	min = zure;
						}
					}
				}
				System.out.println(min);
			}
			
			
			
			
			
			
		}
	}
}
