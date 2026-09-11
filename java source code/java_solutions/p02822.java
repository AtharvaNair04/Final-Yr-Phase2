import java.util.*;
import java.io.*;

	
public class Main {
    
    static class Graph0n {
        private ArrayList<Node0n> dt = new ArrayList<>();
        Graph0n(int sz){
	   	    for(int i=0; i<sz; i++){
	   	        Node0n node1 = new Node0n();
	   	        dt.add(node1);
	   	    }
        }
        public void add(int vn, int val){       dt.get(vn).add(val);                }
        public int get(int vn, int index){      return dt.get(vn).get(index);       }
        public ArrayList<Integer> get(int vn){  return dt.get(vn).getAll();         }
        public int sizeOf(int vn){              return dt.get(vn).size();           }
        public void clear(){    for(int i=0; i<dt.size(); i++){ dt.get(i).clear();  }   }
    }
    
    static class Node0n {   //重みなし無向グラフの頂点
        private ArrayList<Integer> next_vs = new ArrayList<>();
        
        public void add(int val){               next_vs.add(val);       }
        public int get(int ad){                 return next_vs.get(ad); }
        public ArrayList<Integer> getAll(){     return next_vs;         }
        public int size(){                      return next_vs.size();  }
        public void clear(){                    next_vs.clear();        }
    }

    static class Edge {
        int from=-1, v2=-1;     long weight;
        public Edge(int vn, long w){
            this.v2 = vn;
            this.weight = w;
        }
        public Edge(int cm, int vn, long w){
            this.from = cm;
            this.v2 = vn;
            this.weight = w;
        }
    }
    static class Edge2 {
        int v2;     long cost1,cost2;
        public Edge2(int vn, long w1, long w2){
            this.v2 = vn;
            this.cost1 = w1;
            this.cost2 = w2;
        }
    }

    static class Comparator_Edge implements Comparator<Edge>{
        public int compare(Edge a, Edge b){
            if(a.weight>b.weight) return 1;
            else if(a.weight<b.weight) return -1;
            else return 0;
        }
    }
    
	public static void main(String[] args) throws Exception {
		FastScanner sc = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		
		int n = sc.nexI();
		
		Graph0n tree = new Graph0n(n);
		
		int s1=0,g1=0;
		for(int i=1; i<n; i++){
		    s1=sc.nexI()-1;
		    g1=sc.nexI()-1;
		    tree.add(s1,g1);
		    tree.add(g1,s1);
		}
		
		Graph0n e_on_each_dir = new Graph0n(n);
		
		boolean[] is_known = new boolean[n];
		fill(is_known, false);
		
		int[] sum_former = new int[n];
		fill(sum_former,0);
		
		ArrayDeque<Integer> todo1 = new ArrayDeque<>();
		
		int next=-1;
		for(int i=0; i<n; i++){
		    if(tree.sizeOf(i)==1){
		        next=tree.get(i,0);
		        e_on_each_dir.add(i,n-1);
		        e_on_each_dir.add(next,1);
		        sum_former[next]++;
		        is_known[i] = true;
		        todo1.add(next);
		    }
		}   //O(n)
		
		while(todo1.size()>0){
		    int dw = todo1.poll();
		    if(is_known[dw]) continue;
		    
		    next=-1;
		    for(int a : tree.get(dw)){
		        if(!is_known[a]){
		            if(next<0){
		                next=a;
		            }else{
		                next=INF;
		                break;
		            }
		        }
		    }
		    if(next<0){
		        is_known[dw]=true;
		    }else if(!isINF(next)){
    		    sum_former[dw]++;
    		    e_on_each_dir.add(dw, n-sum_former[dw]);
    		    e_on_each_dir.add(next,sum_former[dw]);
    		    sum_former[next] += sum_former[dw];
    		    is_known[dw]=true;
    		    todo1.add(next);
		    }
		}   //O(n+a)くらい？
		
		/*for(int i=0; i<n; i++){
		    out.print(i+1);
		    out.print(":");
		    for(int a: e_on_each_dir.get(i)){
		        out.print(" "+a);
		    }
		    out.println();
		}*/
		
		int[] mod2pow = new int[n+1];
		mod2pow[0]=1;
		for(int i=1; i<=n; i++){
		    mod2pow[i] = mod2pow[i-1]*2;
		    mod2pow[i] %= e97;
		}
		
		long ans_2n = 0;
		
		for(int i=0; i<n; i++){
		    if(e_on_each_dir.sizeOf(i)==1) continue;
		    ans_2n += mod2pow[n-1];
		    for(int a: e_on_each_dir.get(i)){
		        ans_2n -= mod2pow[a];
		    }
		    ans_2n += (e_on_each_dir.sizeOf(i)-1);
		    ans_2n %= e97;
		}
		//out.println(ans_2n);
		
		long x1 = modinv(mod2pow[n],e97);
		x1 *= ans_2n;
		x1 %= e97;
		if(n==2) x1=0;
		if(x1<0) x1+=e97;
		out.println(x1);
		
		out.flush();
	}
	
	
	private static int INF = (int)1e8;
	private static long INFL = (long)1e17;
	private static long e97 = (long)1e9 + 7;
	
	
	private static int abs(int a){  return (a>=0) ? a: -a;  }
	private static long abs(long a){    return (a>=0) ? a: -a;  }
	
	private static int min(int a, int b){   return (a>b) ? b : a;   }
	private static long min(long a, long b){    return (a>b) ? b : a;   }
	
	private static int max(int a, int b){   return (a>b) ? a : b;   }
	private static long max(long a, long b){    return (a>b) ? a : b;   }
	
	private static int minN(int... ins){
		int min = ins[0];
		for(int i=1; i<ins.length; i++){    if(ins[i] < min) min = ins[i];  }
		return min;
	}
	private static int maxN(int... ins){
		int max = ins[0];
		for(int i=1; i<ins.length; i++){    if(ins[i] > max) max = ins[i];  }
		return max;
	}
	private static long minN(long... ins){
		long min = ins[0];
		for(int i=1; i<ins.length; i++){    if(ins[i] < min) min = ins[i];  }
		return min;
	}
	private static long maxN(long... ins){
		long max = ins[0];
		for(int i=1; i<ins.length; i++){    if(ins[i] > max) max = ins[i];  }
		return max;
	}
	
	private static int minExAd(int[] dt, int ad){
	    int min=Integer.MAX_VALUE;
		for(int i=0; i<dt.length; i++){ if((i != ad) && (dt[i] < min)) min = dt[i]; }
		return min;
	}
	private static long minExAd(long[] dt, int ad){
	    long min=Long.MAX_VALUE;
		for(int i=0; i<dt.length; i++){ if((i != ad) && (dt[i] < min)) min = dt[i]; }
		return min;
	}
	private static int minExVal(int[] dt, int ex_val){
	    int min=Integer.MAX_VALUE;
		for(int i=0; i<dt.length; i++){ if((dt[i] != ex_val) && (dt[i] < min)) min = dt[i]; }
		return min;
	}
	private static long minExVal(long[] dt, long ex_val){
	    long min=Long.MAX_VALUE;
		for(int i=0; i<dt.length; i++){ if((dt[i] != ex_val) && (dt[i] < min)) min = dt[i]; }
		return min;
	}
	
	private static boolean same3(long a, long b, long c){
    	if(a!=b) return false;
    	if(b!=c) return false;
    	if(c!=a) return false;
    	return true;
	}
	private static boolean dif3(long a, long b, long c){
    	if(a==b) return false;
    	if(b==c) return false;
    	if(c==a) return false;
    	return true;
	}
	
	private static int factorial(int n) {
		int ans=1;
		for(int i=n; i>0; i--){ ans*=i; }
		return ans;
	}
	
	private static long gcd(long m, long n) {
		if(m < n) return gcd(n, m);
		if(n == 0) return m;
		return gcd(n, m % n);
	}
	
	private static boolean is_prime(long a){
	    if(a==1) return false;
	    for(int i=2; i<=Math.sqrt(a); i++){  if(a%i == 0) return false;  }
	    return true;
	}
	
    private static long modinv(long a, long m) {    //a|m, >1に注意
        long b = m, u = 1, v = 0;
        while (b>0) {
            long t = a / b;
            long pe = a%b;
            a=b;    b=pe;
            pe= u-t*v;
            u=v;    v=pe;
        }
        u %= m; 
        if (u < 0) u += m;
        return u;
    }
    
    private static long[] fac10E97 = null;
    private static long[] finv10E97 = null;
    private static void Cinit(int max_sz, long p){
        fac10E97 = new long[max_sz+1];
        finv10E97 = new long[max_sz+1];
        fac10E97[0]=1;
        finv10E97[0]=1;
        for(int i=1; i<=max_sz; i++){
            fac10E97[i] = (fac10E97[i-1]*i) % p;
        }
        finv10E97[max_sz] = modinv(fac10E97[max_sz], p);
		for(int i=max_sz; i>1; i--){
		    finv10E97[i-1] = (finv10E97[i]*i) % p;
		}
    }
    
    private static long C10e97(int n, int k, long p){
        long ans = fac10E97[n];
        ans *= finv10E97[k];
        ans %= p;
        ans *= finv10E97[n-k];
        ans %=p;
        if(ans<0) return ans+p;
        else return ans;
    }
	
	private static int pow2(int in){            return in*in;   }
	private static long pow2(long in){          return in*in;   }
	private static double pow2(double in){      return in*in;   }
	
	private static int getDigit2(long num){
		long cf = 1;    int d=0;
		while(num >= cf){   d++;    cf = 1<<d;  }
		return d; //numはd桁の数で、2^dより小さい
	}
	
	private static int getDigit10(long num){
		long cf = 1;    int d=0;
		while(num >= cf){   d++;    cf*=10;     }
		return d; //numはd桁の数で、10^dより小さい
	}
	
	private static boolean isINF(int in){
    	if(((long)in*20)>INF) return true;
    	else return false;
	}
	private static boolean isINFL(long in){
    	if((in*10000)>INFL) return true;
    	else return false;
	}
	
	private static long pow10E97(long ob, long soeji, long p){
	    if(ob==0) return 0;
	    if(soeji==0) return 1;
		if(soeji==2) return (ob*ob)%p;
		
		int d = getDigit2(soeji);
		long[] ob_pow_2pow = new long[d];
		
		ob_pow_2pow[0] = ob;
		for(int i=1; i<d; i++){ ob_pow_2pow[i] = (ob_pow_2pow[i-1]*ob_pow_2pow[i-1])%p;   }
		
		long ans=1;
		for(int i=d-1; i>=0; i--){
			if(soeji >= (long)(1<<i)){
				soeji -= (long)(1<<i);
				ans = (ans*ob_pow_2pow[i])%p;
			}
 		}
		return ans;
	}
	
	
	private static int flag(int pos){               return (1<<pos);        }
	private static boolean isFlaged(int bit, int pos){
    	if((bit&(1<<pos)) > 0) return true;
    	else return false;
	}
	private static int deflag(int bit, int pos){    return bit&~(1<<pos);   }
	
	private static void showflag(int bit){
    	for(int i=0; i<getDigit2(bit); i++){
        	if(isFlaged(bit,i)) System.out.print("O");
        	else System.out.print(".");
    	}
	}
	
	public static int biSearch(int[] dt, int target){
		int left=0, right=dt.length-1;
		int mid=-1;
		while(left<=right){
			mid = (right+left)/2;
			if(dt[mid] == target) return mid;
			if(dt[mid] < target) left=mid+1;
			else right=mid-1;
		}
		return -1;
	}
	public static int biSearchMax(long[] dt, long target){
		int left=-1, right=dt.length;
		int mid=-1;
		
		while((right-left)>1){
			mid = left + (right-left)/2;
			if(dt[mid] < target) left=mid;
			else right=mid;
		}
		return left;//target未満の最大のaddress
	}
	public static int biSearchMaxAL(ArrayList<Integer> dt, long target){
		int left=-1, right=dt.size();
		int mid=-1;
		
		while((right-left)>1){
			mid = left + (right-left)/2;
			if(dt.get(mid) < target) left=mid;
			else right=mid;
		}
		return left;//target未満の最大のaddress
	}
	
    private static int get_root_uf(int[] parent, int index){
        if(parent[index] == index) return index;
        int root = get_root_uf(parent, parent[index]);
        parent[index] = root;
        return root;
    }
    
    private static boolean is_same_uf(int[] parent, int x, int y){
        if(get_root_uf(parent,x) == get_root_uf(parent,y)) return true;
        else return false;
    }
    
    private static void unite_uf(int[] parent, int receiver, int attacker){
        parent[get_root_uf(parent,attacker)] = get_root_uf(parent, receiver);
    }
	
    private static int areaSum(int[][] acum_plus1, int bY_notInclude, int aY_include, int bX_notInclude, int aX_include){
        int ans = acum_plus1[aY_include+1][aX_include+1];
        ans -= acum_plus1[aY_include+1][bX_notInclude+1];
        ans -= acum_plus1[bY_notInclude+1][aX_include+1];
        ans += acum_plus1[bY_notInclude+1][bX_notInclude+1];
        return ans;
    }
    
	private static void nextPerm(ArrayList<Integer> former){
		int pivot=-1;
		int ln = former.size();
		for(int i = ln; i>1; i--){
			if(former.get(i-2) < former.get(i-1)){
				pivot = i-2;
				break;
			}
		}
		if(pivot==-1){
    		former = null;
    		return;
		}
		int pivot_fig = former.get(pivot);
		
		int min_pos=pivot+1;
		int min=former.get(min_pos);
		for(int i=ln-1; i > pivot+1; i--){
			if((former.get(i) < min)&&(former.get(i) > pivot_fig)){
				min_pos=i;
				min=former.get(min_pos);
			}
		}
		Collections.swap(former, pivot, min_pos);
		Collections.sort(former.subList(pivot+1, ln));
	}
    private static ArrayList<boolean[]> combi_enum(int ln, boolean[] former, int now_pos, int rest){
        ArrayList<boolean[]> ans = new ArrayList<>();
        boolean[] ans1 = new boolean[former.length+1];
	   	for(int i=0; i<former.length; i++){
	   	    ans1[i] = former[i];
	   	}
        if(ln==(now_pos+1)){
            switch(rest){
                case 0:
                    ans1[former.length] = false;
                    ans.add(ans1);
                    break;
                case 1:
                    ans1[former.length] = true;
                    ans.add(ans1);
                    break;
            }
        }else{
            if(rest == 0){
                ans1[former.length] = false;
                ans.addAll(combi_enum(ln, ans1, now_pos+1, rest));
            }else{
                ans1[former.length] = false;
                ans.addAll(combi_enum(ln, ans1.clone(), now_pos+1, rest));
                ans1[former.length] = true;
                ans.addAll(combi_enum(ln, ans1.clone(), now_pos+1, rest-1));
            }
        }
        return ans;
    }
	
	private static int dfs2(boolean[][] state, int x, int y, int depth){		//薄氷渡り
		int[] tmpltX = {1,-1,0,0};
		int[] tmpltY = {0,0,1,-1};
		int newX = -1, newY = -1;
		
		state[x][y] = false;
		depth++;
		
		int maxDunder_this_brunch = depth, tmpD=0;
		for(int i=0; i < 4; i++){
			newX = x+tmpltX[i];
			newY = y+tmpltY[i];
			if((newX >= 0) && (newY >= 0) && (newX < state.length) && (newY < state[0].length) && state[newX][newY]) tmpD = dfs2(state, newX, newY, depth);
			if(tmpD > maxDunder_this_brunch) maxDunder_this_brunch=tmpD;
		}
		state[x][y] = true;
		return maxDunder_this_brunch;
	}
	
	
	static void show_b2(boolean[][] dt, int lit_x, int lit_y){
		PrintWriter out = new PrintWriter(System.out);
		for(int i=0; i<dt[0].length; i++){
			for(int j=0; j<dt.length; j++){
				if((i==lit_y) && (j==lit_x)) out.print("X");
				else if(dt[j][i]) out.print("O");
				else out.print(".");
			}
			out.println();
		}
		out.flush();
	}
	static void show_L2(long[][] dt, String cmnt){
		PrintWriter out = new PrintWriter(System.out);
		for(int i=0; i<dt[0].length; i++){
			for(int j=0; j<dt.length; j++){
				out.print(dt[j][i]+",");
			}
			out.println("<-"+cmnt+":"+i);
		}
		out.flush();
	}
	static void disp_que(ArrayDeque<Integer> dt){		//上手くいかなかった時用
		int a=0;
		while(dt.size()>0){
			a=dt.removeLast();
			System.out.print(a);
		}
		System.out.println("\n");
	}
	
	
	
	
	private static void fill(boolean[] ob, boolean res){
		for(int i=0; i<ob.length; i++){ ob[i] = res;    }
	}
	private static void fill(int[] ob, int res){
		for(int i=0; i<ob.length; i++){ ob[i] = res;    }
	}
	private static void fill(long[] ob, long res){
		for(int i=0; i<ob.length; i++){ ob[i] = res;    }
	}
	private static void fill(char[] ob, char res){
		for(int i=0; i<ob.length; i++){ ob[i] = res;    }
	}
	private static void fill(boolean[][] ob, boolean res){
		for(int i=0; i<ob.length; i++){ for(int j=0; j<ob[0].length; j++){  ob[i][j] = res; }   }
	}
	private static void fill(int[][] ob, int res){
		for(int i=0; i<ob.length; i++){ for(int j=0; j<ob[0].length; j++){  ob[i][j] = res; }   }
	}
	private static void fill(long[][] ob, long res){
		for(int i=0; i<ob.length; i++){ for(int j=0; j<ob[0].length; j++){  ob[i][j] = res; }   }
	}
	private static void fill(char[][] ob, char res){
		for(int i=0; i<ob.length; i++){ for(int j=0; j<ob[0].length; j++){  ob[i][j] = res; }   }
	}
	private static void fill(int[][][] ob, int res){
		for(int i=0; i<ob.length; i++){	for(int j=0; j<ob[0].length; j++){
				for(int k=0; k<ob[0][0].length; k++){   ob[i][j][k] = res;  }   }   }
	}
	private static void fill(long[][][][] ob, long res){
		for(int i=0; i<ob.length; i++){	for(int j=0; j<ob[0].length; j++){
				for(int k=0; k<ob[0][0].length; k++){	for(int l=0; l<ob[0][0][0].length; l++){
    				    ob[i][j][k][l]=res; }   }   }   }
	}
	
    
static class FastScanner {
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
	public long nexL() {
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
	public int nexI() {
		long nl = nexL();
		if (nl < Integer.MIN_VALUE || nl > Integer.MAX_VALUE) throw new NumberFormatException();
		return (int) nl;
	}
	public double nextDouble() { return Double.parseDouble(next());}
	
	public void ni(long[] array2){
	   	for(int i=0; i<array2.length; i++){
	   	    array2[i] = nexL();
	   	}
	   	return;
	}
	public void ni(int[] array2){
	   	for(int i=0; i<array2.length; i++){
	   	    array2[i] = nexI();
	   	}
	   	return;
	}
}
}
