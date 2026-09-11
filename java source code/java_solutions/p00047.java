import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    void run(){
	String ans = solve();
	System.out.println(ans);
    }

    String solve(){
	String ans = "A";
	while(sc.hasNext()){
	    String s = sc.next();
	    String l = s.substring(0, 1);
	    String r = s.substring(2, 3);
	    if(ans.equals(l)) ans = r;
	    else if(ans.equals(r)) ans = l;
	    //System.out.println("*"+ans);
	}
	return ans;
    }
}
