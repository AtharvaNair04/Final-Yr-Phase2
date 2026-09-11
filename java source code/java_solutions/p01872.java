import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		String p=sc.next();
		
		int[] num=new int[13];
		int sum, k=0, count=0, ans=0;
		
		for(int i=11; i>=0; i--) {
			if(p.charAt(11-i)=='?')	{
				k=i;
			}
			else {
				num[i]=p.charAt(11-i)-'0';
			}
		}
		
		for(int j=0; j<=9; j++) {
			num[k]=j;
			sum = 0;
			for(int n=11; n>=1; n--) {
				if(n<=6) {
					sum+=num[n]*(n+1);
				}
				else if(n>6) {
					sum+=num[n]*(n-5);
				}
			}
				sum %= 11;
				if(sum <= 1) sum=0;
				else sum=11-sum;
				if(sum == num[0]) {
					ans = num[k];
					count++;
				}
		}
		if(count>1)	System.out.println("MULTIPLE");
		else System.out.printf("%d\n", ans);
	}
}
