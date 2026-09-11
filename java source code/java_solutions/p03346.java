import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;

public class Main{
    public static void main(String args[])
    {
        FastReader Reader=new FastReader(System.in);
        int N=Reader.nextInt();
        ArrayList<Integer> arr= new ArrayList<>();
        for(int i=0;i<N;i++)
        {
            int x=Reader.nextInt();
            arr.add(x);
        }
        int count=0;
        /*if(arr.get(0)>arr.get(1)) {
            int x = arr.remove(0);
            arr.add(x);
            count++;
        }
        int prev=arr.get(0);
        for(int i=1;i<N;i++)
        {
            int next=arr.get(i);
            if(next-prev>1 || next-prev<1)
                count++;


        }*/
        /*int i=0;
        while(i!=N-1)
        {
        if(arr.get(i)>arr.get(i+1))
        {
            int x=arr.remove(i+1);
            arr.add(0,x);
            i++;
            count++;
        }
        else if(arr.get(i+1)-arr.get(i)>1)
        {
            int x=arr.remove(i+1);
            arr.add(x);
            count++;
        }
        else
        {
            i++;
        }
        }*/
        HashMap<Integer,Integer> map=new HashMap<>();
        int max=0;
        for(int i=0;i<N;i++)
        {
            int x=arr.get(i);
            if(map.containsKey(x-1))
            {
                int z=map.get(x-1);
                map.put(x, z + 1);
                if(z+1>max)
                    max=z+1;

            }
            else {
                map.put(x, 1);
                if(1>max)
                    max=1;
            }
        }
        System.out.println(N-max);
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
