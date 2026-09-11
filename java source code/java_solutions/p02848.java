import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner stdIn = new Scanner(System.in);
		int n = stdIn.nextInt(); //n個後の文字
		char[] s = stdIn.next().toCharArray(); //文字列S
		int [] ans = new int[s.length];
		char[] words = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		int count = 0;
		
		for(int i = 0; i < s.length; i++) {
			for(int j = 0; j < words.length; j++) {
				
				if(s[i] == words[j]) {
				ans[count++] = j;
				}
			}
		
		}
		
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < ans.length; j++) {
				if(ans[j] == 25) {
					ans[j] = 0;
				}else {
					
				ans[j]++;
				}
				
			}
		
		}
	
	
		for(int i = 0; i < ans.length; i++) {
			System.out.print(words[ans[i]]);
		}
	}
}
