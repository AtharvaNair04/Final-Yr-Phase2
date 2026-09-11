//Volume0-0044
import java.util.*;

public class Main {

	//declare
	private static int   MAX = 50022;
	private static boolean[] prm = new boolean[MAX];
	
	public static void main(String[] args) {

		int n,
	    small = MAX,
	    large = 0;
		
		//initialize
		init();

		//input
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			n = sc.nextInt();
			
			//calculate
			for(int i=n-1;i>0;i--){
				if(prm[i]){small = i; break;}
			}
			for(int i=n+1;i<MAX;i++){
				if(prm[i]){large = i; break;}
			}
			
			//output
			System.out.println(small + " " + large);
		}
	}

	//Creation of a prime number table.
	private static void init(){
		for(int i=0;i<MAX;i++){prm[i] = true;}
		prm[0] = false;
		prm[1] = false;
		for(int i=4;i<MAX;i+=2){prm[i] = false;}
		for(int i=3;i<Math.sqrt(MAX);i+=2){
			if(!prm[i]){continue;}
			for(int k=i*2;k<MAX;k+=i){
				prm[k] = false;
			}
		}
	}
}
