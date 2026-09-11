import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Main {
	
	final boolean isDebug = false;
	String fileName = "input.txt";
	FastScanner sc;
	//PrintWriter out;
	final int MOD = 1000000007;
	
	/* solve */
	void solve(){
		int N = sc.nextInt();
		int[] A = new int[N+1];
		long[] sum = new long[N+1];
		for(int i = 1; i <= N; i++){
			A[i] = sc.nextInt();
			sum[i] = sum[i-1] + A[i];
		}
		
		long ans = Long.MAX_VALUE;
		int left = 1, right = 3;
		long minLeft = Long.MAX_VALUE, minRight = Long.MAX_VALUE;
		for(int mid = 3; mid <= N - 1; mid++){
			if(right == mid) right++;
			
			minLeft = Math.abs((sum[left-1]-sum[0]) - (sum[mid-1]-sum[left-1]));
			while(left+1 <= mid && Math.abs((sum[(left+1)-1]-sum[0]) - (sum[mid-1]-sum[(left+1)-1])) < minLeft){
				left++;
				minLeft = Math.abs((sum[left-1]-sum[0]) - (sum[mid-1]-sum[left-1]));
			}
			long P = sum[left-1]-sum[0];
			long Q = sum[mid-1]-sum[left-1];
			
			minRight = Math.abs((sum[right-1]-sum[mid-1]) - (sum[N]-sum[right-1]));
			while(right+1 <= N && Math.abs((sum[(right+1)-1]-sum[mid-1]) - (sum[N]-sum[(right+1)-1])) < minRight){
				right++;
				minRight = Math.abs((sum[right-1]-sum[mid-1]) - (sum[N]-sum[right-1]));
			}
			long R = sum[right-1]-sum[mid-1];
			long S = sum[N]-sum[right-1];
			
			//System.out.println("mid = " + mid + " : " + P + " " + Q + " " + R + " " + S);
			
			long max = Math.max(Math.max(P, Q), Math.max(R, S));
			long min = Math.min(Math.min(P, Q), Math.min(R, S));
			ans = Math.min(ans, Math.abs(max - min));
		}
		
		System.out.println(ans);
	}
	/* end solve */
	
	/* main */
	public static void main(String[] args) throws IOException {
		new Main().m();
	}
	
	void m() throws IOException {
		long S = System.currentTimeMillis();
		sc = (isDebug) ? new FastScanner(new FileInputStream(fileName)) : new FastScanner(System.in);
		//out = new PrintWriter(System.out);
		solve();
		//out.flush();
		long G = System.currentTimeMillis();
		if(isDebug){
			System.out.println("---Debug---");
			System.out.printf("%7d ms", (G-S));
		}
	}
	/* end main */
}
/* end Main */

class FastScanner {
    private InputStream in;
    private final byte[] buffer = new byte[1024];
    private int ptr = 0;
    private int buflen = 0;
    public FastScanner(InputStream in) {
		this.in = in;
	}
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
    private int readByte() {
    	if (hasNextByte()) return buffer[ptr++];
    	else return -1;
    }
    private static boolean isPrintableChar(int c){
    	return 33 <= c && c <= 126;
    }
    public boolean hasNext() {
    	while(hasNextByte() && !isPrintableChar(buffer[ptr]))
    		ptr++; return hasNextByte();
    }
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
    public double nextDouble() {
    	return Double.parseDouble(next());
    }
}
