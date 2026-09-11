import java.util.Scanner;
public class Main {
	
	public static void main(String[] args) {
		try(Scanner sc = new Scanner(System.in)) {
			while(sc.hasNext()) {
				int N = sc.nextInt();
				if(N == 0) break;
				
				int[] dx = {-1, 0, 1, 0};
				int[] dy = {0, -1, 0, 1};
				int[] x = new int[N];
				int[] y = new int[N];
				x[0] = 0;
				y[0] = 0;
				
				int minx = 0;
				int maxx = 0;
				int miny = 0;
				int maxy = 0;
				
				for(int i=1; i<N; i++) {
					int n = sc.nextInt();
					int d = sc.nextInt();
					x[i] = x[n] + dx[d];
					y[i] = y[n] + dy[d];
					
					minx = Math.min(x[i], minx);
					maxx = Math.max(x[i], maxx);
					miny = Math.min(y[i], miny);
					maxy = Math.max(y[i], maxy);
				}
				
				int xdif = maxx - minx + 1;
				int ydif = maxy - miny + 1;
				System.out.printf("%d %d\n", xdif, ydif);
			}
		}
	}
}
