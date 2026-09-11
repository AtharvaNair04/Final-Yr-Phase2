import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int n;
    long[] a;
    long max;

    void run(){
	while(sc.hasNext()){
	    n = sc.nextInt();
	    if(n==0) break;
	    a = new long[n];
	    max = Integer.MIN_VALUE;
	    for(int i=0; i<n; i++)
		a[i] = sc.nextLong();

	    for(int first=0; first<n; first++){
		long sum=0;
		for(int p=first; p<n; p++){
		    sum += a[p];
		    max = Math.max(max, sum);
		}
	    }

	    System.out.println(max);
	}
    }
}
