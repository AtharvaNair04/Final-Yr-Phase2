import java.util.*;

public class Main{
    
    static int n,m;
    static List<Edge>[] mst;
    static int[][] vmax;
    static boolean[] used;
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        
        int inf = (int)1e9;
        
        n = Integer.parseInt(sc.next());
        m = Integer.parseInt(sc.next());
        
        Kruskal kr = new Kruskal(n);
        for(int i=0; i<m; i++){
            int a = Integer.parseInt(sc.next())-1;
            int b = Integer.parseInt(sc.next())-1;
            int c = Integer.parseInt(sc.next());
            kr.addEdge(a, b, c);
        }
        
        long sum = kr.kruskal();
        mst = kr.getMST();
        
        vmax = new int[n][n];
        used = new boolean[n];
        for(int i=0; i<n; i++){
            Arrays.fill(used, false);
            dfs(i, i);
        }
        
        int q = Integer.parseInt(sc.next());
        for(int i=0; i<q; i++){
            int s = Integer.parseInt(sc.next())-1;
            int t = Integer.parseInt(sc.next())-1;
            System.out.println(sum - vmax[s][t]);
        }
    }
    
    public static void dfs(int s, int now){
        used[now] = true;
        for(int i=0; i<mst[now].size(); i++){
            Edge e = mst[now].get(i);
            if(!used[e.to]){
                int tmp = Math.max(vmax[s][now], e.cost);
                vmax[s][e.to] = Math.max(vmax[s][e.to], tmp);
                dfs(s, e.to);
            }
        }
    }
}

class Kruskal{
    private int n;
    private List<Edge> edge;
    private List<Edge>[] mst;
    private UnionFind uf;
    
    public Kruskal(int n){
        this.n = n;
        edge = new ArrayList<>();
        mst = new List[n];
        for(int i=0; i<n; i++){
            mst[i] = new ArrayList<>();
        }
        uf = new UnionFind(n);
    }
    
    public void addEdge(int from, int to, int cost){
        edge.add(new Edge(from, to, cost));
    }
    
    public long kruskal(){
        long sum = 0;
        Collections.sort(edge);
        for(int i=0; i<edge.size(); i++){
            Edge e = edge.get(i);
            if(uf.unite(e.from, e.to)){
                mst[e.from].add(new Edge(e.from, e.to, e.cost));
                mst[e.to].add(new Edge(e.to, e.from, e.cost));
                sum += e.cost;
            }
        }
        return sum;
    }
    
    public List<Edge>[] getMST(){
        return mst;
    }
}

class UnionFind{
    static int[] par;
    static int[] rank;
    
    public UnionFind(int n){
        par = new int[n];
        Arrays.fill(par, -1);
        rank = new int[n];
    }
    
    public static int find(int x){
        if(par[x] < 0){
            return x;
        }else{
            par[x] = find(par[x]);
            return par[x];
        }
    }
    
    public static boolean unite(int x, int y){
        x = find(x);
        y = find(y);
        if(x==y) return false;
        
        if(rank[x]<rank[y]){
            par[x] = y;
        }else{
            par[y] = x;
            if(rank[x]==rank[y]) rank[x]++;
        }
        
        return true;
    }
    
    public static boolean same(int x, int y){
        return find(x)==find(y);
    }
}

 
class Edge implements Comparable<Edge>{
    public int from = 0;
    public int to = 0;
    public int cost = 0;
    
    public Edge(int from, int to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }
    
    @Override
    public int compareTo(Edge o) {
        return this.cost - o.cost;
    }
}
