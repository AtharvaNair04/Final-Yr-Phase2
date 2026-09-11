import java.io.*;
import java.util.*;

class Main{
    public static void main(String[] args){
	BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
	LinkedList<Integer> list = new LinkedList<Integer>();
	boolean[] prime = new boolean[110000];
	int[] s = new int[10000];
	prime[0] = true; prime[1] = true;
	for(int i=2; i<Math.sqrt(110000); i++)
	    if(!prime[i])
		for(int j=i*i; j<110000; j+=i)
		    prime[j] = true;
	for(int i=2; i<110000; i++)
	    if(!prime[i])
		list.add(i);
	s[0] = list.poll();
	for(int i=1; i<10000; i++)
	    s[i] = s[i-1]+list.poll();
	try {
	    while(true){
		int n = Integer.valueOf(sc.readLine());		
		if(n==0)
		    break;
		System.out.println(s[n-1]);
	    }
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
}
