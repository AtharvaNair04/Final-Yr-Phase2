import java.io.*;
import java.util.*;

class Main{
    public static void main(String[] args){
	BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
	int[] prime = new int[32769];
	int[] map = new int[32769];
	prime[0] = 1;  prime[1] = 1;
	TreeSet<Integer> tree = new TreeSet<Integer>();
	for(int i=2; i<Math.sqrt(32769); i++)
	    if(prime[i]==0)
		for(int j=i*i; j<32769; j+=i)
		    prime[j] = 1;
	int k = 0;
	for(int i=2; i<32768; i++)
	    if(prime[i]==0){
		map[k] = i;
		k++;
	    }
	try {
	    while(true){
		int count=0, n = Integer.valueOf(sc.readLine());
		if(n==0)
		    break;
		k = 0;
		while(map[k] != 0){
		    if(map[k]>n/2)
			break;
		    if(prime[n-map[k]]==0)
			count++;
		    k++;
		}
		System.out.println(count);
	    }
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
}
