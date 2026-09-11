import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;
import java.math.BigInteger;
 
public class Main implements Runnable {
	
	static int mod = 1000000007;
	
    public static void main(String[] args) {
    	new Thread(null, new Main(), "", 1024 * 1024 * 1024).start();
    }
    
    public void run() {
    	PrintWriter out = new PrintWriter(System.out);
        FastScanner sc = new FastScanner();
        
        int n = sc.nextInt();
        int k = sc.nextInt();
        int s = 2*k;
        
        int[][] im = new int[2*s+1][2*s+1];
        
        for(int i=0;i<n;i++){
        	int x = sc.nextInt();
        	int y = sc.nextInt();
        	String z = sc.next();
        	if(!z.equals("B")){
        		y += k;
        	}
        	x %= s;
        	y %= s;
        	
        	im[x][y]++;
        	im[x+k][y]--;
        	im[x][y+k]--;
        	im[x+k][y+k]++;
        	im[x+k][y+k]++;
        	im[x+k][y+k+k]--;
        	im[x+k+k][y+k]--;
        	im[x+k+k][y+k+k]++;
        	
        }
        
        cumulativeSum(im);
        
        for(int i=0;i<2*s+1;i++){
        	for(int j=0;j<2*s+1;j++){
        		if(i < s && j < s){
        			continue;
        		}
				im[i%s][j%s] += im[i][j];
        	}
        }
        
        int ans = max(im);
        
        out.println(ans);
        
        out.flush();
    }
    
	static void print(int[][] x){
		for(int i=0;i<x.length;i++){
			for(int j=0;j<x[0].length;j++){
				System.out.print(x[i][j] + ",	");
			}
			System.out.println();
		}
	}
    
	static void cumulativeSum(int[][] x){

		for(int i=0;i<x.length;i++){
			for(int j=1;j<x[0].length;j++){
				x[i][j] += x[i][j-1];
			}
		}
		
		for(int j=0;j<x[0].length;j++){
			for(int i=1;i<x.length;i++){
				x[i][j] += x[i-1][j];
			}
		}
		
	}
	static int max(int[][] x){
		int max = Integer.MIN_VALUE;
		for(int i=0;i<x.length;i++){
			for(int j=0;j<x[0].length;j++){
				if(x[i][j] > max){
					max = x[i][j];
				}
			}
		}
		return max;
	}
	
	static int mod(int a, int mod) {
		return (a % mod + mod) % mod;
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
		} else {
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
	private int readByte() {
		if (hasNextByte())
			return buffer[ptr++];
		else
			return -1;
	}
	private static boolean isPrintableChar(int c) {
		return 33 <= c && c <= 126;
	}
	public boolean hasNext() {
		while (hasNextByte() && !isPrintableChar(buffer[ptr]))
			ptr++;
		return hasNextByte();
	}
	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();
		StringBuilder sb = new StringBuilder();
		int b = readByte();
		while (isPrintableChar(b)) {
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	public long nextLong() {
		if (!hasNext())
			throw new NoSuchElementException();
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
		while (true) {
			if ('0' <= b && b <= '9') {
				n *= 10;
				n += b - '0';
			} else if (b == -1 || !isPrintableChar(b)) {
				return minus ? -n : n;
			} else {
				throw new NumberFormatException();
			}
			b = readByte();
		}
	}
	public int nextInt() {
		long nl = nextLong();
		if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE)
			throw new NumberFormatException();
		return (int) nl;
	}
	public int[] nextintArray(int n){
		int[] a = new int[n];
		for(int i=0;i<n;i++){
			a[i] = nextInt();
		}
		return a;
	}
	public long[] nextlongArray(int n){
		long[] a = new long[n];
		for(int i=0;i<n;i++){
			a[i] = nextLong();
		}
		return a;
	}
	public Integer[] nextIntegerArray(int n){
		Integer[] a = new Integer[n];
		for(int i=0;i<n;i++){
			a[i] = nextInt();
		}
		return a;
	}
	public double nextDouble() {
		return Double.parseDouble(next());
	}
}
