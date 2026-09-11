import java.util.Scanner;


public class Main{

	public static void main(String args[]){

		long MOD=1000000007L;

		Scanner sc = new Scanner(System.in);

		String s=sc.next();
		char c[]=s.toCharArray();
		int k=c.length;
		int count=0;
		long p3[]=new long[k+1];
		long p2[]=new long[k+1];
		p2[0]=1L;
		p3[0]=1L;
		for(int i=1;i<=k;++i){
			p3[i]=p3[i-1]*3%MOD;
			p2[i]=p2[i-1]*2%MOD;
		}

		long ans=0L;

		for(int i=0;i<k;++i){
			if(c[i]=='1'){
				ans = (ans + p3[k-i-1]*p2[count])%MOD;
				++count;
			}
		}
		ans=(ans+p2[count])%MOD;
		System.out.println(ans);
	}
}
