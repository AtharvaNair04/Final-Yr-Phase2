import java.util.Scanner;

public class Main{
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		while(true){
			int n = in.nextInt();
			if(n==0) break;
			int[] k = new int[n];
			for(int i=0; i<n; i++){
				k[i] = in.nextInt();
			}
			char[] s = in.next().toCharArray();
//			int[] num = new int[s.length];
			for(int i=0; i<s.length; i++){
				int val = 'a'<=s[i]&&s[i]<='z'?s[i]-'a' : s[i]-'A'+26;
				val = (val + 52 - k[i%n])%52;
				s[i] = (char) ((val < 26)? 'a'+val: 'A'+val-26);
			}
			System.out.println(String.valueOf(s));
		}
	}
}
