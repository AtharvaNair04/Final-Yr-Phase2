import java.util.*;
import java.io.*;
 
 
public class Main {
    public static void main(String[] args) throws Exception {
        FastScanner sc = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);
        
        int BIG_NUMBER = 100000000;
        long LARGE_NUMBER = 10000000000000000L;
        
        int n=sc.nextInt();
        
        long[] an = new long[n];
	   	for(int i=0; i<n; i++){
	   	    an[i] = sc.nextLong();
	   	}
	   	
	   	long[] kisum = new long[n]; //奇数、偶数番目の累積和
	   	kisum[0] = an[0];
	   	kisum[1] = an[1];
	   	for(int i=2; i<n; i++){
	   	    kisum[i] = kisum[i-2]+an[i];
	   	}
        
        long ans=-LARGE_NUMBER;
        if(n%2==0){
            ans = maxL(kisum[n-2], kisum[n-1]);
            int gap=1;
	   	    while(gap<(n-1)){
	   	        ans = maxL(ans, kisum[gap-1]+kisum[n-1]-kisum[gap]);
	   	        gap+=2;
	   	    }
        }else{
            ans = maxL(kisum[n-3], kisum[n-2]);
            ans = maxL(ans, kisum[n-1]-an[0]);
            int gap=1;
	   	    while(gap<(n-1)){
	   	        ans = maxL(ans, kisum[gap-1]+kisum[n-2]-kisum[gap]);
	   	        gap+=2;
	   	    }
	   	    gap=2;
	   	    while(gap<(n-1)){
	   	        ans = maxL(ans, kisum[gap-1]+kisum[n-1]-kisum[gap]);
	   	        gap+=2;
	   	    }
	   	    gap=1;
	   	    while(gap<(n-2)){
	   	        ans = maxL(ans, kisum[gap-1]+kisum[n-1]-kisum[gap+1]);
	   	        gap+=2;
	   	    }
	   	    
	   	    if(n>6){
    	   	    long[] dp = new long[n];    //i個め以降からx-x-x-x||x-x-x形式で取り出す時の最大値
    	   	    
    	   	    dp[n-4] = an[n-4]+an[n-1];
            	for(int i=(n-6); i>0; i--){
            	    dp[i] = maxL(dp[i+2]+an[i], an[i]+kisum[n-1]-kisum[i+1]);
           		}
           		
    	   	    gap=1;
    	   	    while(gap<(n-4)){
    	   	        ans = maxL(ans, kisum[gap-1]+dp[gap+2]);
    	   	        gap+=2;
    	   	    }
	   	    }
        }
        out.println(ans);
        out.flush();
    }
    
    private static long gcd(long m, long n) {
        if(m < n) return gcd(n, m);
        if(n == 0) return m;
        return gcd(n, m % n);
    }
    
    private static int pow2(int in){
        return in*in;
    }
    private static long pow2(long in){
        return in*in;
    }
    
    private static int minExcept(int[] dt, int ex, char mode){
        int min=Integer.MAX_VALUE;
        
        if(mode =='a'){
        	for(int i=0; i<dt.length; i++){
        	    if((i != ex) && (dt[i] < min)) min = dt[i];
        	}
        }else if(mode =='v'){
        	for(int i=0; i<dt.length; i++){
        	    if((dt[i] != ex) && (dt[i] < min)) min = dt[i];
        	}
        }
    	return min;
    }
    private static long minExcept(long[] dt, int ex, char mode){
        long min=Long.MAX_VALUE;
        
        if(mode =='a'){
        	for(int i=0; i<dt.length; i++){
        	    if((i != ex) && (dt[i] < min)) min = dt[i];
        	}
        }else if(mode =='v'){
        	for(int i=0; i<dt.length; i++){
        	    if((dt[i] != ex) && (dt[i] < min)) min = dt[i];
        	}
        }
    	return min;
    }
    
    private static int abs(int a){
        if(a>=0) return a;
        else return -a;
    }
    private static long absL(long a){
        if(a>=0) return a;
        else return -a;
    }
    private static int min(int a, int b){
        if(a>b) return b;
        else return a;
    }
    private static long minL(long a, long b){
        if(a>b) return b;
        else return a;
    }
    private static int min2(int... ins){
        int min = ins[0];
        
    	for(int i=1; i<ins.length; i++){
    	    if(ins[i] < min) min = ins[i];
    	}
    	return min;
    }
    private static int max(int a, int b){
        if(a>b) return a;
        else return b;
    }
    private static long maxL(long a, long b){
        if(a>b) return a;
        else return b;
    }
    private static int max2(int... ins){
        int max = ins[0];
        
    	for(int i=1; i<ins.length; i++){
    	    if(ins[i] > max) max = ins[i];
    	}
    	return max;
    }
    
    
    static void show_i2d_array(long[][] dt){
        PrintWriter out = new PrintWriter(System.out);
    	for(int i=0; i<dt[0].length; i++){
    		for(int j=0; j<dt.length; j++){
    		    out.print(dt[j][i]+",");
    		}
    		out.println("<-city="+i);
    	}
    	out.flush();
    }
    
    public static void combSort(int[][] origin, int ln){
        int h=ln*10/13, tmp=0;
        boolean fin=true;
        while((h>1)||(!fin)){
            fin=true;
            for(int i=0; i < ln-h; i++){
                if(origin[i][0] > origin[i+h][0]){
                    tmp=origin[i][0];
                    origin[i][0]=origin[i+h][0];
                    origin[i+h][0]=tmp;
                    tmp=origin[i][1];
                    origin[i][1]=origin[i+h][1];
                    origin[i+h][1]=tmp;
                    fin=false;
                }
            }
            h *=10;
            h/=13;
        }
    }
}
 
class FastScanner {
    private final InputStream in = System.in;
    private final byte[] buffer = new byte[1024];
    private int ptr = 0;
    private int buflen = 0;
    private boolean hasNextByte() {
        if (ptr < buflen) {
            return true;
        }else{
            ptr = 0;
            try {
                buflen = in.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (buflen <= 0) {
                return false;
            }
        }
        return true;
    }
    
    private int readByte() { if (hasNextByte()) return buffer[ptr++]; else return -1;}
    private static boolean isPrintableChar(int c) { return 33 <= c && c <= 126;}
    public boolean hasNext() { while(hasNextByte() && !isPrintableChar(buffer[ptr])) ptr++; return hasNextByte();}
    public String next() {
        if (!hasNext()) throw new NoSuchElementException();
        StringBuilder sb = new StringBuilder();
        int b = readByte();
        while(isPrintableChar(b)) {
            sb.appendCodePoint(b);
            b = readByte();
        }
        return sb.toString();
    }
    public long nextLong() {
        if (!hasNext()) throw new NoSuchElementException();
        long n = 0;
        boolean minus = false;
        int b = readByte();
        if (b == '-') {
            minus = true;
            b = readByte();
        }
        if (b < '0' || '9' < b) {
            throw new NumberFormatException();
        }
        while(true){
            if ('0' <= b && b <= '9') {
                n *= 10;
                n += b - '0';
            }else if(b == -1 || !isPrintableChar(b)){
                return minus ? -n : n;
            }else{
                throw new NumberFormatException();
            }
            b = readByte();
        }
    }
    public int nextInt() {
        long nl = nextLong();
        if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE) throw new NumberFormatException();
        return (int) nl;
    }
    public double nextDouble() { return Double.parseDouble(next());}
}
