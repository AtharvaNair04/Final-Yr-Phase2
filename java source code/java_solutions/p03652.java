import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Arrays;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main
{
    public static void main(String[] args)
    {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        TaskB solver = new TaskB();
        solver.solve(1, in, out);
        out.close();
    }

    static class TaskB
    {
        int[][] a;
        int n;
        int m;

        public void solve(int testNumber, InputReader in, OutputWriter out)
        {
            n = in.readInt();
            m = in.readInt();
            a = IOUtils.readIntTable(in, n, m);
            int low = 0;
            int high = n;
            while (low < high)
            {
                int mid = (low + high) / 2;
                if (check(mid))
                {
                    high = mid;
                } else
                {
                    low = mid + 1;
                }
            }
            out.printLine(low);
        }

        boolean check(int x)
        {
            boolean[] set = new boolean[n];
            int[] ptr = new int[n];
            Arrays.fill(ptr, 0);
            Arrays.fill(set, false);
            boolean ret = true;
            HashSet<Integer> rejected = new HashSet<Integer>();
            while (x >= 0)
            {
                HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
                for (int i = 0; i < n; i++)
                {
                    if (hm.containsKey(a[i][ptr[i]]))
                    {
                        hm.put(a[i][ptr[i]], hm.get(a[i][ptr[i]]) + 1);
                    } else
                    {
                        hm.put(a[i][ptr[i]], 1);
                    }
                }
                for (int key : hm.keySet())
                {
                    if (hm.get(key) > x)
                    {
                        rejected.add(key);
                    }
                }
                boolean check = true;
                for (int i = 0; i < n; i++)
                {
                    while (ptr[i] < m && rejected.contains(a[i][ptr[i]]))
                    {
                        check = false;
                        ptr[i]++;
                    }
                    if (ptr[i] == m)
                    {
                        ret = false;
                        check = true;
                    }
                }
                if (check)
                {
                    break;
                }
            }
            return ret;
        }

    }

    static class OutputWriter
    {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream)
        {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer)
        {
            this.writer = new PrintWriter(writer);
        }

        public void close()
        {
            writer.close();
        }

        public void printLine(int i)
        {
            writer.println(i);
        }

    }

    static class IOUtils
    {
        public static int[] readIntArray(InputReader in, int size)
        {
            int[] array = new int[size];
            for (int i = 0; i < size; i++)
            {
                array[i] = in.readInt();
            }
            return array;
        }

        public static int[][] readIntTable(InputReader in, int rowCount, int columnCount)
        {
            int[][] table = new int[rowCount][];
            for (int i = 0; i < rowCount; i++)
            {
                table[i] = readIntArray(in, columnCount);
            }
            return table;
        }

    }

    static class InputReader
    {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private InputReader.SpaceCharFilter filter;

        public InputReader(InputStream stream)
        {
            this.stream = stream;
        }

        public int read()
        {
            if (numChars == -1)
            {
                throw new InputMismatchException();
            }
            if (curChar >= numChars)
            {
                curChar = 0;
                try
                {
                    numChars = stream.read(buf);
                } catch (IOException e)
                {
                    throw new InputMismatchException();
                }
                if (numChars <= 0)
                {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int readInt()
        {
            int c = read();
            while (isSpaceChar(c))
            {
                c = read();
            }
            int sgn = 1;
            if (c == '-')
            {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do
            {
                if (c < '0' || c > '9')
                {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public boolean isSpaceChar(int c)
        {
            if (filter != null)
            {
                return filter.isSpaceChar(c);
            }
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c)
        {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public interface SpaceCharFilter
        {
            public boolean isSpaceChar(int ch);

        }

    }
}
