import java.io.*;
import java.util.*;
 
class Main{
    public static void main(String[] args){
        BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
        try{
	    String[] st = sc.readLine().split(" ");
	    int N = Integer.valueOf(st[0]), M = Integer.valueOf(st[1]);
	    int[] pool = new int[N];
	    TreeSet<Integer> aa = new TreeSet<Integer>();
	    for(int i=0; i<N; i++)
	        pool[i]=Integer.valueOf(sc.readLine());
	    int c = 0;
	    for(int i=N-1; i>=0; i--){
		if(!aa.contains(pool[i])){
		    System.out.println(pool[i]);
		    aa.add(pool[i]);
		    if(++c==M)
			break;
		}
	    }
	}catch(Exception e){
            System.out.println("Error");
	}
    }
}
