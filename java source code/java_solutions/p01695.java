import java.util.Scanner;
import java.util.ArrayList;

public class Main {
	Scanner sc = new Scanner(System.in);
	public void run(){
		while(true){
			int n = sc.nextInt();
			if(n == 0) break;
			calc(n);
		}
	}
	public void calc(int n){
		char[][] words = new char[n][];
		
		words[0] = sc.next().toCharArray();
		
		for(int i = 1; i < n; i++){
			words[i] = sc.next().toCharArray();
			
			int before = -1;
			int mmax = 0;
			for(int k = 0; k < words[i].length; k++){
				if(words[i][k+1] != '.'){
					words[i][k] = '+';
					for(int j = i-1; j >= 0; j--){
						if(words[j][k] == ' '){
							words[j][k] = '|';
						}
						else break;
					}
					break;
				}
				else {
					words[i][k] = ' ';
				}
			}
		}
		
		
		for(int i = 0; i < n; i++){
			System.out.println(words[i]);
		}
	}
	
	public static void main(String[] args){
		new Main().run();
	}
	
}
