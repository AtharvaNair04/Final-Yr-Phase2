import java.io.*;
import java.util.*;

class Main  {
    static int mod = 1000000007;
    static int mx = 2000007;
    static int[] fact, ifact;
    
    public static void main(String args[]) throws IOException {
        fact = new int[mx];
        ifact = new int[mx];
        fact[1] = 1;
        ifact[1] = 1;
        
        for(int i = 2; i < mx; ++i){
            fact[i] = modMul(fact[i-1], i);
            ifact[i] = modMul(ifact[i-1], modExp(i, mod-2));
        }
        
        Scan sc = new Scan();
        int tr1 = sc.scanInt();
        int tc1 = sc.scanInt();
        int tr2 = sc.scanInt();
        int tc2 = sc.scanInt();
        
        int r1 = Math.min(tr1, tr2);
        int c1 = Math.min(tc1, tc2);
        
        int r2 = tr1+tr2-r1;
        int c2 = tc1+tc2-c1;
            
        int part1 = modAdd(comb(r1 + c1, c1), comb(r2 + c2 + 2, c2 + 1), true);
        int part2 = modAdd(comb(r1 + c2 + 1, c2 + 1), comb(r2 + c1 + 1, c1), true);
        int res = modAdd(part1, part2, false);
        if(res < 0)
            res += mod;
        System.out.println(res);
    }
    
    static int comb(int n, int r){
        return modMul(fact[n], modMul(ifact[r], ifact[n-r]));
    }
    
    static int modAdd(int a, int b, boolean add){
        return add ? (int)(((long)a + b)%mod) : (int)(((long)a - b)%mod);    
    }
    
    static int modMul(int a, int b){
        return (int)(((long)a*b)%mod);
    }
    
    static int modExp(int a, int b){
        int res = 1;
        while(b > 0){
            if(b%2==1){
                res = modMul(res, a);
            }
            a = modMul(a, a);
            b>>=1;
        }
        return res;
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
