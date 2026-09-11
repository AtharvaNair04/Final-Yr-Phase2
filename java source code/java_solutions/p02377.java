import java.io.*;
import java.util.*;

public class Main implements Runnable{
    private ArrayList<ArrayList<Integer>> graph;
    private Edge[] edges;
    public static void main(String[] args) throws Exception {
        new Thread(null, new Main(), "bridge", 16 * 1024 * 1024).start();
    }

    @Override
    public void run() {
        try {
            solve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void solve() throws Exception{
        FastScanner scanner = new FastScanner(System.in);

        int V = scanner.nextInt();
        int E = scanner.nextInt();
        int F = scanner.nextInt();

        graph = new ArrayList<>(V);
        edges = new Edge[E * 2];
        for(int i = 0; i < V; ++i){
            graph.add(new ArrayList<Integer>());
        }

        int edgeSize = 0;
        for(int i = 0; i < E; ++i){
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int cap = scanner.nextInt();
            int cost = scanner.nextInt();

            edges[edgeSize++] = new Edge(v, 0, cap, cost);
            edges[edgeSize++] = new Edge(u, 0, 0, -cost);
            graph.get(u).add(edgeSize - 2);
            graph.get(v).add(edgeSize - 1);
        }

        costOfMaxFlow(F, 0, V - 1);
    }

    private void costOfMaxFlow(int F, int s, int t){
        int V = graph.size();
        int[] dist = new int[V];
        int[] curFlow = new int[V];
        int[] prevNode = new int[V];
        int[] prevEdge = new int[V];

        int totalFlow = 0;
        int totalCost = 0;

        while(totalFlow < F){
            BellmanFord(s, dist, prevNode, prevEdge, curFlow);
            if(dist[t] == Integer.MAX_VALUE){
                break;
            }

            int pathFlow = Math.min(curFlow[t], F - totalFlow);
            totalFlow += pathFlow;

            for(int v = t; v != s; v = prevNode[v]){
                Edge edge = edges[prevEdge[v]];
                totalCost += edge.cost * pathFlow;

                edge.flow += pathFlow;
                edges[prevEdge[v] ^ 1].flow -= pathFlow;
            }
        }

        if(totalFlow < F){
            System.out.println("-1");
        }
        else{
            System.out.println(totalCost);
        }
    }

    private void BellmanFord(int s, int[] dist, int[] prevNode, int[] prevEdge, int[] curFlow){
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[s] = 0;
        curFlow[s] = Integer.MAX_VALUE;
        prevNode[s] = -1;
        prevEdge[s] = -1;

        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(s);
        boolean[] inQueue = new boolean[dist.length];
        inQueue[s] = true;
        while(!queue.isEmpty()){
            Integer u = queue.poll();
            inQueue[u] = false;

            for(int edgeIndex : graph.get(u)){
                Edge edge = edges[edgeIndex];
                if(edge.flow >= edge.cap){
                    continue;
                }

                if(dist[edge.v] > dist[u] + edge.cost){
                    dist[edge.v] = dist[u] + edge.cost;
                    prevNode[edge.v] = u;
                    prevEdge[edge.v] = edgeIndex;
                    curFlow[edge.v] = Math.min(curFlow[u], edge.cap - edge.flow);
                    if(!inQueue[edge.v]){
                        queue.add(edge.v);
                    }
                }
            }
        }
    }

    static class Edge{
        int v;
        int flow;
        int cap;
        int cost;

        public Edge(int v, int flow, int cap, int cost){
            this.v = v;
            this.flow = flow;
            this.cap = cap;
            this.cost = cost;
        }
    }

    static class FastScanner {
        private InputStream in;
        private final byte[] buffer = new byte[1024 * 8];
        private int ptr = 0;
        private int buflen = 0;

        public FastScanner(InputStream in){
            this.in = in;
        }

        private boolean hasNextByte() {
            if (ptr < buflen) {
                return true;
            } else {
                ptr = 0;
                try {
                    buflen = in.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (buflen <= 0) {
                    return false;
                }
            }
            return true;
        }

        private int readByte() {
            if (hasNextByte()) return buffer[ptr++];
            else return -1;
        }

        private static boolean isPrintableChar(int c) {
            return 33 <= c && c <= 126;
        }

        private void skipUnprintable() {
            while (hasNextByte() && !isPrintableChar(buffer[ptr])) ptr++;
        }

        public boolean hasNext() {
            skipUnprintable();
            return hasNextByte();
        }

        public String next() {
            if (!hasNext()) throw new NoSuchElementException();
            StringBuilder sb = new StringBuilder();
            int b = readByte();
            while (isPrintableChar(b)) {
                sb.appendCodePoint(b);
                b = readByte();
            }
            return sb.toString();
        }

        public long nextLong() {
            return Long.parseLong(next());
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }
    }
}
