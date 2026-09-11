import java.util.Scanner;
//import java.io.*;
//import java.util.Arrays;
public class Main {
	public static void main(String[] args) throws java.io.IOException {
		Scanner scan = new Scanner(System.in);
		//InputStreamReader is = new InputStreamReader(System.in);
		//BufferedReader br = new BufferedReader(is);
		//String text = scan.next();
		
		while(scan.hasNext()){
			String text = scan.next();
			int count = 0;
			for(int i = 0 ; i < Integer.parseInt(text) ; i++){
				++count;
				if(count == 40){
					count = 1;
				}
			}
			if(Integer.toString(count).length() == 1){
				System.out.println("3C0" + count);
			}else{
				System.out.println("3C" + count);
			}
			
		}
	}
}
