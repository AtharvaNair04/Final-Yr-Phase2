import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	int n = kbd.nextInt();
	while(n>0){
	    String[][] c = {
		{null, null, null, null, null},
		{".", ",", "!", "?", " ", null},
		{"a", "b", "c", null, null, null},
		{"d", "e", "f", null, null, null},
		{"g", "h", "i", null, null, null},
		{"j", "k", "l", null, null, null},
		{"m", "n", "o", null},
		{"p", "q", "r", "s", null},
		{"t", "u", "v", null},
		{"w", "x", "y", "z", null},
	    };
	    solve(c);
	    n--;
	}
    }

    static void solve(String[][] c){
	String s = kbd.next();
	int i, cnt=0, a=0;
	String ans = "";
	for(i=0; i<s.length(); i++){
	    int b = Integer.parseInt(s.substring(i, i+1));
	    if(b!=0){
		a = b;
		cnt++;
		if(c[a][cnt-1]==null){
		    cnt = 1;
		}
		//System.out.println(a+" "+cnt);
	    }
	    else if(a==0);
	    else{
		ans += c[a][cnt-1];
		cnt = 0;
		a=0;
		//System.out.println(ans);
	    }
	}
	System.out.println(ans);
    }
}
