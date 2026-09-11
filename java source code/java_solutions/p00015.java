import java.util.*;
import java.math.*;
public class Main{
    public static void main(String[]args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int t;
    BigInteger a;
    BigInteger b;
    BigInteger sum;
    String ans;
   
    void run(){
	while(sc.hasNext()){
	    t = sc.nextInt();
	    while(t>0){
		t--;
		a = sc.nextBigInteger();
		b = sc.nextBigInteger();
		sum = a.add(b);
		ans = sum.toString();
		System.out.println
		    (ans.length()>80 ? "overflow":ans);
	    }
	}
    }
}
