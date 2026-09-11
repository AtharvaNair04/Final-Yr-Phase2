import java.util.*;
class Main{
    static List<int[]>[] to;
    static long[] dist;
    static int n;
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        //距離ありの隣接リスト
        to = new List[n];
        for (int i = 0; i < n; i++) {
            to[i] = new ArrayList<int[]>();
        }
        for(int i = 0; i < n-1; i++){
            int a = scanner.nextInt()-1;
            int b = scanner.nextInt()-1;
            int d = scanner.nextInt();
            to[a].add(new int[]{b,d});
            to[b].add(new int[]{a,d});
        }
        int query = scanner.nextInt();
        int k = scanner.nextInt()-1;
        long[] distK = calcDist(k);

        while(query-- > 0){
            int p = scanner.nextInt()-1;
            int q = scanner.nextInt()-1;
            System.out.println(distK[p] + distK[q]);
        }
    }
    public static long[] calcDist(int p){
        dist = new long[n];
        dfs(p, 0, -1);
        return dist;
    }
    public static void dfs(int p, long d, int par){
        for(int[] qData : to[p]){
            int q = qData[0];
            int PtoQ = qData[1];
            if(q == par){continue;}
            dist[q] = d + PtoQ;
            dfs(q,d+PtoQ,p);
        }
    }
}
