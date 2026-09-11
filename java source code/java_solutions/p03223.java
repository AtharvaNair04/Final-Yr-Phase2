import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        Tenka1_2018_C solver = new Tenka1_2018_C();
        solver.solve(1, in, out);
        out.close();
    }

    static class Tenka1_2018_C {
        public void solve(int testNumber, Scanner in, PrintWriter out) {
            int n = in.nextInt();
            List<Integer> list = new ArrayList<Integer>();
            List<Integer> maxlist = new ArrayList<Integer>();
            long ans = 0;
            for (int i = 0; i < n; i++) {
                list.add(in.nextInt());
            }
            Collections.sort(list);

            maxlist.add(list.get(0));
            maxlist.add(list.get(list.size() - 1));
            list.remove(0);
            list.remove(list.size() - 1);


            while (list.size() > 0) {
                if (list.size() < 2) break;
                maxlist.add(0, list.get(list.size() - 1));
                list.remove(list.size() - 1);

                maxlist.add(list.get(0));
                list.remove(0);

                if (list.size() < 2) break;
                maxlist.add(list.get(list.size() - 1));
                list.remove(list.size() - 1);

                maxlist.add(0, list.get(0));
                list.remove(0);

            }

            if (list.size() == 1) {
                int last = list.get(0);

                if (Math.abs(last - maxlist.get(0)) > Math.abs(last - maxlist.get(maxlist.size() - 1))) {
                    maxlist.add(0, list.get(0));
                } else {
                    maxlist.add(list.get(0));
                }
            }

            for (int i = 0; i < maxlist.size() - 1; i++) {
                ans += Math.abs(maxlist.get(i) - maxlist.get(i + 1));
            }

            out.print(ans);
        }

    }
}
