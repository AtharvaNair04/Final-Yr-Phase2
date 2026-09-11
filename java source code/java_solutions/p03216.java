import java.util.Arrays;
import java.util.Scanner;

public class Main{
	public static void main(String args[]) {
	
		Scanner sc=new Scanner(System.in);
		int n=sc.nextInt();
		String s=sc.next();
		int q=sc.nextInt();
		int k[]=new int[q];

		
		for(int i=0;i<q;++i) {
			k[i]=sc.nextInt();
			int countd=0, countm=0;
			long ans=0,next=0;
			for (int m=0;m<n;++m) {
				
				if(m-k[i]>=0) {
				if(s.charAt(m-k[i])=='D') {--countd; next=next-countm;}
				else if(s.charAt(m-k[i])=='M')--countm; 
				}
				
				if(s.charAt(m)=='D')++countd;
				else if(s.charAt(m)=='M') {++countm; next=next+countd;} 
				else if(s.charAt(m)=='C')ans=ans+next;
			}
			System.out.println(ans);
		}
		
	}
}
