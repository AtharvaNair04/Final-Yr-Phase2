import java.io.*;
import java.util.*;

public class Main {
	
	static final int[] queens = new int[8];
	static final boolean[] threatend = new boolean[8];
	
	public static void main(String[] Args) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		final int n = Integer.parseInt(br.readLine());
		for (int i = 0; i < n; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			queens[r] = c;
			threatend[r] = true;
		}
		check(0);
		final char[] output = new char[72];
		Arrays.fill(output,'.');
		for (int i = 8; i < 72; i+=9) {
			output[i] = '\n';
		}
		for (int i = 0; i < 8; i++) {
			output[i*9+queens[i]] = 'Q';
		}
		System.out.print(output);
	}
	
	private static boolean check(int r){
		if (r>=8) return true;
		if(threatend[r]) return available(r, queens[r]) && check(r+1);
		for (int i = 0; i < 8; i++) {
			queens[r] = i;
			if(available(r,i) && check(r+1)) return true;
		}
		return false;
	}
	
	private static boolean available(int r, int c){
		for (int i = 0; i < 8; i++) {
			if(i<r||(i>r && threatend[i])){
				if(c == queens[i]) return false;
				if(Math.abs(c-queens[i]) == Math.abs(r-i)) return false;
			}
		}
		return true;
	}
}
