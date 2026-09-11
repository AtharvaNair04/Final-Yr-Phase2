import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int x, y, z, e, m;

    void run(){
	while(sc.hasNext()){
	    e = sc.nextInt();
	    if(e==0) break;
	    m = Integer.MAX_VALUE;
	    for(z=(int)Math.cbrt(e); z>=0; z--)
		for(y=(int)Math.sqrt(e); y>=0; y--){
		    x = e - y*y - z*z*z;
		    if(x<0) continue;
		    m = Math.min(x+y+z, m);
		}

	    System.out.println(m);
	}
    }
}
