import java.util.*;
public class Main {
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(), M = in.nextInt();
        MaxFlowDinic maxflow = new MaxFlowDinic(N*M+2);
        int SRC = N*M, DEST = N*M+1;
        char[][] g = new char[N][];
        for(int i = 0; i< N; i++){
            g[i] = in.next().toCharArray();
            for(int j = 0; j< M; j++){
                if(g[i][j] == '.'){
                    if((i+j)%2 == 0)maxflow.addEdge(SRC, i*M+j, 1);
                    else maxflow.addEdge(i*M+j, DEST, 1);
                }
            }
        }
        int[][] D = new int[][]{{-1, 0},{0,-1},{1,0},{0,1}};
        for(int i = 0; i< N; i++){
            for(int j = 0; j< M; j++){
                if((i+j)%2 == 0 && g[i][j] == '.'){
                    for(int[] d:D){
                        int ii = i+d[0], jj = j+d[1];
                        if(ii < 0 || jj < 0 || ii >= N || jj >= M || g[ii][jj] == '#')continue;
                        
                        maxflow.addEdge(i*M+j, ii*M+jj, 1);
                    }
                }
            }
        }
        char[] forward = new char[]{'^', '<','v', '>'}, backward = new char[]{'v', '>','^', '<'};
        
        long ans = maxflow.dinic(SRC, DEST);
        char[][] out = new char[N][M];
        for(int i = 0; i< N; i++){
            for(int j = 0; j< M; j++){
                if(g[i][j] == '#')g[i][j] = '#';
                else if((i+j)%2 == 0){
                    
                    int u = i*M+j;
                    for(MaxFlowDinic.Edge e:maxflow.adj[u]){
                        if(e.c == 1 && e.flow == 1){
                            int ind = -1, v = e.v;
                            for(int d = 0; d< D.length; d++){
                                int ii = i+D[d][0], jj = j+D[d][1];
                                if(ii == v/M && jj == v%M){
                                    g[i][j] = forward[d];
                                    g[ii][jj] = backward[d];
                                }
                            }
                            
                        }
                    }
                }
            }
        }
        System.out.println(ans);
        for(char[] ch:g){
            for(char c:ch)System.out.print(c);
            System.out.println();
        }
    }
}
class MaxFlowDinic{
        final long IINF = (long)1e18;
        int V;
        int[] level;
        ArrayList<Edge>[] adj;
        public MaxFlowDinic(int v){
            V = v;
            adj = new ArrayList[V];
            for(int i = 0; i< V; i++)adj[i] = new ArrayList<>();
            level = new int[V];
        }
        void addEdge(int u, int v, long w){
            Edge a = new Edge(v,0,w,adj[v].size());
            Edge b = new Edge(u,0,0,adj[u].size());
            adj[u].add(a);
            adj[v].add(b);
        }
        long dinic(int s, int t){
            if(s==t)return 0;
            long total = 0;
            while(bfs(s,t)){
                int[] start = new int[V];
                long flow = 0;
                while((flow = sendFlow(s,t,IINF, start)) >0)total+=flow;
            }
            return total;
        }
        long sendFlow(int u, int t, long flow, int[] start){
            if(u==t)return flow;
            for(; start[u]<adj[u].size(); start[u]++){
                Edge e = adj[u].get(start[u]);
                if(level[e.v] == level[u]+1 && e.flow<e.c){
                    long cur = Math.min(flow, e.c-e.flow);
                    long tmp_flow = sendFlow(e.v,t, cur, start);
                    if(tmp_flow>0){
                        e.flow+=tmp_flow;adj[e.v].get(e.rev).flow -= tmp_flow;
                        return tmp_flow;
                    }
                }
            }
            return 0;
        }
        boolean bfs(int s, int t){
            for(int i = 0; i< V; i++)level[i] = -1;
            level[s] = 0;
            Queue<Integer> q = new LinkedList<>();
            q.add(s);
            while(!q.isEmpty()){
                int x = q.poll();
                for(Edge e:adj[x]){
                    if(level[e.v] == -1 && e.flow<e.c){
                        level[e.v] = level[x]+1;
                        q.add(e.v);
                    }
                }
            }
            return level[t]>=0;
        }
        class Edge{
            int v, rev;
            long flow, c;
            public Edge(int V, long F, long C, int R){
                v = V;flow = F;c = C;rev = R;
            }
        }
    }
