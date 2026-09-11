import java.io.*;
import java.util.*;
 
public class Main{
	
	int N, M;
	int[] step = new int[3];
	IntMatrix[] mats;
	boolean[] flg;
	int[] ans;
	public void solve(){
		N = nextInt();
		M = nextInt();
		step[0] = nextInt();
		step[1] = nextInt();
		step[2] = nextInt();
		IntMatrix mat = new IntMatrix(N, N);
		for(int i = 0; i < M; i++){
			int s = nextInt() - 1;
			int d = nextInt() - 1;
			mat.set(s, d, 1);
		}
		mats = new IntMatrix[3];
		for(int i = 0; i < 3; i++){
			mats[i] = mat.pow(step[i]);
			//out.println(mats[i]);
		}
		
		ans = new int[N];
		for(int i = 0; i < N; i++){
			ans[i] = 100000;
		}
		ans[N - 1] = 0;
		boolean flg = true;
		while(flg){
			flg = false;
			for(int i = 0; i < N; i++){
				int mm = max(i);
				if(ans[i] > mm){
					flg = true;
					ans[i] = mm;
				}
			}
		}
		if(ans[0] >= 100000){
            out.println("IMPOSSIBLE");
        }else{
            out.println(ans[0]);
        }
	}
	
	public int max(int node){
		int max = 0;
		for(int i = 0; i < 3; i++){
			max = Math.max(max, min(node, i));
		}
		return max;
	}
	
	public int min(int node, int s){
		int min = 100000;
		for(int i = 0; i < N; i++){
			if(mats[s].get(node, i) > 0){
				min = Math.min(min, ans[i] + 1);
			}
		}
		return min;
	}
	
	private static PrintWriter out;
	public static void main(String[] args){
		out = new PrintWriter(System.out);
		new Main().solve();
		out.flush();
	}
	
	
	
	public static int nextInt(){
		int num = 0;
		String str = next();
		boolean minus = false;
		int i = 0;
		if(str.charAt(0) == '-'){
			minus = true;
			i++;
		}
		int len = str.length();
		for(;i < len; i++){
			char c = str.charAt(i);
			if(!('0' <= c && c <= '9')) throw new RuntimeException();
			num = num * 10 + (c - '0');
		}
		return minus ? -num : num;
	}
	
	public static long nextLong(){
		long num = 0;
		String str = next();
		boolean minus = false;
		int i = 0;
		if(str.charAt(0) == '-'){
			minus = true;
			i++;
		}
		int len = str.length();
		for(;i < len; i++){
			char c = str.charAt(i);
			if(!('0' <= c && c <= '9')) throw new RuntimeException();
			num = num * 10l + (c - '0');
		}
		return minus ? -num : num;
	}
	public static String next(){
		int c;
		while(!isAlNum(c = read())){}
		StringBuilder build = new StringBuilder();
		build.append((char)c);
		while(isAlNum(c = read())){
			build.append((char)c);
		}
		return build.toString();
	}
	
	
	private static byte[] inputBuffer = new byte[1024];
	private static int bufferLength = 0;
	private static int bufferIndex = 0;
	private static int read(){
		if(bufferLength < 0) throw new RuntimeException();
		if(bufferIndex >= bufferLength){
			try{
				bufferLength = System.in.read(inputBuffer);
				bufferIndex = 0;
			}catch(IOException e){
				throw new RuntimeException(e);
			}
			if(bufferLength <= 0) return (bufferLength = -1);
		}
		return inputBuffer[bufferIndex++];
	}
	
	private static boolean isAlNum(int c){
		return '!' <= c && c <= '~';
	}
}

class IntMatrix {
	public final int row;
	public final int colum;
	int[] data;
	public IntMatrix(int row, int colum){
		this.row = row;
		this.colum = colum;
		this.data = new int[row * colum];
	}
	
	public int index(int r, int c){
		if(0 <= r && r < row && 0 <= c && c <= colum){
			return r * colum + c;
		}else{
			throw new IndexOutOfBoundsException("Matrix size row: " + row + " colum:" + colum + "access: (" + r + "," + c + ")");
		}
	}
	
	public void set(int r, int c, int v){
		this.data[index(r,c)] = v;
	}
	public int get(int r, int c){
		return this.data[index(r,c)];
	}
	
	public static IntMatrix identity(int n){
		IntMatrix mat = new IntMatrix(n, n);
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(i == j) mat.set(i, j, 1);
				else mat.set(i, j, 0);
			}
		}
		return mat;
	}
	
	public IntMatrix mul(IntMatrix mat){
		if(this.colum != mat.row) throw new ArithmeticException();
		IntMatrix ans = new IntMatrix(this.row, mat.colum);
		for(int i = 0; i < ans.row; i++){
			for(int j = 0; j < ans.colum; j++){
				int sum = 0;
				for(int k = 0; k < this.colum; k++){
					sum = Math.max(sum, get(i, k) * mat.get(k, j));
				}
				ans.set(i, j, sum);
			}
		}
		return ans;
	}
	
	public IntVector mul(IntVector vec){
		if(this.colum != vec.size) throw new ArithmeticException();
		IntVector ans = new IntVector(this.row);
		for(int i = 0; i < ans.size; i++){
			int sum = 0;
			for(int j = 0; j < this.colum; j++){
				sum += get(i, j) * vec.get(j);
			}
			ans.set(i, sum);
		}
		return ans;
	}
	
	public IntMatrix pow(int n){
		if(this.colum != this.row) throw new ArithmeticException();
		if(n == 0) return IntMatrix.identity(this.row);
		if(n % 2 == 0) return (this.mul(this)).pow(n / 2);
		else return this.mul(this.pow(n - 1));
	}

	public String toString(){
		StringBuilder build = new StringBuilder();
		build.append("[");
		for(int i = 0; i < row; i++){
			build.append("[");
			for(int j = 0; j < colum; j++){
				if(j != 0) build.append(",");
				build.append(get(i, j));
			}
			build.append("]");
			if(i != row - 1) build.append(",");
			build.append("\n");
		}
		return build.toString();
	}
}

class IntVector{
	public final int size;
	private int[] data;
	public IntVector(int size){
		this.size = size;
		data = new int[size];
	}

	public void set(int i, int v){
		this.data[i] = v;
	}
	public int get(int i){
		return this.data[i];
	}
}
