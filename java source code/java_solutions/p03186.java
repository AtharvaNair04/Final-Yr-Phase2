import java.io.*;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);

        int A, B, C;
        A = reader.nextInt();
        B = reader.nextInt();
        C = reader.nextInt();

        int ans = B + Math.min(A + B + 1, C);
        System.out.println(ans);
    }

    /////////////////
    public static class InputReader {

        private StringTokenizer tokenizer;
        private BufferedReader reader;

        public InputReader(InputStream in) {
            assert in != null;
            reader = new BufferedReader(new InputStreamReader(in), Short.MAX_VALUE);
        }

        public String next() {
            if (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public String nextLine() {
            if (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = null;
                try {
                    return reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                StringBuilder builder = new StringBuilder();
                while (tokenizer.hasMoreTokens()) {
                    builder.append(tokenizer.nextToken());
                    builder.append(' ');
                }
                return builder.toString();
            }
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }

        public void close() {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
