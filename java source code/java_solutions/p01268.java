import java.util.*;
public class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    int n, m;
    boolean[] prime;
    int[] sum;
    
    void run(){
	prime = new boolean[1000000];
	setP();
	while(sc.hasNext()){
	    n = sc.nextInt();
	    m = sc.nextInt();
	    if(n==-1 && m==-1) break;
	 
	    int p = 0;
	    sum = new int[100000];
	    Arrays.fill(sum, Integer.MAX_VALUE);
	    for(int i=n+1; i<n+300&&p<sum.length; i++)
		for(int k=i; k<n+300&&p<sum.length; k++){
		    if(prime[i]&&prime[k]){
			sum[p] = i+k;
			p++;
		    }
		}
	    Arrays.sort(sum);
	    System.out.println(sum[m-1]);
	}
    }

    void setP(){
	Arrays.fill(prime, true);
	prime[0] = prime[1] = false;
	for(int i=2; i<prime.length; i++)
	    if(prime[i])
		for(int k=i+i; k<prime.length; k+=i)
		    prime[k] = false;
    }
}
