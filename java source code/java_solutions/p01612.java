import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

public class Main{
	Scanner sc=new Scanner(System.in);

	int INF=1<<28;
	double EPS=1e-12;

	int n, m;
	int[] a, b;

	void run(){
		n=sc.nextInt();
		m=sc.nextInt();
		a=new int[m];
		b=new int[m];
		for(int i=0; i<m; i++){
			a[i]=sc.nextInt()-1;
			b[i]=sc.nextInt()-1;
		}
		solve();
	}

	@SuppressWarnings("unchecked")
	void solve(){
		ArrayList<Integer>[] es=new ArrayList[n];
		ArrayList<Integer>[] rs=new ArrayList[n];
		for(int i=0; i<n; i++){
			es[i]=new ArrayList<Integer>();
			rs[i]=new ArrayList<Integer>();
		}
		for(int i=0; i<m; i++){
			es[a[i]].add(b[i]);
			rs[b[i]].add(a[i]);
		}
		int[] d1=new int[n];
		int[] d2=new int[n];
		for(int i=0; i<n; i++){
			for(int j : es[i]){
				d1[j]=max(d1[j], d1[i]+1);
			}
		}
		for(int i=n-1; i>=0; i--){
			for(int j : rs[i]){
				d2[j]=max(d2[j], d2[i]+1);
			}
		}
		// debug("d1", d1);
		// debug("d2", d2);
		int maxChain=0;
		for(int i=0; i<n; i++){
			maxChain=max(maxChain, d1[i]+d2[i]);
		}
		// debug("maxChain", maxChain);
		int[] ind=new int[n];
		for(int i=0; i<m; i++){
			if(d1[a[i]]+1+d2[b[i]]==maxChain){
				// 候補
				if(ind[d1[a[i]]]==0){
					ind[d1[a[i]]]=i+1;
				}else{
					ind[d1[a[i]]]=-1;
				}
			}
		}
//		debug(ind);
		sort(ind);
		int count=0;
		for(int i : ind){
			if(i>0){
				println(i+"");
				count++;
			}
		}
		if(count==0){
			println("-1");
		}
	}

	void debug(Object... os){
		System.err.println(Arrays.deepToString(os));
	}

	void print(String s){
		System.out.print(s);
	}

	void println(String s){
		System.out.println(s);
	}

	public static void main(String[] args){
		new Main().run();
	}
}
