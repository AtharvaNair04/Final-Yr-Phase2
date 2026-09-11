import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc=new Scanner(System.in);
		long a=sc.nextLong();
		long sum = 1;
		long mod = (long)Math.pow(10,9)+7;
		for(int i=1;i<=a;i++){
			sum*=i;
			sum%=mod;
		}
		System.out.print(sum);
	}
}
