import java.io.*;
import java.util.*;

class Main {
	public static void main(String[] args) throws IOException {
		Scan sc = new Scan();
		int N = sc.scanInt();
		int K = sc.scanInt();
		SolABC162_e sol = new SolABC162_e(K);
		sol.calcMu();
		sol.calcGCDTuple(N);
	}
}

class SolABC162_e {
	int K, mod = 1000000007;
	int[] mu, prime;

	public SolABC162_e(int K) {
		this.K = K;
		mu = new int[K + 1];
		prime = new int[K + 1];
		for (int i = 1; i <= K; ++i) {
			mu[i] = 1;
			prime[i] = 1;
		}
	}

	void calcMu() {
		for (int i = 2; i <= K; ++i) {
			if (prime[i] == 1) {
				for (int j = i; j <= K; j += i) {
					if (j > i)
						prime[j] = 0;
					if (j % ((long) i * i) == 0)
						mu[j] = 0;
					mu[j] = -mu[j];
				}
			}
		}
	}

	long modPow(long base, long exp) {
		if (exp == 0)
			return 1;
		long res = 1;
		long runner = base;
		while (exp > 0) {
			if ((exp & 1) == 1) {
				res = (res * runner) % mod;
			}
			runner = (runner * runner) % mod;
			exp >>= 1;
		}
		return res;
	}

	void calcGCDTuple(int cnt) {
		long res = 0;
		for (long d = 1; d <= K; ++d) {
			long tmp = 0;
			for (long s = 1; s <= K / d; ++s) {
				tmp += (mu[(int) s] * modPow(K / ((long) d * s), cnt));
			}
			res += tmp * d;
			res %= mod;
		}
		System.out.println(res % mod);
	}
}

class Scan {
    private byte[] buf=new byte[1024];
    private int index;
    private InputStream in;
    private int total;
	
    public Scan()
    {
        in=System.in;
    }
    public int scan()throws IOException
    {
        if(total<0)
        throw new InputMismatchException();
        if(index>=total)
        {
            index=0;
            total=in.read(buf);
            if(total<=0)
            return -1;
        }
        return buf[index++];
    }
    public int scanInt()throws IOException
    {
        int integer=0;
        int n=scan();
        while(isWhiteSpace(n))
        n=scan();
        int neg=1;
        if(n=='-')
        {
            neg=-1;
            n=scan();
        }
        while(!isWhiteSpace(n))
        {
            if(n>='0'&&n<='9')
            {
                integer*=10;
                integer+=n-'0';
                n=scan();
				
            }
            else throw new InputMismatchException();
        }
        return neg*integer;
    }
	public long scanLong()throws IOException
    {
        long integer=0;
        int n=scan();
        while(isWhiteSpace(n))
        n=scan();
        int neg=1;
        if(n=='-')
        {
            neg=-1;
            n=scan();
        }
        while(!isWhiteSpace(n))
        {
            if(n>='0'&&n<='9')
            {
                integer*=10;
                integer+=n-'0';
                n=scan();
            }
            else throw new InputMismatchException();
        }
        return neg*integer;
    }
    public double scanDouble()throws IOException
    {
        double doub=0;
        int n=scan();
        while(isWhiteSpace(n))
        n=scan();
        int neg=1;
        if(n=='-')
        {
            neg=-1;
            n=scan();
        }
        while(!isWhiteSpace(n)&&n!='.')
        {
            if(n>='0'&&n<='9')
            {
                doub*=10;
                doub+=n-'0';
                n=scan();
            }
            else throw new InputMismatchException();
        }
        if(n=='.')
        {
            n=scan();
            double temp=1;
            while(!isWhiteSpace(n))
            {
                if(n>='0'&&n<='9')
                {
                    temp/=10;
                    doub+=(n-'0')*temp;
                    n=scan();
                }
                else throw new InputMismatchException();
            }
        }
        return doub*neg;
    }
    public String scanString()throws IOException
    {
        StringBuilder sb=new StringBuilder();
        int n=scan();
        while(isWhiteSpace(n))
        n=scan();
        while(!isWhiteSpace(n))
        {
            sb.append((char)n);
            n=scan();
        }
        return sb.toString();
    }
    private boolean isWhiteSpace(int n)
    {
        if(n==' '||n=='\n'||n=='\r'||n=='\t'||n==-1)
        return true;
        return false;
    }
}
