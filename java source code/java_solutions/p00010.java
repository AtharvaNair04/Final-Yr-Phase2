import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    void run(){
	int n = sc.nextInt();
	while(n>0){
	    solve();
	    n--;
	}
    }

    void solve(){
	double x1 = sc.nextDouble();
	double y1 = sc.nextDouble();
	double x2 = sc.nextDouble();
	double y2 = sc.nextDouble();
	double x3 = sc.nextDouble();
	double y3 = sc.nextDouble();

	/* 
	   中心を(a, b)として
	   a = (A-2b(y1-y2))/2(x1-x2) = (B-2b(y1-y3))/2(x1-x3)
	   b = (B(x1-x2)-A(x1-x3)) / 2C
	*/
	double A = x1*x1 - x2*x2 + y1*y1 - y2*y2;
	double B = x1*x1 - x3*x3 + y1*y1 - y3*y3;
	double C = (y1-y3)*(x1-x2) - (y1-y2)*(x1-x3);

	double b = (B*(x1-x2)-A*(x1-x3)) / (2*C);
	double a = (B*(y1-y2)-A*(y1-y3)) / (2*(-C));
	double r = Math.sqrt((x1-a)*(x1-a)+(y1-b)*(y1-b));

	System.out.printf("%.3f %.3f %.3f\n", a, b, r);
    }
}
