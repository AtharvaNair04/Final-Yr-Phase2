//Cutting Down Water Bills
import java.io.IOException;
import java.util.Scanner;

class Main {

	public static void main(String[] args) throws IOException {

		Scanner scan =new Scanner(System.in);
		
		int n;
	      while ((n = scan.nextInt())!=-1) { 


			int p;
			if(n<=10){
				p=1150;
			}else if(n<=20){
				p=1150+(n-10)*125;
			}else if(n<=30){
				p=2400+(n-20)*140;
			}else{
				p=3800+(n-30)*160;
			}

			 System.out.println(4280-p);
			
		}

	}
}
