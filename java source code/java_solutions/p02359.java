import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
 
 
public class Main {
    public void exec() throws IOException {
        InputUtil input = new InputUtil();
        PrintWriter out = new PrintWriter(System.out);
        int N = input.nextInt();
        int T = input.nextInt();
        int[] diffTable= new int[T+1];
        
        for(int i=0; i<N; i++){
        	int l = input.nextInt();
        	int r = input.nextInt();
        	diffTable[l] ++;
        	diffTable[r] --;
        }
        int max=0;
        int count=0;
        for(int i=0; i<T; i++) {
        	count += diffTable[i];
        	max=Math.max(max, count);
        }
        out.println(max);
        out.flush();
        input.close();
    }
     
 
    public static void main(String[] args) throws IOException {
//    	long s = System.currentTimeMillis();
    	new Main().exec();
//    	System.out.println("msec:"+(System.currentTimeMillis()-s));
//    	new Main().test();
    }
//    
//    public void test() {
//        PrintWriter out = new PrintWriter(System.out);
//        Random rd = new Random();
//        int N = 100000;
//        int T = 100000;
//        out.printf("%d %d\n", N, T);
//        for (int i=1; i<=N; i++) {
////        	int st = rd.nextInt(T-1);
////        	int ed = rd.nextInt(T-1-st)+st+1;
////            out.printf("%d %d\n", st, ed);
//        	out.printf("%d %d\n", 0, 100000);
//        }
//        out.flush();
//    }
}
 
/**
 * --------------------------------------------------
 * Input Utility
 * --------------------------------------------------
 */
class InputUtil {
    //Static Fields
    private static final int BUFSIZ = 2100005;
     
    //Fields
    protected InputStream in;
    private char[] buf = null;
    private int ptr = 0;
    private int max = -1;
    private BufferedReader br = null;
     
    //Constructors
    public InputUtil() {this(System.in,BUFSIZ);}
    public InputUtil(int bufsiz) {this(System.in,bufsiz);}
    public InputUtil(InputStream in) {this(in,BUFSIZ);}
    public InputUtil(InputStream in, int bufsiz) {
        initBuf(bufsiz);
        initIn(in);
    }
     
    //Methods
    //Initializer
    protected void initIn(InputStream in) {br = new BufferedReader(new InputStreamReader(this.in = in));}
    protected void initBuf(int bufsiz) {buf = new char[bufsiz];}
     
    //String Input Reader/Parser
    private void readBuf() throws IOException {
        if(ptr >= max && br.ready()) {
            max = br.read(buf);
            ptr = 0;
        }
    }
     
    public BufferedReader getReader() {return br;}
     
    //Number Input Reader/Parser
    private boolean isNum(char b) {return '0' <= b && b <= '9'; }
    private boolean isMinus(char b) {return b=='-'; }
    private void next() throws IOException {ptr++; readBuf();}
 
 
    public int nextInt() throws IOException {return nextInt(new int[1])[0];}
    public int[] nextInt(int[] result) throws IOException {
        readBuf();
        for(int i=0; i<result.length; i++) {
            int sign = 1;
            int res = 0;
            char c;
            while (!isNum(c=buf[ptr]) && !isMinus(c)) 
                next();
            if (isMinus(buf[ptr])) {
                sign = -1;
                next();
            }
            while (isNum(c=buf[ptr])) {
                res *= 10;
                res += c - '0' ;
                next();
            }
            next();
            result[i] = sign * res;
        }
        return result;
    }
     
    //Close 
    public void close() throws IOException {
        if(!this.in.equals(System.in)) 
            this.in.close();
    }
 
}
