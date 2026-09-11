import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int q = kbd.nextInt();
	    if(q!=-1) solve(q);
	}
    }

    static void solve(int q){
	double ans = 1;
	int i = 1;
	while(Math.abs(ans*ans*ans - q)>=0.00001*q){
	    ans = calc(i, ans, q);
	    i++;
	}
	System.out.printf("%.6f\n",ans);
    }
    static double calc(int i, double x, int q){
	if(i==1) return q/2;
	return x-(x*x*x-q)/(3*x*x);
    }
}
