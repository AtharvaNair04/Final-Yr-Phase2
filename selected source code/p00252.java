import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.StringTokenizer;

public class Main{
	public static void main(String[] args) {
		SC sc = new SC(System.in);
		int stat=0;
		stat+=sc.nextInt()*4;
		stat+=sc.nextInt()*2;
		stat+=sc.nextInt();
		if(stat==6 || stat==1 || stat==7) {
			pl("Open");
		}
		else {
			pl("Close");
		}
	}
	static class SC {
		private BufferedReader reader = null;
		private StringTokenizer tokenizer = null;
		public SC(InputStream in) {
			reader = new BufferedReader(new InputStreamReader(in));
		}
		public String next() {
			if (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
			return tokenizer.nextToken();
		}
		public int nextInt() {
			return Integer.parseInt(next());
		}
		public long nextLong() {
			return Long.parseLong(next());
		}
		public double nextDouble() {
			return Double.parseDouble(next());
		}
		public String nextLine() {
			try {
				return reader.readLine();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}
	public static void pl(Object o) {
		System.out.println(o);
	}
	public static void pl() {
		System.out.println();
	}
	public static void p(Object o) {
		System.out.print(o);
	}
	static int root(int a) {

		int ketasu=0;
		int tmp=a;
		while(tmp>0) {
			ketasu++;
			tmp/=10;
		}
		int constant=(ketasu+1)/2;	//よく使うので定数化
		if(ketasu>=1) {
			int[] suuji=new int[constant];
			tmp=a;
			for(int i=0; i<constant; i++) {
				suuji[constant-1-i]=a%100;
				a/=100;
			}
			int ans=0;
			int kai=0;
			int mae=0;
			if(constant<=1) {
				for(int i=10; i>=0; i--) {
					if(suuji[0]>=i*i) {
						return i;
					}
				}
			}
			else {
				for(int i=0; i<constant; i++) {
					mae+=kai*2;
					for(int j=9; j>=0; j--) {
						if((mae*10+j)*j<=suuji[i]) {
							ans=ans*10+j;
							mae*=10;
							kai=j;
							if(i+1<constant) {
								suuji[i+1]+=(suuji[i]-(mae+j)*j)*100;
							}
							break;
						}
					}
				}
			}
			return ans;
		}
		else {
			return 0;
		}
	}
}
