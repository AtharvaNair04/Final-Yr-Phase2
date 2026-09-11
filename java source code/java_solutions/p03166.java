import java.io.*;
import java.util.*;

public class Main {
    static PrintWriter pw =new PrintWriter(new OutputStreamWriter(System.out));
    public static void main(String[] args) {
        MyScanner ms = new MyScanner();
        int N  = ms.nextInt();
        int M = ms.nextInt();
        graph G = new graph(N);
        for(int i=0;i<M;i++)
        G.addEdge(ms.nextInt(), ms.nextInt());
        System.out.println(G.longestPath());
    }
    public static class MyScanner {
        BufferedReader br;
        StringTokenizer st;
   
        public MyScanner() {
           br = new BufferedReader(new InputStreamReader(System.in));
        }
   
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
   
        int nextInt() {
            return Integer.parseInt(next());
        }
   
        long nextLong() {
            return Long.parseLong(next());
        }
   
        double nextDouble() {
            return Double.parseDouble(next());
        }
   
        String nextLine(){
            String str = "";
        try {
           str = br.readLine();
        } catch (IOException e) {
           e.printStackTrace();
        }
        return str;
        }
  
     }
    static class DSU{
        int [] parent;
        int [] size;
      public   HashMap<Integer,Integer> map = new HashMap<>();
        DSU(int N){
            parent = new int[N+1];
            size = new int[N+1];
            for(int i=0;i<=N;i++){
                parent[i] = i;
                size[i] = 1;
            }
        }
        int find_set(int v){
            if(v==parent[v])return v;
            return parent[v]=find_set(parent[v]);
        }
        void union_set(int a, int b){
            a = find_set(a);
            b = find_set(b);
            if(a!=b){
                map.remove(a);
                map.remove(b);
                parent[b] = a;
                size[a]+=size[b];
                map.put(a, size[a]);
            }
        }
    }
    static class graph{
        int V;
        ArrayList<ArrayList<IP>> list;
        public DSU cc;
        static class IP implements Comparable<IP>{
            int v,weight;
            IP(int v, int weight){
                this.v = v;
                this.weight = weight;
            }
          public  int compareTo(IP ip){
                return weight-ip.weight;
            }
        }
        graph(int V){
            this.V = V;
            list = new ArrayList<ArrayList<IP>>(V+1);
             cc = new DSU(V+1);
             for (int i = 0; i <=V; i++) 
             list.add(new ArrayList<IP>()); 
        }
        void addEdge(int u, int v){
            list.get(u).add(new IP(v, 0));
           ///// list.get(v).add(new IP(u, 0));
            cc.union_set(u,v);
        }
        void addEdge(int u,int v,int weight){
            list.get(u).add(new IP(v, weight));
            list.get(v).add(new IP(u, weight));
            cc.union_set(u,v);
            cc.union_set(4, 2);
        }
        public  int [] subtree(int u){
            int [] subtree = new int[V+1];
                dfsSubtree(u,-1,subtree);
            return subtree;
        }
        void dfsSubtree(int at,int parent,int [] subtree){
                subtree[at]+=1;
              for(int i=0;i<list.get(at).size();i++){
                int v = list.get(at).get(i).v;
                    if(at!=parent){
                        dfsSubtree(v, at, subtree);
                        subtree[at]+=subtree[v];
                    }
                }
            }
        
        int [][] numberOFCCwithNumberOfVertices(){
            int [][] arr= new int[cc.map.size()][2];
            int i=0;
            for (Map.Entry<Integer, Integer> entry : cc.map.entrySet()) {
                arr[i][0] = entry.getKey();
                arr[i][1] = entry.getValue();
            }
            
            return arr;
            

        }
        int []  dijkstra(int source){
            PriorityQueue<IP> pq = new PriorityQueue<>();
            pq.add(new IP(source, 0));
            int [] distance = new int[V+1];
            Arrays.fill(distance, 1000000);
            distance[source]= 0;
            while(!pq.isEmpty()){
                IP poll = pq.poll();
                int u= poll.v;
               for(int i=0;i<list.get(u).size();i++){
                   int v = list.get(u).get(i).v;
                   if(distance[v]>distance[u]+list.get(u).get(i).weight){
                       distance[v]=distance[u]+list.get(u).get(i).weight;
                       pq.add(new IP(v, distance[v]));
                   }
               }
            }
            return distance;
        }
        //Longest path does not contain directed cycle
        int longestPath(){
            int [] dis = new int[V+1];
            Arrays.fill(dis, -1);
            int max = Integer.MIN_VALUE;
            for(int i=1;i<=V;i++){
                if(dis[i]==-1){
                    max = Math.max(dfs(i,dis),max);
                }
            }
            return max;
        }
    

    public int dfs(int i, int[] dis) {
        if(dis[i]!=-1)return dis[i];
        int ret = 0;
        for(int j=0;j<list.get(i).size();j++){
                ret = Math.max(ret, dfs(list.get(i).get(j).v,dis)+1);
        }
        return dis[i] = ret;
    }
}
}
