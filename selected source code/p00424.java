import java.util.*;
public class Main {
    Scanner kbd = new Scanner(System.in);
    HashMap<String, String>  con; 
    public static void main(String[] args){
	new Main().run();
    }
    
    void run(){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    if(n!=0){
		con = new HashMap<String, String>();
		String in;
		String out;
		StringBuffer ans = new StringBuffer();
      		while(n-- != 0){
		    con.put(kbd.next(), kbd.next());
		}

		n = kbd.nextInt();
		while(n-- != 0){
		    String a = kbd.next();
		    if(con.containsKey(a))  ans.append(con.get(a));
		    else ans.append(a);
		}
		System.out.println(ans.toString());
	    }
	}
    }
}
