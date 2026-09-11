import java.util.*;
public class Main {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();long D = in.nextLong();
        long[][] p = new long[N][];
        for(int i = 0; i< N; i++)
            p[i] = new long[]{in.nextLong(), in.nextLong()};
        int[] from = new int[8*N], to = new int[8*N];
        int cnt = 0;
        for(int i = 0; i< N; i++){
            for(int j = i+1; j< N; j++){
                for(int u = 0; u< 2; u++)
                    for(int v = 0; v< 2; v++){
                        if(Math.abs(p[i][u]-p[j][v]) < D){
                            from[cnt] = (i<<1)|u;
                            to[cnt] = (j<<1)|v^1;
                            cnt++;
                            from[cnt] = (j<<1)|v;;
                            to[cnt] = (i<<1)|u^1;
                            cnt++;
                        }
                    }
            }
        }
        int[][] g = make(2*N, cnt, from, to), gt = make(2*N, cnt, to, from);
        int[] scc = new StronglyConnectedComponents().scc(2*N, g, gt);
        for(int i = 0; i< N; i++)if(scc[i<<1] == scc[i<<1|1]){
            System.out.println("No");
            return;
        }
        System.out.println("Yes");
        TreeMap<Integer, Integer> mp = new TreeMap<>();
        int c = 0;
        for(int i:scc)c = Math.max(c, i+1);
        int[] ff = new int[cnt], tt = new int[cnt];
        int cc = 0;
        for(int i = 0; i< cnt; i++){
            if(scc[from[i]] != scc[to[i]]){
                ff[cc] = scc[from[i]];
                tt[cc] = scc[to[i]];
                cc++;
            }
        }
        int[][] gg = make(c, cc, ff, tt);
        
        TreeSet<Integer> set = new TreeSet<>();
        int[] indeg = new int[c];
        for(int i = 0; i< cc; i++)indeg[tt[i]]++;
        for(int i = 0; i< c; i++)if(indeg[i] == 0)set.add(i);
        int[] ord = new int[c];
        for(int i = 0; i< c; i++){
            ord[i] = set.pollFirst();
            for(int v:gg[ord[i]]){
                indeg[v]--;
                if(indeg[v] == 0)set.add(v);
            }
        }
        
        for(int i = 0; i< N; i++){
            if(ord[scc[i<<1]] > ord[scc[i<<1|1]])System.out.println(p[i][0]);
            else System.out.println(p[i][1]);
        }
    }
    static int[][] make(int N, int M, int[] from, int[] to){
        int[][] g = new int[N][];int[] cnt = new int[N];
        for(int i = 0; i< M; i++)cnt[from[i]]++;
        for(int i = 0; i< N; i++)g[i] = new int[cnt[i]];
        for(int i = 0; i< M; i++)g[from[i]][--cnt[from[i]]] = to[i];
        return g;
    }
}
class StronglyConnectedComponents {
    public int[] scc(int n, int[][] g, int[][] gt){
        Stack<Integer> st = new Stack<>();
        boolean[] vis = new boolean[n];
        for(int i = 0; i< n; i++)if(!vis[i])fillOrder(g, st, vis, i);
        Arrays.fill(vis, false);
        int[] scc = new int[n];
        int id = 0;
        while(!st.isEmpty()){
            int u = st.pop();
            if(!vis[u]){assignSCC(gt, scc, vis, u, id++);}
        }
        return scc;
    }
    private void assignSCC(int[][] gt, int[] scc, boolean[] vis, int u, int id){
        vis[u] = true;
        scc[u] = id;
        for(int v:gt[u])if(!vis[v])assignSCC(gt, scc, vis, v, id);
    }
    private void fillOrder(int[][] g, Stack<Integer> s, boolean[] vis, int u){
        vis[u] = true;
        for(int v:g[u])if(!vis[v])fillOrder(g, s, vis, v);
        s.push(u);
    }
}
