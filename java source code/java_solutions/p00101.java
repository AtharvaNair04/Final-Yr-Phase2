import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	int n = Integer.parseInt(kbd.nextLine());
	while(n>0){
	    solve();
	    n--;
	}
    }

    static void solve(){
	String s = kbd.nextLine();
	String ans = s.replaceAll("Hoshino", "Hoshina");
	System.out.println(ans);
    }
}
