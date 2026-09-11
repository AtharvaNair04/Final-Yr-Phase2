import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Main {
    public static void main(String args[])
    {
        FastReader Reader=new FastReader(System.in);
        int N=Reader.nextInt();
        int arr[]=new int[N];
        for(int i=0;i<N;i++)
        {
            arr[i]=Reader.nextInt();
        }
        ArrayList<Integer> fall=new ArrayList<>();
        int flag=0;
        for(int i=0;i<N;i++)
        {
            if(i==0)
            {
                if(arr[i]!=0)
                {
                    flag = 1;
                    break;
                }

            }
            else
            {
                if(arr[i]-arr[i-1]>1) {
                    flag = 1;
                    break;
                }
                else if(arr[i]-arr[i-1]<0)
                {
                    fall.add(i);
                }
            }
        }
        long count=0;
        if(flag==1)
            System.out.println(-1);
        else
        {
            for(int i=fall.size()-1;i>=0;i--)
            {
                int start=fall.get(i);
                int end=0;
                if(i==fall.size()-1)
                    end=N-1;
                else
                    end=fall.get(i+1)-1;
                count+=(long)arr[start];
                for(int j=start+1;j<=end;j++)
                {
                    if(arr[j]!=arr[j-1])
                        count++;
                    else
                    {
                        count+=(long)arr[j];
                    }
                }

            }
            int start=0;
            int end=0;
            if(fall.size()==0)
                end=N-1;
            else
                end=fall.get(0)-1;
            count+=arr[start];
            for(int j=start+1;j<=end;j++)
            {
                if(arr[j]!=arr[j-1])
                    count++;
                else
                {
                    count+=(long)arr[j];
                }
            }
            System.out.println(count);
        }
    }
}

class FastReader {
    private InputStream stream;
    private byte[] buf = new byte[8192];
    private int curChar;
    private int pnumChars;
    private FastReader.SpaceCharFilter filter;

    public FastReader(InputStream stream) {
        this.stream = stream;
    }

    public int read() {
        if (pnumChars == -1) {
            throw new InputMismatchException();
        }
        if (curChar >= pnumChars) {
            curChar = 0;
            try {
                pnumChars = stream.read(buf);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
            if (pnumChars <= 0) {
                return -1;
            }
        }
        return buf[curChar++];
    }

    public String next() {
        return nextString();
    }

    public int nextInt() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        int res = 0;
        do {
            if (c == ',') {
                c = read();
            }
            if (c < '0' || c > '9') {
                throw new InputMismatchException();
            }
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    public long nextLong() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        long res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    public String nextString() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        StringBuilder res = new StringBuilder();
        do {
            res.appendCodePoint(c);
            c = read();
        } while (!isSpaceChar(c));
        return res.toString();
    }

    public String nextLine() {
        int c = read();
        while (isSpaceChar(c))
            c = read();
        StringBuilder res = new StringBuilder();
        do {
            res.appendCodePoint(c);
            c = read();
        } while (!isEndOfLine(c));
        return res.toString();
    }


    public boolean isEndOfLine(int c) {
        return c == '\n' || c == '\r' || c == -1;
    }

    public boolean isSpaceChar(int c) {
        if (filter != null) {
            return filter.isSpaceChar(c);
        }
        return isWhitespace(c);
    }

    public static boolean isWhitespace(int c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
    }

    public interface SpaceCharFilter {
        public boolean isSpaceChar(int ch);

    }

}
