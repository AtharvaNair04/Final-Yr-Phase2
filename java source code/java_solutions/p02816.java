import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.io.BufferedOutputStream;
import java.util.HashSet;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.stream.Collectors;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.Map.Entry;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author mikit
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        LightScanner in = new LightScanner(inputStream);
        LightWriter out = new LightWriter(outputStream);
        FXorShift solver = new FXorShift();
        solver.solve(1, in, out);
        out.close();
    }

    static class FXorShift {
        public void solve(int testNumber, LightScanner in, LightWriter out) {
            // out.setBoolLabel(LightWriter.BoolLabel.YES_NO_FIRST_UP);
            int n = in.ints();
            int[] a = in.ints(n), b = in.ints(n);
            Map<Integer, Integer> freq = new HashMap();
            for (int i = 0; i < n; i++) freq.merge(a[i], 1, Integer::sum);
            HashSet<Integer> witness = new HashSet<>();
            int total = 0;
            for (int x : freq.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList())) {
                if (total + freq.get(x) < 200) {
                    witness.add(x);
                    total += freq.get(x);
                }
            }
            List<Integer> witnessIndex = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (witness.contains(a[i])) witnessIndex.add(i);
            }


            int[] p = IntStream.range(0, n).toArray();
            ArrayUtil.shuffle(p);

            List<Integer> ans = new ArrayList<>();
            loop:
            for (int i = 0; i < n; i++) {
                int x = a[i] ^ b[0];
                for (int j = 0; j < Math.min(n, 300); j++) {
                    if ((a[(p[j] + i) % n] ^ b[p[j]]) != x) continue loop;
                }
                ans.add(i);
            }
            loop:
            for (int i : ans) {
                int x = a[i] ^ b[0];
                if (ans.size() <= 100) {
                    for (int j = 0; j < n; j++) {
                        if ((a[(j + i) % n] ^ b[j]) != x) continue loop;
                    }
                }
                for (int w : witnessIndex) {
                    if ((a[w] ^ b[(w - i + n) % n]) != x) continue loop;
                }
                out.ans(i).ans(x).ln();
            }
        }

    }

    static class LightScanner {
        private BufferedReader reader = null;
        private StringTokenizer tokenizer = null;

        public LightScanner(InputStream in) {
            reader = new BufferedReader(new InputStreamReader(in));
        }

        public String string() {
            if (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int ints() {
            return Integer.parseInt(string());
        }

        public int[] ints(int length) {
            return IntStream.range(0, length).map(x -> ints()).toArray();
        }

    }

    static final class ArrayUtil {
        private ArrayUtil() {
        }

        public static void shuffle(int[] array) {
            Random rnd = ThreadLocalRandom.current();
            for (int i = array.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                int tmp = array[index];
                array[index] = array[i];
                array[i] = tmp;
            }
        }

    }

    static class LightWriter implements AutoCloseable {
        private final Writer out;
        private boolean autoflush = false;
        private boolean breaked = true;

        public LightWriter(Writer out) {
            this.out = out;
        }

        public LightWriter(OutputStream out) {
            this(new OutputStreamWriter(new BufferedOutputStream(out), Charset.defaultCharset()));
        }

        public LightWriter print(char c) {
            try {
                out.write(c);
                breaked = false;
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            return this;
        }

        public LightWriter print(String s) {
            try {
                out.write(s, 0, s.length());
                breaked = false;
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            return this;
        }

        public LightWriter ans(String s) {
            if (!breaked) {
                print(' ');
            }
            return print(s);
        }

        public LightWriter ans(int i) {
            return ans(Integer.toString(i));
        }

        public LightWriter ln() {
            print(System.lineSeparator());
            breaked = true;
            if (autoflush) {
                try {
                    out.flush();
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
            return this;
        }

        public void close() {
            try {
                out.close();
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

    }
}
