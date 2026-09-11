//School of Killifish
public class Main{

	class Scanner {
		int nextInt() {
			try {
				int c = System.in.read();
				while (c != '-' && (c < '0' || '9' < c))
					c = System.in.read();
				if (c == '-') return -nextInt();
				int res = 0;
				do {
					res *= 10;
					res += c - '0';
					c = System.in.read();
				} while ('0' <= c && c <= '9');
				return res;
			} catch (Exception e) {
				return -1;
			}
		}
	}
	
	void run(){
		Scanner sc = new Scanner();
		int INF = Integer.MAX_VALUE;
		int[][] min = new int[1000][1000];
		for(;;){
			int R = sc.nextInt(), C = sc.nextInt(), Q = sc.nextInt(), MIN = INF;
			if((R|C|Q)==0)break;
			int h = (int) Math.ceil(Math.sqrt(R)), w = (int) Math.ceil(Math.sqrt(C));
			for(int i=0;i<h;i++)for(int j=0;j<w;j++)min[i][j]=INF;
			int[][] a = new int[R][C];
			for(int i=0;i<R;i++)for(int j=0;j<C;j++){
				a[i][j] = sc.nextInt();
				int I = i/h, J = j/w;
				min[I][J] = Math.min(min[I][J], a[i][j]);
				MIN = Math.min(MIN, INF);
			}
			while(Q--!=0){
				int r1 = sc.nextInt(), c1 = sc.nextInt(), r2 = sc.nextInt(), c2 = sc.nextInt(), res = INF;
				for(int i=r1/h;i<=r2/h;i++){
					int y1 = Math.max(r1, i*h), y2 = Math.min(r2, i*h+h-1);
					for(int j=c1/w;j<=c2/w;j++){
						if(res <= min[i][j])continue;
						int x1 = Math.max(c1, j*w), x2 = Math.min(c2, j*w+w-1);
						if(y1==i*h && y2==(i+1)*h-1 && x1 == j*w && x2 == (j+1)*w-1)res = Math.min(res, min[i][j]);
						else{
							for(int y=y1;y<=y2;y++)for(int x=x1;x<=x2;x++)
								res = Math.min(res, a[y][x]);
						}
					}
				}
				System.out.println(res);
			}
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
