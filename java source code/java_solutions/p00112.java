import java.util.*;
class Main{
    public static void main(String[] args){
	new Main().run();
    }
    Scanner sc = new Scanner(System.in);

    void run(){
	int n;
	while(true){
	    n = sc.nextInt();
	    if(n==0) break;

	    int[] milk = new int[n];
	    for(int i=0; i<n; i++)
		milk[i] = sc.nextInt();
	    Arrays.sort(milk);

	    long cost = 0;
	    for(int i=0; i<n; i++)
		for(int k=0; k<i; k++){
		    //System.out.println(i+" "+k);
		    cost += milk[k];
		}

	    System.out.println(cost);
	}
    }
}
