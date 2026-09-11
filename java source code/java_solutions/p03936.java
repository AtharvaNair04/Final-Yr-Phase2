import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static Scanner in;
	static PrintWriter out;
	static String INPUT = "";

	static int count(int r1, int c1, int r2, int c2)
	{
		out.printf("? %d %d %d %d\n", r1, c1, r2, c2);
		out.flush();
		return ni();
	}
	
	static void end(boolean[][] map, int mod)
	{
		long h = 0;
		long t = 1;
		for(int i = 0;i < map.length;i++){
			for(int j = 0;j < map[0].length;j++){
				h += (map[i][j] ? 1 : 0) * t;
				h %= mod;
				t = t * 2 % mod;
			}
		}
		out.println("! " + h);
		out.flush();
	}
	
	static void solve()
	{
		int h = ni(), w = ni(), n = ni(), K = ni();
		boolean[][] map = new boolean[h][w];
		for(int i = 0;i < h;i+=2){
			for(int j = 0;j < w;j+=2){
				int lc = count(i, j, i+1, j+1);
				if(lc == 0)continue;
				if(lc == 4){
					for(int k = 0;k < 2;k++){
						for(int l = 0;l < 2;l++){
							map[i+k][j+l] = true;
						}
					}
					continue;
				}
				if(lc == 1){
					int llc = count(i, j, i+1, j);
					int q = llc^1;
					int lruc = count(i, j+q, i, j+q);
					map[i+(lruc^1)][j+q] = true;
					continue;
				}
				if(lc == 3){
					for(int k = 0;k < 2;k++){
						for(int l = 0;l < 2;l++){
							map[i+k][j+l] = true;
						}
					}
					int llc = count(i, j, i+1, j);
					int q = 2-llc^1;
					int lruc = count(i, j+q, i, j+q);
					map[i+(1-lruc^1)][j+q] = false;
					continue;
				}
				int rem = 2;
				for(int k = 0;k < 2;k++){
					for(int l = 0;l < 2;l++){
						int co = k == 1 && l == 1 ? rem : count(i+k, j+l, i+k, j+l);
						rem -= co;
						map[i+k][j+l] = co == 1;
					}
				}
			}
		}
		end(map, K);
	}
	
	public static void main(String[] args) throws Exception
	{
		in = INPUT.isEmpty() ? new Scanner(System.in) : new Scanner(INPUT);
		out = new PrintWriter(System.out);
		
		solve();
		out.flush();
	}
	
	static int ni() { return Integer.parseInt(in.next()); }
	static long nl() { return Long.parseLong(in.next()); }
	static double nd() { return Double.parseDouble(in.next()); }
	static void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
}
