import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

// Riffle Swap
// 2012/12/15
public class Main{
	Scanner sc=new Scanner(System.in);

	int n;
	int mod=1000003;
	long[] memo;

	void run(){
		n=sc.nextInt();
		solve();
	}

	void solve(){
		memo=new long[n+1];
		fill(memo, -1);
		rec(n);
		long sum=0;
		for(int i=1; i<=n; i++){
			if(memo[i]>0){
				sum=(sum+memo[i]*(i-1)%mod)%mod;
			}
		}
		println(sum+"");
	}

	long rec(int n){
		if(memo[n]>=0){
			return memo[n];
		}
		if(n==1){
			return memo[1]=2;
		}
		long res=powMod(2, n, mod)-2;
		for(int i=2; i*i<=n; i++){
			if(n%i==0){
				res=(res-rec(i)*i%mod+mod)%mod;
				if(n/i!=i){
					res=(res-rec(n/i)*n/i%mod+mod)%mod;
				}
			}
		}
		return memo[n]=res*powMod(n, mod-2, mod)%mod;
	}

	long powMod(long x, int k, int mod){
		if(k==0){
			return 1;
		}else if((k&1)==0){
			return powMod(x*x%mod, k>>>1, mod);
		}else{
			return x*powMod(x, k-1, mod)%mod;
		}
	}

	void println(String s){
		System.out.println(s);
	}

	public static void main(String[] args){
		new Main().run();
	}
}
