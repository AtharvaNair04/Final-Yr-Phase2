import java.util.*;

public class Main {

    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    List<Integer>[] lists = new ArrayList[n];
    int[] col = new int[n];
    int[] part = new int[n];
    long[] diff = new long[n];

    public static void main(String[] args) {
        new Main().run();
    }

    void run() {

        for (int i=0; i<n; i++) col[i] = sc.nextInt() - 1;
        for (int i=0; i<n; i++) lists[i] = new ArrayList<>();
        for (int i=0; i<n-1; i++) {
            int a = sc.nextInt() - 1;
            int b = sc.nextInt() - 1;
            lists[a].add(b);
            lists[b].add(a);
        }

//        System.out.println(lists[0]);

        dfs(0, -1, 0);

        StringBuilder sb = new StringBuilder();

        for (int i=0; i<n; i++) {
            long tmp = n - part[i];
            diff[i] += tmp * (tmp + 1) / 2;
        }

        for (int i=0; i<n; i++) {
            long total = (long)n * (long)(n + 1) / (long)2;
            sb.append( total - diff[i] + "\n");
        }

        System.out.println(sb.toString());

    }

    void dfs(int v, int p, int pSize) {
        int size = pSize;
        for (int next: lists[v]) {
            if (next != p) {
                int tmpCur = part[col[v]];
                int tmpNext = part[col[next]];
                dfs(next, v, tmpNext);
                int total = part[col[next]] - tmpNext;
                long val = total - (part[col[v]] - tmpCur);
                diff[col[v]] += val * (val + 1) / 2;
                size += total;
            }
        }
        size++;
        part[col[v]] = size;
    }
}
