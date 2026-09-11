import java.util.*;
public class Main {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		int a;
		int b;
		if(n<2){
			a=0;
		}else{
			a=n*(n-1)/2;
		}
		if(m<2){
			b=0;
		}else{
			b=m*(m-1)/2;
		}
		System.out.println(a+b);
	}
}
