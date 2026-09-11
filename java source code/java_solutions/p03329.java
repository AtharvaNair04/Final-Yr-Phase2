import java.util.*;
import java.io.*;
import java.math.*;
public class Main{
	public static PrintWriter out = new PrintWriter (new BufferedOutputStream(System.out));
	public static void main(String sp[])throws IOException{
		Scanner sc = new Scanner(System.in);
		//FastReader sc = new FastReader();	
		
			TreeMap<Integer,Integer> nine = new TreeMap<>();
			int n = sc.nextInt();
			for(int i=1;;i++){
				int val = ((int)Math.pow(9,i));
				if(val>100000)
					break;
				nine.put(val,0);
			}
			for(int i=1;;i++){
				int val = ((int)Math.pow(6,i));
				if(val>100000)
					break;
				nine.put(val,0);
			}
			if(n<6){
				System.out.println(n);
				return;
			}
			int dp[][] = new int[12][n+1];
			for(int i=0;i<12;i++){
				for(int j=0;j<=n;j++)
					dp[i][j] = Integer.MAX_VALUE;
			}
			
			ArrayList<Integer> al = new ArrayList<>(nine.keySet());
			for(int i=0;i<=11;i++){
				dp[i][0]=0;
			}
			dp[0][1] = 1;
			dp[0][2] = 2;
			dp[0][3] = 3;
			dp[0][4] = 4;
			dp[0][5] = 5;
			
			for(int i=1;i<=11;i++){
				int val = al.get(i-1);
				for(int j=1;j<=n;j++){
					if(dp[i-1][j]!=Integer.MAX_VALUE)
						dp[i][j] = dp[i-1][j];
					if(j-val>=0){
						if(dp[i-1][j-val]!=Integer.MAX_VALUE)
							dp[i][j] = Math.min(dp[i-1][j-val]+1, dp[i][j]);
						if(dp[i][j-val]!=Integer.MAX_VALUE)
							dp[i][j] = Math.min(dp[i][j-val]+1, dp[i][j]);
					}
				}
			}
			
			System.out.println(dp[11][n]);
		
		
		
    }
	
	public static ArrayList<Integer> primefactor(int n){
		int sqrt = (int)Math.sqrt(n);
		ArrayList<Integer> al = new ArrayList<>();
		while(n%2==0){
			al.add(2);
			n/=2;
		}
		for(int i=3;i<=sqrt;i+=2){
			while(n%i==0){
				al.add(i);
				n/=i;
			}
		}
		if(n>2)
			al.add(n);
		return al;
	}
	
	public static long sum(long val){
		long fg =0 ;
		while(val!=0){
			fg+=val%10;
			val/=10;
		}
		return fg;
	}
	
	
	
	public static ArrayList<Integer> factor(int n){
		int sqrt = (int)Math.sqrt(n);
		ArrayList<Integer> al = new ArrayList<>();
		for(int i=1;i<=sqrt;i++){
			if(n%i!=0)
				continue;
			int first = i;
			int second = n/i;
			al.add(i);
			if(first==second){
				continue;
			}
			al.add(n/i);
		}
		return al;
	}
	
	public static int power(int x, int y) 
	{ 
		int temp; 
		if( y == 0) 
			return 1; 
		temp = power(x, y/2); 
		if (y%2 == 0) 
			return temp*temp; 
		else
			return x*temp*temp; 
	} 
	
	public static ArrayList<Integer> comp(){
		ArrayList<Integer> al = new ArrayList<>();
			int n = (int)2e5;
			boolean arr[] = new boolean[n+1];
			int sqrt = (int)Math.sqrt(n);
			for(int i=2;i<=sqrt;i++){
				if(arr[i]==false){
					for(int j=i*i;j<=n;j+=i){
						arr[j]=true;
					}
				}
			}
			for(int i=2;i<=n;i++){
				if(arr[i]==false)
					al.add(i);
			}
			
		return al;
	}
	
	public static class pair{
		int x;
		int y;
		pair(int x, int y){
			this.x=x;
			this.y=y;
		}
	}
	
	public static class comp implements Comparator<pair>{
		public int compare(pair o1, pair o2){
			if(o1.x==o2.x){
				return o1.y-o2.y;
			}
			return o1.x-o2.x;
		}
	}
	
	
	
	
	static class Node{
		int node;
		int d;
		ArrayList<Integer> al = new ArrayList<>();	
	}
	

	static long gcd(long a, long b) 
    { 
      if (b == 0) 
        return a; 
      return gcd(b, a % b);  
    }
	
		
	static class FastReader 
	{ 
		final private int BUFFER_SIZE = 1 << 16; 
		private DataInputStream din; 
		private byte[] buffer; 
		private int bufferPointer, bytesRead; 
  
		public FastReader() 
		{ 
			din = new DataInputStream(System.in); 
			buffer = new byte[BUFFER_SIZE]; 
			bufferPointer = bytesRead = 0; 
		} 
  
		public FastReader(String file_name) throws IOException 
		{ 
			din = new DataInputStream(new FileInputStream(file_name)); 
			buffer = new byte[BUFFER_SIZE]; 
			bufferPointer = bytesRead = 0; 
		} 
  
		public String readLine() throws IOException 
		{ 
			byte[] buf = new byte[64]; // line length 
			int cnt = 0, c; 
			while ((c = read()) != -1) 
			{ 
				if (c == '\n') 
					break; 
				buf[cnt++] = (byte) c; 
			} 
			return new String(buf, 0, cnt); 
		} 
  
		public int nextInt() throws IOException 
		{ 
			int ret = 0; 
			byte c = read(); 
			while (c <= ' ') 
				c = read(); 
			boolean neg = (c == '-'); 
			if (neg) 
				c = read(); 
			do
			{ 
				ret = ret * 10 + c - '0'; 
			}  while ((c = read()) >= '0' && c <= '9'); 
  
			if (neg) 
				return -ret; 
			return ret; 
		} 
  
		public long nextLong() throws IOException 
		{ 
			long ret = 0; 
			byte c = read(); 
			while (c <= ' ') 
				c = read(); 
			boolean neg = (c == '-'); 
			if (neg) 
				c = read(); 
			do { 
				ret = ret * 10 + c - '0'; 
			} 
			while ((c = read()) >= '0' && c <= '9'); 
			if (neg) 
				return -ret; 
			return ret; 
		} 
  
		public double nextDouble() throws IOException 
		{ 
			double ret = 0, div = 1; 
			byte c = read(); 
			while (c <= ' ') 
				c = read(); 
			boolean neg = (c == '-'); 
			if (neg) 
				c = read(); 
  
			do { 
				ret = ret * 10 + c - '0'; 
			} 
			while ((c = read()) >= '0' && c <= '9'); 
  
			if (c == '.') 
			{ 
				while ((c = read()) >= '0' && c <= '9') 
				{ 
					ret += (c - '0') / (div *= 10); 
				} 
			} 
  
			if (neg) 
				return -ret; 
			return ret; 
		} 
  
		private void fillBuffer() throws IOException 
		{ 
			bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE); 
			if (bytesRead == -1) 
				buffer[0] = -1; 
		} 
  
		private byte read() throws IOException 
		{ 
			if (bufferPointer == bytesRead) 
				fillBuffer(); 
			return buffer[bufferPointer++]; 
		} 
  
		public void close() throws IOException 
		{ 
			if (din == null) 
				return; 
			din.close(); 
		} 
	} 
}
