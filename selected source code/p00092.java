import java.io.*;
import java.util.*;

class Main{
    public static void main(String[] args){
	BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
	try{
	    while(true){
		int n = Integer.valueOf(sc.readLine());
		if(n==0)
		    break;
		int[][] dp = new int[n][n];
		int max = 0;
		for(int j=0; j<n; j++){
		    String sp = sc.readLine();
		    for(int i=0; i<n; i++)
			if(sp.charAt(i)=='*')
			    dp[j][i] = 0;
			else
			    if(j==0||i==0)
				dp[j][i]=1;
			    else{
				dp[j][i]= Math.min(Math.min(dp[j][i-1], dp[j-1][i]), dp[j-1][i-1])+1;
				max = Math.max(max, dp[j][i]);
			    }
		}
		System.out.println(max);
	    }
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
}
