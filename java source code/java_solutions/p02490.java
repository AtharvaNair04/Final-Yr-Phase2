import java.util.*;
public class Main {
    public static void main(String[] args) {
	Scanner kbd = new Scanner(System.in);

	while(kbd.hasNext()){
	    int x = kbd.nextInt();
	    int y = kbd.nextInt();
	
	    if(x==0 && y==0){
		;
	    }
	    else{
		if(x<y){
		    System.out.println(x+" "+y);
		}
		else{
		    System.out.println(y+" "+x);
		}
	    }
	}
    }
}
