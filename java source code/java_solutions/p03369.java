import java.util.*;
public class Main{
	public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        int ans = 0;
        if(s.charAt(0)=='o') ans++;
        if(s.charAt(1)=='o') ans++;
        if(s.charAt(2)=='o') ans++; 
        System.out.println(700+100*ans);
    }
}
