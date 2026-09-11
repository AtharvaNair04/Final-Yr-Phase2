import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;
import java.math.BigInteger;
 
public class Main implements Runnable {
	
	static int mod = 1000000007;
    static boolean[] s;
	
    public static void main(String[] args) {
    	new Thread(null, new Main(), "", 1024 * 1024 * 1024).start();
    }
    
    public void run() {
       	PrintWriter out = new PrintWriter(System.out);
        FastScanner sc = new FastScanner();
        
        int n = sc.nextInt();
        int m = sc.nextInt();
        
        DGraph g = new DGraph(n*3);
        
        for(int i=0;i<m;i++){
        	int p = sc.nextInt()-1;
        	int q = sc.nextInt()-1;
        	g.addEdge(p,n+q);
        	g.addEdge(n+p, 2*n+q);
        	g.addEdge(2*n+p, q);
        }
        
        int s = sc.nextInt()-1;
        int t = sc.nextInt()-1;
        
        long d = g.distance(s, t);
        
        if(d==-1){
        	out.println(-1);
        }
        else{
        	out.println(d/3);
        }
        
        out.flush();
    }

}

class DGraph {

	NodeList[] nlist; //隣接リスト
	
	public DGraph(int vnum){
		this.nlist = new NodeList[vnum];
		for(int i=0;i<vnum;i++){
			nlist[i] = new NodeList();
		}
	}
	
	//辺の追加（同じ辺を複数回追加すると多重辺になる）
	void addEdge(int n, int m){
		nlist[n].add(m);
	}
	
	//頂点数を返す
	int vnum(){
		return nlist.length;
	}
	
	//辺数を返す
	int edgeNum(){
		int sum = 0;
		for(NodeList l:nlist){
			sum += l.size();
		}
		return sum;
	}
	
	//到達判定
	boolean isNext(int n, int m){
		return nlist[n].contains(m);
	}
	
	//1歩で到達可能な全ての頂点を返す
	NodeList nextList(int n){
		return nlist[n];
	}

	//トポロジカルソート（ans.size()!=vnum()ならDAGでなく、出力に意味はない））
	ArrayList<Integer> topologicalSort(){
		int idig[] = new int[vnum()]; //入次数
		for(int i=0;i<vnum();i++){
			for(int j:nextList(i)){
				idig[j] ++;
			}
		}
		ArrayDeque<Integer> q = new ArrayDeque<>();
		
		for(int i=0;i<vnum();i++){
			if(idig[i]==0){
				q.add(i); //入次数が0ならキューに入れる（DAGなら必ず存在）
			}
		}
		
		ArrayList<Integer> ans = new ArrayList<>();
		
		while(!q.isEmpty()){
			int now = q.pollFirst();
			ans.add(now);
			
			for(int j:nextList(now)){
				idig[j] --;
				if(idig[j]==0){
					q.add(j);
				}
			}
		}
		
		return ans;
	}
	
	//DFSの帰りがけ順のリスト（DAG保証があるならトポロジカルソートの逆順としても使える）
	ArrayList<Integer> backTrack(){
		ArrayList<Integer> bt = new ArrayList<>();
		boolean[] isChecked = new boolean[vnum()];
		
		for(int i=0;i<vnum();i++){
			dfsbt(i,isChecked,bt);
		}
		
		return bt;
	}
	void dfsbt(int i, boolean[] isChecked, ArrayList<Integer> bt){
		if(!isChecked[i]){
			isChecked[i] = true;
			for(int j:nextList(i)){
				dfsbt(j,isChecked,bt);
			}
			bt.add(i);
		}
	}
	
	//逆辺による隣接リストを返す
	NodeList[] reverseList(){
		NodeList[] rlist = new NodeList[vnum()];
		for(int i=0;i<vnum();i++){
			rlist[i] = new NodeList();
		}
		
		for(int i=0;i<vnum();i++){
			for(int j:nlist[i]){
				rlist[j].add(i);
			}
		}
		
		return rlist;
	}
	
	//サイクルを持つか（DAGか）
	boolean hasCycle(){
		//サイクル上の頂点は、topologicalSortのコードのidigが0になるために自身から伸びる辺を辿ることが必要なので最後までqに入らない
		return !(topologicalSort().size() == vnum());
	}
	
	//最短距離をBFSで求める
	int distance(int n, int m){
		ArrayDeque<Integer> q = new ArrayDeque<>();
		int[] d = new int[vnum()];	//nとの距離。未チェックなら-1。
		for(int i=0;i<vnum();i++){
			d[i] = -1;
		}
		
		q.add(n);
		d[n] = 0;
		
		while(!q.isEmpty()){
			int now = q.pollFirst();
			
			for(int i:nextList(now)){
				if(d[i] == -1){
					if(i==m){
						return d[now]+1;
					}
					
					d[i] = d[now]+1;
					q.offerLast(i);
				}
			}
		}
		
		return -1;
	}
	
	class NodeList extends ArrayList<Integer>{
		private static final long serialVersionUID = -4461479015945373120L;
	}
}
class FastScanner {
	private final InputStream in = System.in;
	private final byte[] buffer = new byte[1024];
	private int ptr = 0;
	private int buflen = 0;
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
		if (hasNextByte())
			return buffer[ptr++];
		else
			return -1;
	}
	private static boolean isPrintableChar(int c) {
		return 33 <= c && c <= 126;
	}
	public boolean hasNext() {
		while (hasNextByte() && !isPrintableChar(buffer[ptr]))
			ptr++;
		return hasNextByte();
	}
	public String next() {
		if (!hasNext())
			throw new NoSuchElementException();
		StringBuilder sb = new StringBuilder();
		int b = readByte();
		while (isPrintableChar(b)) {
			sb.appendCodePoint(b);
			b = readByte();
		}
		return sb.toString();
	}
	public long nextLong() {
		if (!hasNext())
			throw new NoSuchElementException();
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
		while (true) {
			if ('0' <= b && b <= '9') {
				n *= 10;
				n += b - '0';
			} else if (b == -1 || !isPrintableChar(b)) {
				return minus ? -n : n;
			} else {
				throw new NumberFormatException();
			}
			b = readByte();
		}
	}
	public int nextInt() {
		long nl = nextLong();
		if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE)
			throw new NumberFormatException();
		return (int) nl;
	}
	public int[] nextintArray(int n){
		int[] a = new int[n];
		for(int i=0;i<n;i++){
			a[i] = nextInt();
		}
		return a;
	}
	public long[] nextlongArray(int n){
		long[] a = new long[n];
		for(int i=0;i<n;i++){
			a[i] = nextLong();
		}
		return a;
	}
	public Integer[] nextIntegerArray(int n){
		Integer[] a = new Integer[n];
		for(int i=0;i<n;i++){
			a[i] = nextInt();
		}
		return a;
	}
	public int[][] nextintMatrix(int h, int w){
		int[][] mat = new int[h][w];
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				mat[i][j] = nextInt();
			}
		}
		return mat;
	}
	public double nextDouble() {
		return Double.parseDouble(next());
	}
}
