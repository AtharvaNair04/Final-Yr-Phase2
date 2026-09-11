import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
 
public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int V = in.nextInt();
        int E = in.nextInt();
 
        used = new boolean[V];
        lEdge = new List[V];
        for (int i=0;i<V;i++) {
            lEdge[i] = new ArrayList<Edge>();
        }
         
        for (int i=0;i<E;i++) {
            addEdge(in.nextInt(), in.nextInt(), in.nextInt());
        }
         
        int res = 0;
        while(true) {
            Arrays.fill(used, false);
            int ret = dfs(0, V-1, Integer.MAX_VALUE);
            if (ret==0) break;
            res += ret;
        }
        System.out.println(res);
    }
    static List<Edge>[] lEdge;
    static void addEdge(int fr, int to, int cap) {
        lEdge[fr].add(new Edge(to, cap, lEdge[to].size()));
        lEdge[to].add(new Edge(fr, 0, lEdge[fr].size()-1));
    }
     
    static class Edge {
        int to, cap, rev;
        public Edge(int to, int cap, int rev) {
            this.to= to;
            this.cap = cap;
            this.rev = rev;
        }
    }
 
    static boolean[] used;
     
    static int dfs (int fr, int to, int f) {
        if (fr==to) return f;
        used[fr] = true;
         
        for (int i=0;i<lEdge[fr].size();i++) {
            Edge e = lEdge[fr].get(i);
            if (!used[e.to] && e.cap>0) {
                int dist = dfs(e.to, to, Math.min(f, e.cap));
                if (dist>0) {
                    e.cap -=dist;
                    lEdge[e.to].get(e.rev).cap += dist;
                    return dist;
                }
            }
        }
        return 0;
    }
     
}
