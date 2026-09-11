import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);
    
    long a, b;
    long ans;

    void run(){
	while(sc.hasNext()){
	    a = sc.nextInt();
	    b = sc.nextInt();
	    if(a!=0 && b!=0)
		solve();
	}
    }

    void solve(){
	if(a>b){
	    long tmp = a;
	    a = b;
	    b = tmp;
	}
	/*
	  a < b ??即???????????????
	  a*b == GCD*LCM ??則?????????
	 */
	long lcm = b;	
	while(lcm%a!=0) {
	    lcm+=b;
	    //System.out.println(a+" "+b+" "+lcm);
	}
	ans = a*b/lcm;

	System.out.println(ans);
    }
}
