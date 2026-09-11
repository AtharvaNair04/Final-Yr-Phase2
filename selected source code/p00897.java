import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;
 
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            int cap = sc.nextInt();
            if (n == 0) {
                break;
            }
            String src = sc.next();
            String dest = sc.next();
            HashMap<String, Integer> hm = new HashMap<String, Integer>();
            String[][] a = new String[n][2];
            int[] d = new int[n];
            for(int i=0;i<n;i++) {
                a[i][0] = sc.next();
                a[i][1] = sc.next();
                d[i] = sc.nextInt();
            }
            for(int i=0;i<n;i++) {
                for(int j=0;j<2;j++) {
                    if (!hm.containsKey(a[i][j])) {
                        hm.put(a[i][j], hm.size());
                    }
                }
            }
            int ss = hm.get(src);
            int gg = hm.get(dest);
            int v = hm.size();
            Graph g1 = new Graph(v);
            Graph g2 = new Graph(m+3);
            for(int i=0;i<n;i++) {
                g1.addBidirectionalEdge(hm.get(a[i][0]), hm.get(a[i][1]), d[i]);
            }
            int[] s = new int[m+2];
            for(int i=0;i<m;i++) {
                s[i] = hm.get(sc.next());
            }
            s[m] = ss;
            s[m+1] = gg;
            for(int i=0;i<m+1;i++) {
                int[] dist = g1.minDistDijkstra(s[i]);
                for(int j=i+1;j<m+2;j++) {
                    if (dist[s[j]] <= cap * 10) {
                        g2.addBidirectionalEdge(i, j, dist[s[j]]);
                    }
                }
            }
            int ans = g2.minDistDijkstra(m)[m+1];
            System.out.println(ans >= Graph.INF ? -1 : ans);
            System.gc();
        }
    }
 
}
 
class Graph {
    public static final int INF = 1<<29;
    int n;
    ArrayList<Edge>[] graph;
 
    @SuppressWarnings("unchecked")
    public Graph(int n) {
        this.n = n;
        this.graph = new ArrayList[n];
        for(int i=0;i<n;i++) {
            graph[i] = new ArrayList<Edge>();
        }
    }
 
    public void addBidirectionalEdge(int from,int to,int cost) {
        addEdge(from,to,cost);
        addEdge(to,from,cost);
    }
    public void addEdge(int from,int to,int cost) {
        graph[from].add(new Edge(to, cost));
    }
 
    //dijkstra O(ElogV)
    public int[] minDistDijkstra(int s) {
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[s] = 0;
        PriorityQueue<Node> q = new PriorityQueue<Node>();
        q.offer(new Node(0, s));
        while(!q.isEmpty()) {
            Node node = q.poll();
            int v = node.id;
            if (dist[v] < node.dist) {
                continue;
            }
            for(Edge e:graph[v]) {
                if (dist[e.to] > dist[v] + e.cost) {
                    dist[e.to] = dist[v] + e.cost;
                    q.add(new Node(dist[e.to], e.to));
                }
            }
        }
        return dist;
    }
 
    //O(E) all cost is 0 or 1
    public int[] minDistQueue(int s) {
        int[] d = new int[n];
        Arrays.fill(d, INF);
        ArrayDeque<Integer> q = new ArrayDeque<Integer>();
        q.add(s);
        d[s] = 0;
        while(!q.isEmpty()) {
            int v = q.pollFirst();
            for(Edge e:graph[v]) {
                int u = e.to;
                if (d[v] + e.cost < d[u]) {
                    d[u] = d[v] + e.cost;
                    if (e.cost == 0) {
                        q.addFirst(u);
                    }else{
                        q.addLast(u);
                    }
                }
            }
        }
        return d;
    }
 
    //topologicalSort O(V+E)
    public ArrayList<Integer> topologicalSort() {
        ArrayList<Integer> order = new ArrayList<Integer>();
        int[] col = new int[n];
        for(int u=0;u<n;u++) {
            if (col[u] == 0 && !visitTS(order, col, u)) {
                return null;
            }
        }
        Collections.reverse(order);
        return order;
    }
    private boolean visitTS(ArrayList<Integer> order,int[] col ,int v) {
        col[v] = 1;
        for(Edge e:graph[v]) {
            if (col[e.to] == 2) {
                continue;
            }
            if (col[e.to] == 1) {
                return false;
            }
            if (!visitTS(order, col, e.to)) {
                return false;
            }
        }
        order.add(v);
        col[v] = 2;
        return true;
    }
 
    //longest Path in DAG
    public ArrayList<Integer> longestPathInDAG(int s,int g) {
        int[] dist = new int[n];
        int[] bef = new int[n];
        Arrays.fill(dist, -INF);
        Arrays.fill(bef, -1);
        ArrayList<Integer> order = topologicalSort();
        if (order == null) {
            return null;
        }
        dist[s] = 0;
        for(int i=0;i<n;i++) {
            int u = order.get(i);
            for(Edge e:graph[u]) {
                int v = e.to;
                if (dist[v] < dist[u] + e.cost) {
                    dist[v] = dist[u] + e.cost;
                    bef[v] = u;
                }
            }
        }
        ArrayList<Integer> path = new ArrayList<Integer>();
        int now = g;
        while(true) {
            path.add(now);
            if (bef[now] != -1) {
                now = bef[now];
                if (now == s) {
                    break;
                }
            }else{
                return null;
            }
        }
        path.add(now);
        Collections.reverse(path);
        return path;
    }
    public int longestPathLengthInDAG() {
        int[] dist = new int[n];
        Arrays.fill(dist, 0);
        ArrayList<Integer> order = topologicalSort();
        if (order == null) {
            return -1;
        }
        int max = 0;
        for(int i=0;i<n;i++) {
            int u = order.get(i);
            for(Edge e:graph[u]) {
                int v = e.to;
                if (dist[v] < dist[u] + e.cost) {
                    dist[v] = dist[u] + e.cost;
                    max = Math.max(max,dist[v]);
                }
            }
        }
        return max;
    }
 
    public ArrayList<ArrayList<Integer>> stronglyConnectedComponents() {
        ArrayList<ArrayList<Integer>> scc = new ArrayList<ArrayList<Integer>>();
        int[] num = new int[n];
        int[] low = new int[n];
        ArrayDeque<Integer> s = new ArrayDeque<Integer>();
        boolean[] inS = new boolean[n];
        int time = 0;
        for(int u=0;u<n;u++) {
            if (num[u] == 0) {
                visitSCC(u,scc,s,inS,low,num,time);
            }
        }
        return scc;
    }
    private void visitSCC(int v, ArrayList<ArrayList<Integer>> scc,
            ArrayDeque<Integer> s, boolean[] inS, int[] low, int[] num, int time) {
        low[v] = num[v] = ++time;
        s.push(v);
        inS[v] = true;
        for(Edge e:graph[v]) {
            int w = e.to;
            if (num[w] == 0) {
                visitSCC(w,scc,s,inS,low,num,time);
                low[v] = Math.min(low[v],low[w]);
            }else if (inS[w]) {
                low[v] = Math.min(low[v],num[w]);
            }
        }
        if (low[v] == num[v]) {
            ArrayList<Integer> scc1 = new ArrayList<Integer>();
            while(true) {
                int w = s.poll();
                inS[w] = false;
                scc1.add(w);
                if (v == w) {
                    break;
				}
            }
            scc.add(scc1);
        }
    }
 
    //dont' remove multiple edge
    public Graph noSCCGraph(int[] map) {
        ArrayList<ArrayList<Integer>> scc = stronglyConnectedComponents();
        int m = scc.size();
        for(int i=0;i<m;i++) {
            ArrayList<Integer> l = scc.get(i);
            for(int v: l) {
                map[v] = i;
            }
        }
        Graph h = new Graph(m);
        for(int u=0;u<n;u++) {
            for(Edge e:graph[u]) {
                h.addEdge(map[u], map[e.to], e.cost);
            }
        }
        return h;
    }
 
    class Edge {
        int to;
        int cost;
        public Edge(int to,int cost) {
            this.to = to;
            this.cost = cost;
        }
    }
    class Node implements Comparable<Node>{
        int dist;
        int id;
        public Node(int dist,int i) {
            this.dist = dist;
            this.id = i;
        }
        public int compareTo(Node o) {
            return (this.dist < o.dist) ? -1 : ((this.dist == o.dist) ? 0 : 1);
        }
    }
}
