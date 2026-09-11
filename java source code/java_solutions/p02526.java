import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	
	boolean[] s = new boolean[100000000];

	int n = kbd.nextInt();
	for(int i=0; i<n; i++){
	    int b = kbd.nextInt();
	    s[b] = true;
	}

	int m = kbd.nextInt();
	int cnt = 0;

	for(int i=0; i<m; i++){
	    int c = kbd.nextInt();
	    if(s[c]) cnt++;
	}

	System.out.println(cnt);
    }
}
