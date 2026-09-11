import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    String s;
    String[] w;

    void run(){
	s = sc.nextLine();
	w = s.split("[ ,.]");
	show();
    }

    void show(){
	boolean one = true;
	int i;
	for(i=0; i<w.length; i++)
	    if(w[i].length()>2 && w[i].length()<7)
		if(one) {
		    System.out.print(w[i]);
		    one = false;
		}
		else 
		    System.out.print(" "+w[i]);
	System.out.println();
    }
}
