import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	
	double a = kbd.nextDouble();
	double b = kbd.nextDouble();
	double c = kbd.nextDouble();
	double d, s, l, h;

	/*
	if(c==90){
	    s = a*b*0.5;
	    d = Math.sqrt(a*a+b*b);	    
	    l = a+b+d;
	    h = b;
	}
	*/

	//else {
	s = 0.5*a*b*Math.sin(Math.toRadians(c));

	d = Math.sqrt(a*a+b*b-2*a*b*Math.cos(Math.toRadians(c)));
	
	l = a+b+d;
	
	h = s*2/a;
	//}
	
	System.out.println(s);	
	System.out.println(l);	
	System.out.println(h);
    }
}
