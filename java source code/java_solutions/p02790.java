import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Main {
static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
static PrintWriter pw =new PrintWriter(new OutputStreamWriter(System.out));
static StringTokenizer st;
static BitSet bs;
static int TC,N,M,a,b,n;
static int [] values;
    public static void main(String[] args) throws IOException {
        // write your code here
        st= new StringTokenizer(br.readLine());
        a = Integer.parseInt(st.nextToken());
        b = Integer.parseInt(st.nextToken());
        String sum = "";
        String sumA = "";
        for (int i=0;i<a;i++)
            sum+=b;
        for (int j=0;j<b;j++)
            sumA+=a;
        pw.println(sum.compareTo(sumA)<0?sum:sumA);pw.flush();


    }

    private static boolean condition(int A, int B) {
        int firstDigit = firstDigit(B);
        int firstDigitA = firstDigit(A);
        return (A%10)==firstDigit&&firstDigitA==B%10;
    }
    static int firstDigit(int n)
    {

        int digits = (int)(Math.log10(n));
        n = (int)(n / (int)(Math.pow(10, digits)));
        return n;
    }
}
