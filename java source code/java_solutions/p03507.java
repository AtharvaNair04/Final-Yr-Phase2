import java.io.*;
import java.util.*;
 
class Main{

    // pos自身を含め西側にある花の数がK個以上ならtrue
    boolean check(long[] w, long[] d, long K, long pos){
        long count = 0;
        for(int i = 0; i < w.length; i++){
            if(pos < w[i]) continue;
            count += (pos - w[i]) / d[i] + 1;
        }

        if(count >= K){
            return true;
        } else {
            return false;
        }
    }

    void solve(){
        int N = ni();
        long K = nl();
        long[] w = new long[N];
        long[] d = new long[N];
        for(int i = 0; i < N; i++){
            w[i] = nl();
            d[i] = nl();
        }

        long ng = -1;
        long ok = 7000000000000000000L;
        while(Math.abs(ok - ng) > 1){
            long mid = (ok + ng) / 2;
            if(check(w, d, K, mid)){
                ok = mid;
            } else {
                ng = mid;
            }
        }

        System.out.println(ok);
        
    } 

    public static void main(String[] args){
        new Main().solve();
    }

    private FastScanner scan = new FastScanner();
    private final int MOD = 1_000_000_007;
    private final int INF = 2147483647;
    private final long LINF = 9223372036854775807L;
    private long[] fac;
    private long[] finv;
    private long[] inv;

    // Scanner
    int ni(){ return scan.nextInt();}
    int[] ni(int n){int[] a = new int[n]; for(int i = 0; i < n; i++){a[i] = ni();} return a;}
    int[][] ni(int y, int x){int[][] a = new int[y][x];
        for(int i = 0; i < y; i++){for(int j = 0; j < x; j++){a[i][j] = ni();}} return a;}
    long nl(){return scan.nextLong();}
    long[] nl(int n){long[] a = new long[n]; for(int i = 0; i < n; i++){a[i] = nl();} return a;}
    long[][] nl(int y, int x){long[][] a = new long[y][x];
        for(int i = 0; i < y; i++){for(int j = 0; j < x; j++){a[i][j] = nl();}} return a;}
    String ns(){return scan.next();}
    String[] ns(int n){String[] a = new String[n]; for(int i = 0; i < n; i++){a[i] = ns();} return a;}
    String[][] ns(int y, int x){String[][] a = new String[y][x];
        for(int i = 0; i < y; i++){for(int j = 0; j < x; j++){a[i][j] = ns();}} return a;}

    // Mathematics
    int max(int a, int b){return Math.max(a, b);}
    long max(long a, long b){return Math.max(a, b);}
    double max(double a, double b){return Math.max(a, b);}
    int max(int[] a){int max = a[0]; for(int value:a){max = max(max,value);} return max;}
    long max(long[] a){long max = a[0]; for(long value:a){max = max(max,value);} return max;}
    double max(double[] a){double max = a[0]; for(double value:a){max = max(max,value);} return max;}
    int min(int a, int b){return Math.min(a, b);}
    long min(long a, long b){return Math.min(a, b);}
    double min(double a, double b){return Math.min(a, b);}
    int min(int[] a){int min = a[0]; for(int value:a){min = min(min,value);} return min;}
    long min(long[] a){long min = a[0]; for(long value:a){min = min(min,value);} return min;}
    double min(double[] a){double min = a[0]; for(double value:a){min = min(min,value);} return min;}
    long sum(int[] a){long sum = 0; for(int value:a){sum += value;} return sum;}
    long sum(long[] a){long sum = 0; for(long value:a){sum += value;} return sum;}
    double sum(double[] a){double sum = 0; for(double value:a){sum += value;} return sum;}
    int gcd(int a, int b){return b == 0 ? a : gcd(b, a % b);}
    long gcd(long a, long b){return b == 0 ? a : gcd(b, a % b);}
    int lcm(int a, int b){return a / gcd(a, b) * b;}
    long lcm(long a, long b){return a / gcd(a, b) * b;}
    long fact(int n){ if(n == 0){ return 1; } long a = n; for(long i = n - 1; i >= 2; i--){ a = a % MOD * i; } return a; }
    long fact(long n){ if(n == 0){ return 1; } long a = n; for(long i = n - 1; i >= 2; i--){ a = a % MOD * i; } return a; }
    // nPr(int)
    long npr(int n, int r){
        long a = 1;
        for(int i = n; i > n - r; i--){
            a *= i;
        }
        return a;
    }
    // nPr(long)
    long npr(long n, long r){
        long a = 1;
        for(long i = n; i > n - r; i--){
            a *= i;
        }
        return a;
    }
    // 素数判定(int)
    boolean checkPrime(int n){
        for(int i = 2; i * i <= n; i++){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }
    // 素数判定(long)
    boolean checkPrime(long n){
        for(long i = 2; i * i <= n; i++){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }
    // エラトステネスの篩
    List<Integer> erathos(int n){
        boolean[] flag = new boolean[n + 1];
        List<Integer> nums = new ArrayList<>();
        for(int i = 2; i <= n; i++){
            if(flag[i]) continue;
            nums.add(i);
            for(int j = i * 2; j <= n; j += i){
                flag[j] = true;
            }
        }

        return nums;
    }

    // mod pにおける累乗 a^n
    long modpow(long a, long n, long p){
        long res = 1;
        while(n > 0){
            if((n & 1) == 1){
                res = res * a % p;
            }
            a = a * a % p;
            n = n >> 1;
        }
        return res;
    }
    // mod pにおけるaの逆元a^-1
    long modinv(long a, long p){
        return modpow(a, p - 2, p);
    }
    // fac,finv,invの初期化
    void comInit(int max){
        fac = new long[max];
        finv = new long[max];
        inv = new long[max];
        fac[0] = fac[1] = 1;
        finv[0] = finv[1] = 1;
        inv[1] = 1;
        for(int i = 2; i < max; i++){
            fac[i] = fac[i - 1] * i % MOD;
            inv[i] = MOD - inv[MOD % i] * (MOD / i) % MOD;
            finv[i] = finv[i - 1] * inv[i] % MOD;
        }
    }
    // 二項係数nCr
    long com(int n, int r){
        if(n < r || (n < 0 || r < 0)){
            return 0;
        }
        return fac[n] * (finv[r] * finv[n - r] % MOD) % MOD;
    }
    // 二項係数nCr（nが10^9など巨大なとき用）
    long ncr(long n, long k){
        long a = 1;
        long b = 1;
        for(int i = 1; i <= k; i++){
            a = a * (n + 1 - i) % MOD;
            b = b * i % MOD;
        }
        
        return modinv(b, MOD) * a % MOD;
    }
    // 二次元上の二点間の距離
    double distance(double x1, double y1, double x2, double y2){
        double dist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return dist;
    }
    // 三次元上の二点間の距離
    double distance(double x1, double y1, double z1, double x2, double y2, double z2){
        double dist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        return dist;
    }


    // Array
    void sort(int[] a){ Arrays.sort(a);}
    void sort(long[] a){ Arrays.sort(a);}
    void sort(double[] a){ Arrays.sort(a);}
    void sort(String[] a){ Arrays.sort(a);}
    int[] reverse(int[] a){
        int[] reversed = new int[a.length];
        for(int i = 0; i < a.length; i++){
            reversed[a.length - i - 1] = a[i];
        }
        return reversed;
    }
    long[] reverse(long[] a){
        long[] reversed = new long[a.length];
        for(int i = 0; i < a.length; i++){
            reversed[a.length - i - 1] = a[i];
        }
        return reversed;
    }
    double[] reverse(double[] a){
        double[] reversed = new double[a.length];
        for(int i = 0; i < a.length; i++){
            reversed[a.length - i - 1] = a[i];
        }
        return reversed;
    }
    char[] reverse(char[] a){
        char[] reversed = new char[a.length];
        for(int i = 0; i < a.length; i++){
            reversed[a.length - i - 1] = a[i];
        }
        return reversed;
    }
    String[] reverse(String[] a){
        String[] reversed = new String[a.length];
        for(int i = 0; i < a.length; i++){
            reversed[a.length - i - 1] = a[i];
        }
        return reversed;
    }
    boolean[] reverse(boolean[] a){
        boolean[] reversed = new boolean[a.length];
        for(int i = 0; i < a.length; i++){
            reversed[a.length - i - 1] = a[i];
        }
        return reversed;
    }
    void fill(int[] array, int x) { Arrays.fill(array, x); }
    void fill(long[] array, long x) { Arrays.fill(array, x); }
    void fill(double[] array, double x) { Arrays.fill(array, x); }
    void fill(boolean[] array, boolean x) { Arrays.fill(array, x); }
    void fill(int[][] array, int x) { for(int a[] : array) { fill(a, x); } }
    void fill(long[][] array, long x) { for(long a[] : array) { fill(a, x); } }
    void fill(double[][] array, double x) { for(double a[] : array) { fill(a, x); } }
    void fill(boolean[][] array, boolean x) { for(boolean a[] : array) { fill(a, x); } }
    void fill(int[][][] array, int x) { for(int[][] ary : array) { for(int[] a : ary){ fill(a, x); } } }
    void fill(long[][][] array, long x) { for(long[][] ary : array) { for(long[] a : ary){ fill(a, x); } } }

    // Algorithm
    // 深さ優先探索
    void dfs(Node[] nodes, int v, boolean[] seen){
        seen[v] = true;
        
        for(Edge edge : nodes[v].edges){
            if(seen[edge.to]) continue;
            dfs(nodes, edge.to, seen);
        }

    }
    // 幅優先探索
    long[] bfs(Node[] nodes, int start){
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        long[] dist = new long[nodes.length];
        fill(dist, -1);
        dist[start] = 0;

        while(!queue.isEmpty()){
            int now = queue.poll();
            for(Edge edge : nodes[now].edges){
                if(dist[edge.to] != -1) continue;
                dist[edge.to] = dist[now] + 1;
                queue.add(edge.to);
            }
        }

        return dist;
    }
    
    // ダイクストラ法
    long[] dijkstra(Node[] nodes, int start){
        Queue<Edge> queue = new PriorityQueue<>();
        long[] dist = new long[nodes.length];
        fill(dist, LINF / 2);
        dist[start] = 0;
        queue.add(new Edge(start, start, dist[start]));

        while(!queue.isEmpty()){
            Edge now = queue.poll();        
            if(dist[now.to] < now.cost) continue;
            for(Edge edge : nodes[now.to].edges){
                if(dist[edge.to] > dist[edge.from] + edge.cost){
                    dist[edge.to] = dist[edge.from] + edge.cost;
                    queue.add(new Edge(edge.from, edge.to, dist[edge.to]));
                    nodes[edge.to].past = edge.from;
                }
            }
        }

        return dist;
    }
    
    // ソート済みint型配列でkey以上の値の最小indexを返す
    int lowerBound(int[] a, int key){
        int ng = -1;
        int ok = a.length;
        while(Math.abs(ok - ng) > 1){
            int mid = (ok + ng) / 2; 
            if(a[mid] >= key){
                ok = mid;
            } else {
                ng = mid;
            }
        }
        return ok;
    }
    // ソート済みlong型配列でkey以上の値の最小indexを返す
    int lowerBound(long[] a, long key){
        int ng = -1;
        int ok = a.length;
        while(Math.abs(ok - ng) > 1){
            int mid = (ok + ng) / 2; 
            if(a[mid] >= key){
                ok = mid;
            } else {
                ng = mid;
            }
        }
        return ok;
    }
    // 文字列sとtの最長共通部分列の長さを返す
    int lcs(String s , String t){
        int[][] dp = new int[s.length() + 1][t.length() + 1];

        for(int i = 0; i < s.length(); i++){
            for(int j = 0; j < t.length(); j++){
                if(s.charAt(i) == t.charAt(j)){
                    dp[i + 1][j + 1] = max(dp[i][j] + 1, dp[i + 1][j + 1]);
                }
                dp[i + 1][j + 1] = max(dp[i + 1][j + 1], dp[i + 1][j]);
                dp[i + 1][j + 1] = max(dp[i + 1][j + 1], dp[i][j + 1]);
            }
        }

        return dp[s.length()][t.length()];
    }

}

// ノード
class Node{
    int id;             // ノード番号
    int past;           // 直前の頂点
    List<Edge> edges;   // 辺のリスト

    Node(int id, int past){
        this.id = id;
        this.past = past;
        this.edges = new ArrayList<>();
    }
    void addEdge(Edge edge){
        edges.add(edge);
    }

    public boolean equals(Object obj){
        if(obj instanceof Node){
            Node node = (Node)obj;
            return this.id == node.id;
        } 
        return false;
    }
    public int hashCode(){
        return id;
    }
    public String toString(){
        return "[id:" + id + " past: " + past + "]";
    }
}

// 辺の情報を持つクラス
class Edge implements Comparable<Edge>{
    int from;       // どの頂点から
    int to;          //  どの頂点へ
    long cost;       // 辺の重み

    Edge(int from, int to, long cost){
        this.from = from;
        this.to = to;
        this.cost = cost;
    }
    public int compareTo(Edge edge){
        return Long.compare(this.cost, edge.cost);
    }

    public String toString(){
        return "[" + from + " to " + to + " cost:" + cost + "]";
    }
}
// 辺の重み比較のComparator
class EdgeComparator implements Comparator<Edge>{
    @Override
    public int compare(Edge e1, Edge e2){
        long cost1 = e1.cost;
        long cost2 = e2.cost;
        if(cost1 < cost2){
            return -1;
        } else if(cost1 > cost2){
            return 1;
        } else {
            return 0;
        }
    }
}
// Union-Find
class UnionFind{
    int[] par;
    int[] size;
    UnionFind(int N){
        par = new int[N];
        size = new int[N];
        for(int i = 0; i < N; i++){
            par[i] = i;
            size[i] = 1;
        }
    }

    void init(int N){
        for(int i = 0; i < N; i++){
            par[i] = i;
            size[i] = 1;
        }
    }

    int root(int x){
        if(par[x] == x){
            return x;
        } else {
            return par[x] = root(par[x]);
        }
    }

    boolean same(int x, int y){
        return root(x) == root(y);
    }

    void unite(int x, int y){
        x = root(x);
        y = root(y);
        if(x == y) return;
        if(size[x] < size[y]){
            int tmp = x;
            x = y;
            y = tmp;
        }
        size[x] += size[y];
        par[y] = x;
    }

    int size(int x){
        return size[root(x)];
    }

}

// 順列を管理する
class Permutation {
    private int number;
    private int listSize;
    private int searched;
    private int nextIndex;
    private int[][] permList;

    Permutation(int num) {
        this.number = num;
        this.listSize  = this.fact(this.number);
        this.searched   = 0;
        this.nextIndex = 0;
        this.permList  = new int[this.listSize][this.number];

        this.create(0, new int[this.number], new boolean[this.number]);
    }

    int[] nextPerm() {
        return permList[this.nextIndex++];
    }

    boolean isNext() {
        if(this.nextIndex < this.listSize) {
            return true;
        } else {
            this.nextIndex = 0;
            return false;
        }
    }

    int fact(int n){
        return n == 0 ? 1 : n * fact(n-1);
    }

    void create(int num, int[] list, boolean[] flag) {
        if(num == this.number) {
            copyArray(list, permList[this.searched]);
            this.searched++;
        }
        for(int i = 0; i < this.number; i++){
            if(flag[i]) continue;
            list[num] = i;
            flag[i] = true;
            this.create(num+1, list, flag);
            flag[i] = false;

        }
    }

    void copyArray(int[] from, int[] to) {
        for(int i=0; i<from.length; i++) to[i] = from[i];
    }

    void printNum(int[] nums) {
        for(int n : nums) System.out.print(n);
        System.out.println();
    }
}

// 一点更新区間取得Segment Tree
class SegTree{
    int[] dat;
    int size;
    
    SegTree(int N){
        this.dat = new int[N * 4];
        this.size = N;
    }

    // [a, b]の最小値を返す
    // l,rにはノードkに対応する区間を与える
    int query(int a, int b, int k, int l, int r){
        if(r < a || b < l) return Integer.MAX_VALUE;
        if(a <= l && r <= b) return dat[k];
        int vl = query(a, b, k << 1, l, (l + r) / 2);
        int vr = query(a, b, (k << 1) + 1, (l + r) / 2 + 1, r);
        return Math.min(vl, vr);
    }

    // インデックスiの値をxに更新
    void update(int i, int x){
        i += this.size - 1;
        dat[i] = x;
        while(i > 0){
            i = i >> 1;     // 1ビットぶん右シフト
            dat[i] = Math.min(dat[i << 1], dat[(i << 1) + 1]);
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for(int i = size; i < 2 * size; i++){
            sb.append(dat[i]).append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        String output = sb.append("]").toString();
        return output;
    }
}

// Range Sum Queryを実現するBinary Indexed Tree
class BIT{
    int[] tree;

    BIT(int N){
        this.tree = new int[N + 1];
    }

    int sum(int i){
        int sum = 0;
        while(i > 0){
            sum += this.tree[i];
            i -= i & -i;
        }
        return sum;
    }

    void add(int i, int x){
        while(i <= tree.length){
            this.tree[i] += x;
            i += i & -i;
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < tree.length; i++){
            sb.append(tree[i] + ",");
        }
        sb.append("]");
        String output = sb.toString();
        return output;
    }
    
}

// 標準のScannerより高速に標準入力する
class FastScanner {
    private final InputStream in = System.in;
    private final byte[] buffer = new byte[1024];
    private int ptr = 0;
    private int buflen = 0;
    private boolean hasNextByte() {
        if (ptr < buflen) {
            return true;
        }else{
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
    private int readByte() { if (hasNextByte()) return buffer[ptr++]; else return -1;}
    private static boolean isPrintableChar(int c) { return 33 <= c && c <= 126;}
    public boolean hasNext() { while(hasNextByte() && !isPrintableChar(buffer[ptr])) ptr++; return hasNextByte();}
    public String next() {
        if (!hasNext()) throw new NoSuchElementException();
        StringBuilder sb = new StringBuilder();
        int b = readByte();
        while(isPrintableChar(b)) {
            sb.appendCodePoint(b);
            b = readByte();
        }
        return sb.toString();
    }
    public long nextLong() {
        if (!hasNext()) throw new NoSuchElementException();
        long n = 0;
        boolean minus = false;
        int b = readByte();
        if (b == '-') {
            minus = true;
            b = readByte();
        }
        if (b < '0' || '9' < b) {
            throw new NumberFormatException();
        }
        while(true){
            if ('0' <= b && b <= '9') {
                n *= 10;
                n += b - '0';
            }else if(b == -1 || !isPrintableChar(b)){
                return minus ? -n : n;
            }else{
                throw new NumberFormatException();
            }
            b = readByte();
        }
    }
    public int nextInt() {
        long nl = nextLong();
        if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE) throw new NumberFormatException();
        return (int) nl;
    }
    public double nextDouble() { return Double.parseDouble(next());}
}
