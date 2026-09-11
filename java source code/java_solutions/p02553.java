import java.util.Scanner;
 
public class Main {
  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    long a=sc.nextLong();
    long b=sc.nextLong();
    long c=sc.nextLong();
    long d=sc.nextLong();
    
    long x=a*c;
    long y=a*d;
    long z=b*c;
    long w=b*d;
    
    if(x>=y&&x>=z&&x>=w)
    {
    	System.out.println(x); 
    }else {
    	if(y>=z&&y>=w&&y>=x) {
    		System.out.println(y);
    	}else {
    		if(z>=w&&z>=y&&z>=x) {
    			System.out.println(z);
    		}else {
    			System.out.println(w);
    		}
    	}
    }
  }
}
