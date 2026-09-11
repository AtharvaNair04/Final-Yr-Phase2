import java.util.*;
public class Main{
    static Scanner kbd = new Scanner(System.in);
    public static void main(String[] args){
	while(kbd.hasNext()){
	    int n = kbd.nextInt();
	    if(n!=0) solve(n);
	}
    }

    static void solve(int n){
	int[] a = new int[n];
	int i;
	for(i=0; i<a.length; i++)
	    a[i] = kbd.nextInt();
	Arrays.sort(a);
	System.out.print(a[0]);
	for(i=1; i<a.length; i++)
	    System.out.print(" "+a[i]);
	System.out.println();
    }
}
