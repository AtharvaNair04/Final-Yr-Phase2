import java.io.*;
import java.util.*;

class Main{
    static int num = 0, n, sum = 0;
    static LinkedList<Integer> index;
    static int[] box;
    public static void main(String[] args){
	BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
	try {
	    n = Integer.valueOf(sc.readLine());
	    String inp = new StringBuffer(sc.readLine()).reverse().toString();
	    int m = Integer.valueOf(sc.readLine());
	    String[] st = sc.readLine().split(" ");
	    box = new int[m];
	    for(int i=0; i<m; i++)
		box[i] = Integer.valueOf(st[i]);
	    int start = -1;
	    index = new LinkedList<Integer>();
	    for(int i=0; i<n; i++)
		if(inp.charAt(i)=='*')
		    index.add(i);
		else
		    if(i%2==0)
			sum+=(int)(inp.charAt(i)-'0');
		    else{
			int t = 2*(int)(inp.charAt(i)-'0');
			sum+=t/10+t%10;
		    }
	    solve();
	    System.out.println(num);
	}catch(Exception e){
	    System.out.println("Error");
	}
    }
    static void solve(){
	if(index.size()==0){
	    if(sum%10==0)
		num++;
	    return;
	}
	
	for(int u: box){
	    int zz, t = index.poll();
	    if(t%2==0)
		zz = u;
	    else
		zz = (2*u)/10+(2*u)%10;
	    sum+=zz;
	    solve();
	    sum-=zz;
	    index.offerFirst(t);
	}
    }
}
