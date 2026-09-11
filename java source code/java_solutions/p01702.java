import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Arrays;
import java.util.AbstractCollection;
import java.util.Scanner;
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
        UnknownSwitches solver = new UnknownSwitches();
        try {
            int testNumber = 1;
            while (true)
                solver.solve(testNumber++, in, out);
        } catch (UnknownError e) {
            out.close();
        }
    }

    static class UnknownSwitches {
        public void solve(int testNumber, Scanner in, PrintWriter out) {
            final int N = in.nextInt();
            final int M = in.nextInt();
            final int Q = in.nextInt();
            if (N == 0 && M == 0 && Q == 0) throw new UnknownError();
            String[] S = new String[Q + 1];
            String[] B = new String[Q + 1];
            S[0] = new String();
            for (int i = 0; i < N; i++) {
                S[0] += "0";
            }
            B[0] = new String();
            for (int i = 0; i < M; i++) {
                B[0] += "0";
            }
            for (int i = 0; i < Q; i++) {
                S[i + 1] = in.next();
                B[i + 1] = in.next();
            }
            StringBuilder buildAns = new StringBuilder();
            for (int i = 0; i < M; i++) {
                buildAns.append("?");
            }
            boolean[] isHatena = new boolean[M];
            boolean[] visited = new boolean[N];
            Arrays.fill(isHatena, false);
            Arrays.fill(visited, false);

            ArrayList<HashSet<Integer>> sets = new ArrayList<>();
            for (int i = 0; i < Q; i++) {
                HashSet<Integer> set = new HashSet<>();
                for (int j = 0; j < M; j++) {
                    if (B[i].charAt(j) != B[i + 1].charAt(j)) {
                        set.add(j);
                    }
                }
                sets.add(set);
            }

            for (int i = 0; i < N; i++) {
                HashSet<Integer> masterSet = new HashSet<>();
                HashSet<Integer> masterNgSet = new HashSet<>();
                for (int j = 1; j <= Q; j++) {
                    if (S[j].charAt(i) == '1') {
                        visited[i] = true;
                        if (masterSet.size() == 0) {
                            masterSet.addAll(sets.get(j - 1));
                        } else {
                            masterSet.retainAll(sets.get(j - 1));
                        }
                    } else {
                        masterNgSet.addAll(sets.get(j - 1));
                    }
                }
                for (int e : masterSet) {
                    if (!isHatena[e]) {
                        if (buildAns.charAt(e) != '?' && buildAns.charAt(e) != cc(i) && !masterNgSet.contains(e)) {
                            //System.err.println(buildAns.charAt(e) + " " + cc(i));
                            isHatena[e] = true;
                        } else if (!masterNgSet.contains(e)) {
                            buildAns.setCharAt(e, cc(i));
                        }
                    }
                }
            }
            int cnt = 0;
            int notVisit = -1;
            for (int i = 0; i < N; i++) {
                if (!visited[i]) {
                    cnt++;
                    notVisit = i;
                }
            }
            if (cnt == 1) {
                for (int i = 0; i < M; i++) {
                    if (buildAns.charAt(i) == '?' && !isHatena[i]) {
                        buildAns.setCharAt(i, cc(notVisit));
                    }
                }
            }
            for (int i = 0; i < M; i++) {
                if (isHatena[i]) {
                    buildAns.setCharAt(i, '?');
                }
            }
            out.println(buildAns);
        }

        char cc(int n) {
            if (0 <= n && n <= 9) return (char) (n + '0');
            else return (char) (n - 10 + 'A');
        }

    }
}
