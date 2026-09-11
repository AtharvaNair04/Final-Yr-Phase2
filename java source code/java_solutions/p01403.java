import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
	
	public static final int MAX = 1000000;
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		
		final int t = sc.nextInt();
		
		long[] p = new long[MAX + 1];
		long[] f = new long[MAX + 1];
		
		for(int i = 0; i <= MAX; i++){
			p[i] = 1L;
			f[i] = i;
		}
		
		for(int i = 2; i <= MAX; i++){
			if(p[i] > 0){
				f[i] -= f[i] / i;
				
				for(int j = 2 * i; j <= MAX; j += i){
					p[j] = 0;
					f[j] -= f[j] / i;
				}
			}
		}
		
		long[] sum = new long[MAX + 1];
		sum[0] = f[0] + 1;
		for(int i = 1; i <= MAX; i++){
			sum[i] = f[i] + sum[i - 1];
		}
		
		for(int tt = 0; tt < t; tt++){
			final int n = sc.nextInt();
			
			//System.out.println(f[n]);
			System.out.println(sum[n]);
		}
	}
	
	public static class Scanner {
		private BufferedReader br;
		private StringTokenizer tok;
		
		public Scanner(InputStream is) throws IOException{
			br = new BufferedReader(new InputStreamReader(is));
			getLine();
		}
		
		private void getLine() throws IOException{
			while(tok == null || !tok.hasMoreTokens()){
				tok = new StringTokenizer(br.readLine());
			}
		}
		
		private boolean hasNext(){
			return tok.hasMoreTokens();
		}
		
		public String next() throws IOException{
			if(hasNext()){
				return tok.nextToken();
			}else{
				getLine();
				return tok.nextToken();
			}
		}
		
		public int nextInt() throws IOException{
			if(hasNext()){
				return Integer.parseInt(tok.nextToken());
			}else{
				getLine();
				return Integer.parseInt(tok.nextToken());
			}
		}
		
		public long nextLong() throws IOException{
			if(hasNext()){
				return Long.parseLong(tok.nextToken());
			}else{
				getLine();
				return Long.parseLong(tok.nextToken());
			}
		}
		
		public double nextDouble() throws IOException{
			if(hasNext()){
				return Double.parseDouble(tok.nextToken());
			}else{
				getLine();
				return Double.parseDouble(tok.nextToken());
			}
		}
	}

}
