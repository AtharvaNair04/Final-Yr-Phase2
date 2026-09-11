import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

		public static void main(String args[]) throws IOException{
			Scanner scan = new Scanner(System.in);

			double cx1, cy1, r1;
			double cx2, cy2, r2;
			
			//if(args.length != 0){
				cx1 = scan.nextDouble();
				cy1 = scan.nextDouble();
				r1 = scan.nextDouble();
				cx2 = scan.nextDouble();
				cy2 = scan.nextDouble();
				r2 = scan.nextDouble();
				
				double l = (Math.pow(cx1 - cx2, 2) + Math.pow(cy1 - cy2, 2));
				double rlen = r1+r2;
				double revlen = r1 - r2;
				if(l > rlen*rlen) 
					System.out.println("4");
				else if(l == rlen*rlen) 
					System.out.println("3");
				else if(l < rlen*rlen) {
					if(l > revlen*revlen)
						System.out.println("2");
					else if(l == revlen*revlen)
						System.out.println("1");
					else if(l < revlen*revlen)
						System.out.println("0");
				}
			//}
				scan.close();
			//else System.out.println("Input doesn't be exist");
		}	
	}
