import java.util.*;
import java.lang.*;
import java.math.*;
import java.io.*;
import static java.lang.Math.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;

public class Main{
	Scanner sc=new Scanner(System.in);

	long INF=1L<<60;
	double EPS=1e-12;

	int n;
	long[] as, bs;

	void run() throws Exception{
		n=sc.nextInt();
		as=new long[n];
		bs=new long[n];
		for(int i=0; i<n; i++){
			bs[i]=sc.nextLong();
			as[i]=sc.nextLong();
		}
		solve();
	}

	void solve() throws Exception{
		if(n==0){
			println("Yes 1");
			return;
		}
		int m=5;
		long[] ps=new long[5];
		ps[0]=BigInteger.valueOf((int)1e3).nextProbablePrime().longValue();
		for(int i=1; i<m; i++){
			ps[i]=BigInteger.valueOf(ps[i-1]+1).nextProbablePrime().longValue();
		}

		for(int x=1; x<=1000; x++){
			int mod1=(int)ps[0], mod2=(int)ps[1];
			if(f(x, mod1)==0&&df(x, mod1)==0&&f(x, mod2)==0&&df(x, mod2)==0){
				println("Yes "+x);
				return;
			}
		}
		// debug("ps", ps);

		long min=INF;
		long[] A=new long[m], B=new long[m];
		fill(A, 1);
		ArrayList<Integer> list1=candidate((int)ps[0]);
		ArrayList<Integer> list2=candidate((int)ps[1]);
		ArrayList<Integer> list3=candidate((int)ps[2]);
		ArrayList<Integer> list4=candidate((int)ps[3]);
		ArrayList<Integer> list5=candidate((int)ps[4]);
		
		/*
		long size=(long)list1.size()*list2.size()*list3.size()*list4.size()
				*list5.size();
		if(size>1e8){
			StringBuilder sb=new StringBuilder();
			sb.append(n).append('\n');
			for(int i=0; i<n&&i<8; i++){
				sb.append(as[i]).append(' ').append(bs[i]).append('\n');
			}
			sb.append(size).append('\n');
			throw new Exception(sb.toString());
			// return;
		}
		*/
		for(int x1 : list1){
			for(int x2 : list2){
				for(int x3 : list3){
					for(int x4 : list4){
						for(int x5 : list5){
							B[0]=x1;
							B[1]=x2;
							B[2]=x3;
							B[3]=x4;
							B[4]=x5;
							long res=linearCongruence(A, B, ps);
							if(res!=-1&&res>0){
								min=min(min, res);
							}
						}
					}
				}
			}
		}
		if(min<INF){
			println("Yes "+min);
		}else{
			println("No");
		}
		// println(""+(min<INF?min:-1));
	}

	long linearCongruence(long[] A, long[] B, long[] M){
		long x=0, m=1;
		for(int i=0; i<A.length; i++){
			long a=A[i]*m, b=B[i]-A[i]*x, d=gcd(M[i], a);
			if(b%d!=0)
				return -1;
			x+=m*(mulMod(b/d, invMod(a/d, M[i]/d), (M[i]/d)));
			m*=M[i]/d;
		}
		return x%m;
	}

	long gcd(long a, long b){
		return a==0?b:gcd(b%a, a);
	}

	long invMod(long a, long mod){
		return powMod(a, mod-2, mod);
	}

	ArrayList<Integer> candidate(int p){
		ArrayList<Integer> list=new ArrayList<Integer>();
		for(int i=0; i<p; i++){
			if(f(i, p)==0&&df(i, p)==0){
				list.add(i);
			}
		}
		return list;
	}

	long mulMod(long x, long y, long mod){
		if(y==0)
			return 0;
		if((y&1)==0)
			return mulMod((x<<1)%mod, y>>>1, mod);
		return (x+mulMod(x, y-1, mod))%mod;
	}

	long powMod(long x, long k, int mod){
		if(k==0){
			return 1%mod;
		}
		if(k%2==0){
			return powMod(x*x%mod, k>>>1, mod);
		}
		return x*powMod(x, k-1, mod)%mod;
	}

	long powMod(long x, long k, long mod){
		if(k==0){
			return 1%mod;
		}
		if(k%2==0){
			return powMod(mulMod(x, x, mod), k>>>1, mod);
		}
		return mulMod(x, powMod(x, k-1, mod), mod);
	}

	long f(long x, int mod){
		long val=0;
		for(int i=0; i<n; i++){
			val=(val+as[i]%mod*powMod(x, bs[i], mod)%mod)%mod;
		}
		return val;
	}

	long df(long x, int mod){
		long val=0;
		for(int i=0; i<n; i++){
			if(bs[i]>0){
				val=(val+bs[i]%mod*as[i]%mod*powMod(x, bs[i]-1, mod)%mod)%mod;
			}
		}
		return val;
	}

	long nextPrime(long n){
		for(n++; !BigInteger.valueOf(n).isProbablePrime(200); n++);
		return n;
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

	public static void main(String[] args) throws Exception{
		new Main().run();
	}
}
