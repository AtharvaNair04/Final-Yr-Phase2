import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Main {
	
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		
		final int N = sc.nextInt();
		final long a = sc.nextInt();
		final long d = sc.nextInt();
		
		final int M = sc.nextInt();
		int[] xs = new int[M];
		int[] ys = new int[M];
		int[] zs = new int[M];
		
		for(int i = 0; i < M; i++){
			xs[i] = sc.nextInt();
			ys[i] = sc.nextInt() - 1;
			zs[i] = sc.nextInt() - 1;
		}
		
		final int K = sc.nextInt() - 1;
		int[] query_K = new int[M + 1];
		query_K[M] = K;
		
		for(int i = M - 1; i >= 0; i--){
			if(xs[i] == 0 && ys[i] <= query_K[i + 1] && query_K[i + 1] <= zs[i]){
				query_K[i] = (zs[i] - query_K[i + 1]) + ys[i];
			}else{
				query_K[i] = query_K[i + 1];
			}
		}
		
		long answer = a + d * query_K[0];
		for(int i = 0; i < M; i++){
			if(xs[i] == 0){ continue; }
			else if(xs[i] == 1 && ys[i] <= query_K[i] && query_K[i] <= zs[i]){
				answer += 1;
			}else if(xs[i] == 2 && ys[i] <= query_K[i] && query_K[i] <= zs[i]){
				answer /= 2;
			}
		}
		
		System.out.println(answer);
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
	}
}
