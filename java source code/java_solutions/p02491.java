import java.util.*;
public class Main {
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args) {

	while(kbd.hasNext()){
	    int a = kbd.nextInt();
	    int b = kbd.nextInt();
	
	    if(a==0 && b==0){
		;
	    }
	    else{
		int x = a/b;
		int y = a%b;
		double c = a;
		double d = b;
		double z = c/d;
		System.out.printf("%d %d %f\n",x,y,z);
	    }
	}
    }
}
