import java.util.*;
import java.io.*;

public class Main {
		public static void main(String[] args) throws IOException {
		
		// 入出力高速化 java.io.* とthrows IOExceptionを忘れずに
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int N = Integer.parseInt(br.readLine());
		String S = br.readLine();
		String T = br.readLine();
		
		int Tstart = 0;
		Scompare: for(int t=N; t>0;t--){
			for(int s=0; s<N; s++){
				if(S.substring(s).contentEquals(T.substring(0,t))){
					Tstart = t;
					break Scompare;
				}
			}
		}
		
        System.out.print(2*N-Tstart);
    }
}
