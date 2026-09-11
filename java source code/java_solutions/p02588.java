import java.util.*;
import java.math.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int n=sc.nextInt();
		sc.nextLine();
		BigDecimal d;
		BigDecimal g=new BigDecimal("1000000000");
		int[][] c=new int[19][19];
		long a;
		int t;
		int f;
		for(int i=0;i<n;i++){
			t=0;
			f=0;
			d=new BigDecimal(sc.nextLine());
			d=d.multiply(g);
			a=d.longValue();
			//System.out.println(a);
			while(a%2==0){
				t++;
              	//System.out.println(a);
				a=a/2;
			}
			while(a%5==0){
				f++;
              	//System.out.println(a);
				a=a/5;
			}
			if(t>18){
				t=18;
			}
			if(f>18){
				f=18;
			}
          	//System.out.println(t);
          	//System.out.println(f);
			c[t][f]++;
		}
		long[][] s=new long[20][20];
		for(int i=18;i>=0;i--){
			for(int j=18;j>=0;j--){
				s[i][j]=s[i+1][j]+s[i][j+1]-s[i+1][j+1]+c[i][j];
			}
		}
		long ans=0;
      	for(int i=0;i<=18;i++){
			for(int j=0;j<=18;j++){
				ans+=c[i][j]*s[18-i][18-j];
			}
		}
      	//System.out.println(ans);
      	ans-=s[9][9];
		ans=ans/2;
      	System.out.println(ans);
	}
}
