import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    if(n!=0){
		int[] p = new int[n];
		int i;
		for(i=0; i<n; i++){
		    p[i] = kbd.nextInt();
		}

		int s = 0;
		i=0;
		while(i<n){
		    while(i+1<n && p[i]+1 == p[i+1]) i++;
		    if(s==i)
			System.out.print(p[s]);
		    else
			System.out.print(p[s]+"-"+p[i]);
		    i++;
		    s = i;
		    if(i<n) System.out.print(" ");
		}
		System.out.println();
	    }
	}
    }
}
