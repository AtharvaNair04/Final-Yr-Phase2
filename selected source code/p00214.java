import java.util.Arrays;
import java.util.Scanner;

//Autumnal Illumination
public class Main{

	int n;
	int[][][] p;
	
	int cross(int[] s, int[] t, int[] r){
		int x1 = t[0]-s[0], y1 = t[1]-s[1], x2 = r[0]-s[0], y2 = r[1]-s[1];
		return x1*y2-x2*y1;
	}
	boolean online(int[] s, int[] t, int[] r){
		if(cross(s, t, r)!=0)return false;
		int minx = Math.min(s[0], t[0]), maxx = Math.max(s[0], t[0]);
		int miny = Math.min(s[1], t[1]), maxy = Math.max(s[1], t[1]);
		return minx<=r[0]&&r[0]<=maxx&&miny<=r[1]&&r[1]<=maxy;
	}
	boolean isCross(int[] a, int[] b, int[] s, int[] t){
		if(cross(a, b, s)*cross(a, b, t)>=0)return false;
		if(cross(b, a, s)*cross(b, a, t)>=0)return false;
		if(cross(s, t, a)*cross(s, t, b)>=0)return false;
		return cross(t, s, a)*cross(t, s, b)<0;
	}
	
	boolean col(int a, int b){
		for(int i=0;i<4;i++)for(int j=0;j<4;j++){
			if(online(p[a][i], p[a][(i+1)%4], p[b][j]))return true;
			if(online(p[b][i], p[b][(i+1)%4], p[a][j]))return true;
			if(isCross(p[a][i], p[a][(i+1)%4], p[b][j], p[b][(j+1)%4]))return true;
		}
		for(int j=0;j<4;j++){
			boolean in = true;
			for(int i=0;i<4;i++)if(cross(p[a][i], p[a][(i+1)%4], p[b][j])>=0)in = false;
			if(in)return true;
		}
		return false;
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
//		for(;;){
//			int[] p1 = new int[]{0, 0}, p2 = new int[]{10, 0};
//			int[][] t = new int[2][2];
//			for(int i=0;i<2;i++)for(int j=0;j<2;j++)t[i][j]=sc.nextInt();
//			System.out.println("Inter:"+isCross(p1, p2, t[0], t[1])+" Rev:"+isCross(t[0], t[1], p1, p2));
//			System.out.println("Online:"+online(p1, p2, t[0])+" :"+online(p1, p2, t[1]));
//			System.out.println("Rev:"+online(t[0], t[1], p1)+" :"+online(t[0], t[1], p2));
//			System.out.println("Line: ("+t[0][0]+","+t[0][1]+")-("+t[1][0]+","+t[1][1]+") P("+t[2][0]+","+t[2][1]+") is:" + online(t[0], t[1], t[2]));
			
//		}
		for(;;){
			int m = sc.nextInt();
			if(m==0)break;
			while(m--!=0){
				n = sc.nextInt();
				p = new int[n][4][2];
				for(int i=0;i<n;i++)for(int j=0;j<4;j++)for(int k=0;k<2;k++)p[i][j][k]=sc.nextInt();
				UnionFind u = new UnionFind(n);
				for(int i=0;i<n;i++)for(int j=0;j<n;j++){
					if(u.find(i, j))continue;
					if(col(i, j))u.union(i, j);
				}
				System.out.println(u.num);
			}
		}
	}
	
	class UnionFind {
		final int[] tree;
		int num;
		public UnionFind(int n) {
			this.tree = new int[n];
			Arrays.fill(tree, -1);
			num=n;
		}
		void union(int x, int y) {
			x = root(x);
			y = root(y);
			if(x != y) {
				if(tree[x] < tree[y]) {
					x ^= y; y ^= x; x^= y;
				}
				tree[x] += tree[y];
				tree[y] = x;
				num--;
			}
		}
		boolean find(int x, int y) {
			return root(x) == root(y);
		}
		int root(int x) {
			return tree[x] < 0 ? x : (tree[x] = root(tree[x]));
		}
		int size(int x) {
			return -tree[root(x)];
		}
		int getNum() {
			return num;
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
