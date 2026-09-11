import java.util.*;
import java.io.*;
import java.math.*;
public class Main{
	public static PrintWriter out = new PrintWriter (new BufferedOutputStream(System.out));
	public static void main(String sp[])throws IOException{
		Scanner sc = new Scanner(System.in);
		//FastReader sc = new FastReader();	
		char set[] = new char[30];
		int n = sc.nextInt();
		int c = sc.nextInt();
		int d = c;
		int er[][] = new int[c+1][c+1];
		for(int i=1;i<=c;i++){
			for(int j=1;j<=c;j++)
				er[i][j] = sc.nextInt();
		}
		int arr[][] = new int[n+1][n+1];
		for(int i=1;i<=n;i++){
			for(int j=1;j<=n;j++)
				arr[i][j] = sc.nextInt();
		}
		char ch = 'a';
		for(int i=0;i<Math.min(c,26);i++){
			set[i] = ch;
			ch++;
		}
		if(c>26){
			c-=26;
			ch = 'A';
			for(int i=0;i<Math.min(26,c);i++){
				set[i+26] = ch;
				ch++;
			}
		}
		printAllKLengthRec(set, "", d, 3);
		
		long dp[][] = new long[3][d+1];
		
		
		for(int i=1;i<=n;i++){
			for(int j=1;j<=n;j++){
				int g = (i+j)%3;
				for(int k=1;k<=d;k++){
					if(arr[i][j]==k)
						continue;
					dp[g][k] += er[arr[i][j]][k];
				}
			}
		}
		long min = Long.MAX_VALUE;
		int siz = al.size();
		for(int i=0;i<siz;i++){
			String st = al.get(i);
			if(st.charAt(0)==st.charAt(1) || st.charAt(1)==st.charAt(2) || st.charAt(0)==st.charAt(2))
				continue;
			char ch1 = st.charAt(0);
			char ch2 = st.charAt(1);
			char ch3 = st.charAt(2);

			int ind1 = (ch1>='A' && ch1<='Z') ? ((26)+((ch1-'A')+1)) : ((ch1-'a')+1);
			int ind2 = (ch2>='A' && ch2<='Z') ? ((26)+((ch2-'A')+1)) : ((ch2-'a')+1);
			int ind3 = (ch3>='A' && ch3<='Z') ? ((26)+((ch3-'A')+1)) : ((ch3-'a')+1);
			min = Math.min(dp[0][ind1]+dp[1][ind2]+dp[2][ind3], min);
		}
		System.out.println(min);

		
    }
	
	static ArrayList<String> al = new ArrayList<>();
	
	static void printAllKLengthRec(char[] set,  
								   String prefix,  
								   int n, int k) 
	{ 
		if (k == 0)  
		{ 
			al.add(prefix); 
			return; 
		} 
	  
		
		for (int i = 0; i < n; ++i) 
		{ 
	  
			String newPrefix = prefix + set[i];  
			  
			
			printAllKLengthRec(set, newPrefix,  
									n, k - 1);  
		} 
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
