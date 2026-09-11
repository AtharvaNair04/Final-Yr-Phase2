//package AtCoder;
import java.util.*;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		long n=sc.nextLong();
		long x=sc.nextLong();
		long ans=3*(n-gcd(n,x));
		System.out.println(ans);
	}
	static long gcd(long a,long b)
	{
		if(b==0)
			return a;
		return gcd(b,a%b);
	}

}
