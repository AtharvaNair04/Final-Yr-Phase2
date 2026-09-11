import java.io.InputStreamReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        QuickScanner in = new QuickScanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        TaskB solver = new TaskB();
        solver.solve(in, out);
        out.close();
    }
}

class Task {
    public void solve(QuickScanner in, PrintWriter out) {
        int D = in.nextInt();
        int T = in.nextInt();
        int S = in.nextInt();
        double time = (double)D/(double)T;
        if(T*S >= D) {
        	out.print("Yes");        	
        }else {
        	out.print("No");
        }
    }
}
class TaskB {
    public void solve(QuickScanner in, PrintWriter out) {
        char[] S = in.next().toCharArray();
        char[] T = in.next().toCharArray();
        int ans = T.length;
        for(int start = 0; start <= S.length - T.length; start++) {
        	int diff = 0;
        	for(int i = 0; i < T.length; i++) {
        		if(T[i] != S[start + i]) {
        			diff++;
        		}
        	}
        	ans = Math.min(ans,diff);
        }
        out.print(ans);
    }
}


class QuickScanner {
    BufferedReader in;
    StringTokenizer token;
    String delim;
    public QuickScanner(InputStream inputStream)
    {
        this.in=new BufferedReader(new InputStreamReader(inputStream));
        this.delim=" \n\t";
        this.token=new StringTokenizer("",delim);
    }

    public boolean hasNext()
    {
        while(!token.hasMoreTokens())
        {
            try {
                String s=in.readLine();
                if(s==null) return false;
                token=new StringTokenizer(s,delim);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
        }
        return true;
    }

    public String next()
    {
        hasNext();
        return token.nextToken();
    }

    public int nextInt()
    {
        return Integer.parseInt(next());
    }

}
