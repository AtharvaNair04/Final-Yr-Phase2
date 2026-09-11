import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner kbd = new Scanner(System.in);

    void run(){
	int sum = 0;
	int x = 0;
	int y = 0;

	while(kbd.hasNext()){
	    sum += kbd.nextInt();
	}

	while(sum>=60){
	    x++;
	    sum -= 60;
	}
	y = sum;

	System.out.println(x+"\n"+y);
    }
}
