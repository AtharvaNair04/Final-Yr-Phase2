import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Main {

    private static class Entry {
        final int id;
        final long cost;
        
        Entry(int id, long cost) {
            this.id = id;
            this.cost = cost;
        }
    }

    public static void main(String[] args) throws IOException {
        int[][] costs;
        List<List<Entry>> links = new ArrayList<>();
        int n, m;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String[] array;
            array = in.readLine().split(" ");
            n = Integer.parseInt(array[0]);
            m = Integer.parseInt(array[1]);
            
            for (int i = 0; i < n; ++i) {
                links.add(new ArrayList<>());
            }

            for (int i = 0; i < m; ++i) {
                array = in.readLine().split(" ");
                int l = Integer.parseInt(array[0]) - 1;
                int r = Integer.parseInt(array[1]) - 1;
                int c = Integer.parseInt(array[2]);
                
                links.get(r).add(new Entry(l, c));
            }
        }
        
        BitSet closed = new BitSet();
        PriorityQueue<Entry> h = new PriorityQueue<>(Comparator.comparingLong(e -> e.cost));
        h.add(new Entry(n - 1, 0));
        while (!h.isEmpty()) {
            Entry e = h.poll();
            
            int n1 = e.id;
            if (closed.get(n1)) {
                continue;
            }
            closed.set(n1);
            if (n1 == 0) {
                System.out.println(e.cost);
                System.exit(0);
            }

            long c1 = e.cost;
            for (Entry e2 : links.get(n1)) {
                int n2 = e2.id;
                if (closed.get(n2)) {
                    continue;
                }
                long c2 = e2.cost;
                h.add(new Entry(n2, c1 + c2));
            }
            
            int n3 = n1 + 1;
            if (n3 < n) {
                if (!closed.get(n3)) {
                    h.add(new Entry(n3, c1));
                }
            }
        }
        System.out.println(-1);
    }
}
