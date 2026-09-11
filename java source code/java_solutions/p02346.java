import java.util.Scanner;

public class Main {
    int n, q;
    Query[] qs;

    public static void main(String[] args) {
        Main p = new Main();
        p.read();
        p.solve();
    }

    void read() {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        q = sc.nextInt();
        qs = new Query[q];

        for (int i = 0; i < q; i++) {
            int c = sc.nextInt();
            int a = sc.nextInt();
            int b = sc.nextInt();
            qs[i] = new Query(c, a, b);
        }
    }

    void solve() {
        SegmentTree tree = new SegmentTree(n);
        for (Query query: qs) {
            if (query.com == 0) {
                tree.add(query.x, query.y);
            }
            else {
                int ans = tree.query(query.x, query.y+1,
                        0, 0, tree.n);
                System.out.println(ans);
            }
        }
    }

    private class SegmentTree {
        int n;
        int[] tree;

        SegmentTree(int a) {
            for (n = 1; n <= a; n = n << 1) {}
            tree = new int[2 * n - 1];
        }

        void add(int i, int x) {
            i += n - 1;
            tree[i] += x;
            while (i > 0) {
                i = (i - 1) / 2;
                tree[i] = tree[2 * i + 1] + tree[2 * i + 2];
            }
        }

        int query(int s, int e, int k, int l, int r) {
            if (r <= s || e <= l) return 0;
            if (s <= l && r <= e) return tree[k];

            int vl = query(s, e, 2 * k + 1, l, (r + l) / 2);
            int vr = query(s, e, 2 * k + 2, (r + l) / 2, r);
            return vl + vr;
        }
    }

    private class Query {
        int com, x, y;
        Query(int c, int a, int b) {
            com = c;
            x = a;
            y = b;
        }
    }
}
