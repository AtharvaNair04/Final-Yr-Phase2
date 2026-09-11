import java.util.*;
import java.io.*;

public class Main{
    static final Reader sc = new Reader();
    static final PrintWriter out = new PrintWriter(System.out,false);

    public static void main(String[] args) throws Exception {
    	
    	while(true){
    		int n = sc.nextInt();
    		int m = sc.nextInt();
    		int p = sc.nextInt();
    		if(n==0 && m==0 && p==0){
    			break;
    		}
    		int[][][] map = new int[n][n][n];
    		int[] dx = {1,1,0,0,0,1,1, 1, 1, 1, 1, 0, 1};
    		int[] dy = {0,0,0,1,1,1,1,-1,-1, 1,-1, 1, 0};
    		int[] dz = {0,1,1,1,0,0,1, 1,-1,-1, 0,-1,-1};
    		int[] x = new int[p];
    		int[] y = new int[p];
    		int ans = 0;
    		int count = 0;
    		for(int i=0;i<p;i++){
    			x[i] = sc.nextInt()-1;
    			y[i] = sc.nextInt()-1;
    		}
    		int now = 1;
    		boolean flag = false;
    		for(int i=0;i<p;i++){
    			for(int j=0;j<n;j++){
    				if(map[x[i]][y[i]][j]==0){
    					map[x[i]][y[i]][j] = now;
    					break;
    				}
    			}
    			for(int xx=0;xx<n;xx++){
    				for(int yy=0;yy<n;yy++){
    					for(int zz=0;zz<n;zz++){
    						if(map[xx][yy][zz]==0){
    							continue;
    						}
    						int w = map[xx][yy][zz];
    						for(int k=0;k<dx.length;k++){
    							boolean ok = true;
    							if(xx+(m-1)*dx[k]>=n || xx+(m-1)*dx[k]<0 ||
    								yy+(m-1)*dy[k]<0 || yy+(m-1)*dy[k]>=n || 
    								zz+(m-1)*dz[k]>=n || zz+(m-1)*dz[k]<0){
    								continue;
    							}
    							for(int l=1;l<m;l++){
    								if(w!=map[xx+l*dx[k]][yy+l*dy[k]][zz+l*dz[k]]){
    									ok = false;
    									break;
    								}
    							}
    							if(ok){
    								ans = now;
    								count = i+1;
    								break;
    							}
    						}
    						if(ans!=0){
    							break;
    						}
    					}
    					if(ans!=0){
    						break;
    					}
    				}
    				if(ans!=0){
    					break;
    				}
    			}
    			if(ans!=0){
    				break;
    			}
    			if(now==1){
    				now = 2;
    			}
    			else{
    				now = 1;
    			}
    		}
    		if(ans==0){
    			out.println("Draw");
    		}
    		else if(ans==1){
    			out.println("Black "+count);
    		}
    		else if(ans==2){
    			out.println("White "+count);
    		}
    		out.flush();
    	}

        sc.close();
        out.close();
    }

    static void trace(Object... o) { System.out.println(Arrays.deepToString(o));}
}

class Reader {

    private final InputStream in;
    private final byte[] buf = new byte[1024];
    private int ptr = 0;
    private int buflen = 0;

    public Reader() { this(System.in);}
    public Reader(InputStream source) { this.in = source;}

    private boolean hasNextByte() {
        if (ptr < buflen) return true;
        ptr = 0;
        try{
            buflen = in.read(buf);
        }catch (IOException e) {e.printStackTrace();}
        if (buflen <= 0) return false;
        return true;
    }

    private int readByte() { if (hasNextByte()) return buf[ptr++]; else return -1;}

    private boolean isPrintableChar(int c) { return 33 <= c && c <= 126;}

    private void skip() { while(hasNextByte() && !isPrintableChar(buf[ptr])) ptr++;}

    public boolean hasNext() {skip(); return hasNextByte();}

    public String next() {
        if (!hasNext()) throw new NoSuchElementException();
        StringBuilder sb = new StringBuilder();
        int b = readByte();
        while (isPrintableChar(b)) {
            sb.appendCodePoint(b);
            b = readByte();
        }
        return sb.toString();
    }

    public long nextLong() {
        if (!hasNext()) throw new NoSuchElementException();
        boolean minus = false;
        long num = readByte();

        if(num == '-'){
            num = 0;
            minus = true;
        }else if (num < '0' || '9' < num){
            throw new NumberFormatException();
        }else{
            num -= '0';
        }

        while(true){
            int b = readByte();
            if('0' <= b && b <= '9')
                num = num * 10 + (b - '0');
            else if(b == -1 || !isPrintableChar(b))
                return minus ? -num : num;
            else
                throw new NoSuchElementException();
        }
    }

    public int nextInt() {
        long num = nextLong();
        if (num < Integer.MIN_VALUE || Integer.MAX_VALUE < num)
            throw new NumberFormatException();
        return (int)num;
    }

    public double nextDouble() {
        return Double.parseDouble(next());
    }

    public char nextChar() {
        if (!hasNext()) throw new NoSuchElementException();
        return (char)readByte();
    }

    public String nextLine() {
        while (hasNextByte() && (buf[ptr] == '\n' || buf[ptr] == '\r')) ptr++;
        if (!hasNextByte()) throw new NoSuchElementException();

        StringBuilder sb = new StringBuilder();
        int b = readByte();
        while (b != '\n' && b != '\r') {
            sb.appendCodePoint(b);
            b = readByte();
        }

        return sb.toString();
    }

    public int[] nextIntArray(int n) {
        int[] res = new int[n];
        for (int i=0; i<n; i++) res[i] = nextInt();
        return res;
    }

    public char[] nextCharArray(int n) {
        char[] res = new char[n];
        for (int i=0; i<n; i++) res[i] = nextChar();
        return res;
    }

    public void close() {try{ in.close();}catch(IOException e){ e.printStackTrace();}};
}
