import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc=new Scanner(System.in);
		int n=sc.nextInt(),a=sc.nextInt(),b=sc.nextInt();
		int[] x=new int[n];
		for(int i=0;i<n;i++){
			x[i]=sc.nextInt();
		}
		long sum=0;
		for(int i=0;i<n-1;i++){
			long t=x[i+1]-x[i];
			if(t*a<=b)
				sum+=t*a;
			else
				sum+=b;
		}
		System.out.print(sum);
	}
}
