/*import java.util.*;
import java.io.*;
import java.lang.*;
public class test{
	static double res=0;
	public static void help(int p,int r,int n){
		double tmp=p;
		for(int i=1;i<=n;i++){
			double x=1+(double)r/1200 ;
			tmp*=x;
		}
		res+=tmp;
		int k=100000,m=360;
		for(int i=m;i>0;i--){
			help(k,5,i);
		}
		System.out.println("paid amount "+k*m+" ");
		System.out.println((long)res);
	}
	public static void main(String[] args){
		Scanner s=new Scanner(System.in);
	    int n=s.nextInt();
		int[] arr=new int[n];
		for(int i=0;i<n;i++){
			arr[i]=s.nextInt();
		}
		long[] dp1=new long[n];
		long[] dp2=new long[n];
		if(arr[0]>0){dp1[0]=1;}
		else{dp2[0]=1;}
		long pos=dp1[0],neg=dp2[0];
		for(int i=1;i<n;i++){
			if(arr[i]>0){
				dp1[i]=dp1[i-1]+1;
				dp2[i]=dp2[i-1];
			}
			else{
				dp1[i]=dp2[i-1];
				dp2[i]=dp1[i-1]+1;
			}
			pos+=dp1[i];
			neg+=dp2[i];
		}
		System.out.println(neg+" "+pos);
	}
}			
	

*/
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskB solver = new TaskB();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskB {
        static final long MODULO = 998244353;

        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n = in.nextInt();
            String s = in.next();
            int[][] pos = new int[3][n];
            int[] cnt = new int[3];
            for (int i = 0; i < s.length(); ++i) {
                int what = "RGB".indexOf(s.charAt(i));
                pos[what][cnt[what]++] = i;
            }
            int[] val = new int[s.length()];
            int[] minusOneType = new int[s.length()];
            Arrays.fill(minusOneType, -1);
            Arrays.fill(val, -2);
            Arrays.fill(cnt, 0);
            for (int i = 0; i < s.length(); ++i)
                if (val[i] == -2) {
                    val[i] = -1;
                    int what = "RGB".indexOf(s.charAt(i));
                    int p1 = -1;
                    int p2 = -1;
                    for (int j = 0; j < 3; ++j)
                        if (j != what) {
                            while (val[pos[j][cnt[j]]] != -2) ++cnt[j];
                            if (p1 < 0) p1 = pos[j][cnt[j]];
                            else p2 = pos[j][cnt[j]];
                        }
                    val[Math.min(p1, p2)] = 0;
                    minusOneType[Math.min(p1, p2)] = what;
                    val[Math.max(p1, p2)] = 1;
                }
            long res = 1;
            for (int i = 1; i <= n; ++i) {
                res = res * i % MODULO;
            }
            Arrays.fill(cnt, 0);
            for (int i = 0; i < s.length(); ++i) {
                int what = "RGB".indexOf(s.charAt(i));
                if (val[i] == -1) ++cnt[what];
                if (val[i] == 0) {
                    int need = minusOneType[i];
                    res = res * cnt[need]-- % MODULO;
                }
            }
            Arrays.fill(cnt, 0);
            for (int i = s.length() - 1; i >= 0; --i) {
                int what = "RGB".indexOf(s.charAt(i));
                if (val[i] == 1) ++cnt[what];
                if (val[i] == 0) {
                    int need = 3 - what - minusOneType[i];
                    res = res * cnt[need]-- % MODULO;
                }
            }
            out.println(res);
        }

    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}
