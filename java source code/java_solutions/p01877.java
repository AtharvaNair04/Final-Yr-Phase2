import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
  
  
public class Main {
	
	public static class StarrySkyTree{
		private static final long M_INF = Long.MIN_VALUE / 2 + 1;
		private static final long   INF = Long.MAX_VALUE / 2 - 1;
		
		private static final long DEFAULT = 0;
		
		int n;
		long[] add;
		long[] min;
		long[] max;
		
		public StarrySkyTree(int n_){
			this.n = 1;
			while(n < n_){ this.n *= 2; }
		
			add = new long[this.n * 2 - 1];
			min = new long[this.n * 2 - 1];
			max = new long[this.n * 2 - 1];

			for(int i = 0; i < this.n * 2 - 1; i++){
				add[i] = DEFAULT;
				min[i] = DEFAULT;
				max[i] = DEFAULT;
			}
		}
		
		public void add(long v, int a, int b){
			add(v, a, b, 0, 0, this.n);
		}
		private void add(long v, int a, int b, int k, int l, int r){
			if(r <= a || b <= l){ return; }
			
			if(a <= l && r <= b){
				add[k] += v;
				max[k] += v;
				min[k] += v;
			}else{
				add(v, a, b, k * 2 + 1, l, (l + r) / 2);
				add(v, a, b, k * 2 + 2, (l + r) / 2, r);
				
				max[k] = Math.max(max[k * 2 + 1], max[k * 2 + 2]) + add[k];
				min[k] = Math.min(min[k * 2 + 1], min[k * 2 + 2]) + add[k];
			}
		}
		
		public long max(int a, int b){ return max(a, b, 0, 0, this.n); }
		public long min(int a, int b){ return min(a, b, 0, 0, this.n); }		
		
		private long max(int a, int b, int k, int l, int r){
			if(r <= a || b <= l){ return M_INF; }
			if(a <= l && r <= b){ return max[k]; }
			
			final long left_max  = max(a, b, k * 2 + 1, l, (l + r) / 2);
			final long right_max = max(a, b, k * 2 + 2, (l + r) / 2, r);
			return Math.max(left_max, right_max);
		}
		
		private long min(int a, int b, int k, int l, int r){
			if(r <= a || b <= l){ return INF; }
			if(a <= l && r <= b){ return min[k]; }
			
			final long left_min  = min(a, b, k * 2 + 1, l, (l + r) / 2);
			final long right_min = min(a, b, k * 2 + 2, (l + r) / 2, r);
			return Math.min(left_min, right_min);
		}
		
		
		public long value(int index){
			int k = index + this.n - 1;
			long value = add[k];
			
			while(k > 0){ k = (k - 1) / 2; value += add[k]; }
			return value;
		}
		
		public String toString(int l, int r){
			StringBuilder sb = new StringBuilder();
			for(int i = l; i < r; i++){
				sb.append(i == l ? "" : " ");
				sb.append(value(i));
			}
			return sb.toString();
		}
	}
	
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
          
        final int n = sc.nextInt();
        final int n_half = n / 2;
        
        long[] values = new long[n];
        for(int i = 0; i < n; i++){
        	values[i] = sc.nextLong();
        }
          
        StarrySkyTree tree = new StarrySkyTree(n_half);
        for(int i = 0; i < n; i++){
        	final long v = values[i] * (i < n_half ? 1 : -1);
        	final int l = i < n_half ? i : (n - i) - 1;
        	//System.out.println(l + " " + (l + 1));
        	tree.add(v, l, l + 1);
        	//System.out.println(i + " " + tree.toString(0, n_half));
        }
        
        final int Q = sc.nextInt();
        for(int i = 0; i < Q; i++){
        	final int l = sc.nextInt() - 1;
        	final int r = sc.nextInt() - 1;
        	final long x = sc.nextLong();
        	
        	if(l < n_half && r < n_half){
        		//System.out.println(l + " " + r);
        		tree.add(x, l, r + 1);
        	}else if(l >= n_half){
        		//System.out.println(((n - r) - 1) + " " + ((n - l) - 1));
        		tree.add(-x, (n - r) - 1, (n - l) /* - 1 */);
        	}else{
        		tree.add(x, l, n_half);
        		tree.add(-x, (n - r) - 1, n_half);
        	}
        	
        	//System.out.println(i + " " + tree.toString(0, n_half));
        	//System.out.println(":" + tree.max(0, n_half) + " , " + tree.min(0, n_half));
        	
        	final long max = tree.max(0, n_half);
        	final long min = tree.min(0, n_half);
        	
        	System.out.println((max == 0 && min == 0) ? "1" : "0");
        }
    }
    
    public static class Scanner {
		private BufferedReader br;
		private StringTokenizer tok;
		
		public Scanner(InputStream is){
			br = new BufferedReader(new InputStreamReader(is));
		}
		
		private void getLine(){
			try{
				while(!hasNext()){
					tok = new StringTokenizer(br.readLine());
				}
			} catch (IOException e){
				
			}
		}
		
		private boolean hasNext(){
			return tok != null && tok.hasMoreTokens();
		}
		
		public String next(){
			getLine(); return tok.nextToken();
		}
		
		public int nextInt(){
			return Integer.parseInt(next());
		}
		
		public long nextLong(){
			return Long.parseLong(next());
		}
	}
}
