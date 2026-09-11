import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

//Computation of Minimum Length of Pipeline
public class Main{

	class E implements Comparable<E>{
		int s, t, d;
		public E(int s, int t, int d) {
			this.s = s;
			this.t = t;
			this.d = d;
		}
		public int compareTo(E o) {
			return d-o.d;
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

	void run(){
		Scanner sc = new Scanner(System.in);
		for(;;){
			int s = sc.nextInt(), n = sc.nextInt();
			if((s|n)==0)break;
			PriorityQueue<E> q = new PriorityQueue<E>();
			for(int i=0;i<s;i++)for(int j=0;j<n;j++){
				int x = sc.nextInt();
				if(x==0)continue;
				q.add(new E(i, s+j, x));
			}
			for(int i=0;i<n;i++)for(int j=i+1;j<n;j++){
				int x = sc.nextInt();
				if(x==0)continue;
				q.add(new E(s+i, s+j, x));
			}
			UnionFind u = new UnionFind(s+n);
			boolean[] ok = new boolean[s+n];
			for(int i=0;i<s;i++)ok[i] = true;
			int res = 0, c = s;
			while(c<s+n){
				E e = q.poll();
				if(ok[e.s]&&ok[e.t])continue;
				if(!u.find(e.s, e.t)){
					res += e.d;
					u.union(e.s, e.t);
					if(ok[e.s]){
						for(int i=s;i<s+n;i++){
							if(!ok[i]&&u.find(e.s, i)){
								ok[i] = true; c++;
							}
						}
					}
					else if(ok[e.t]){
						for(int i=s;i<s+n;i++){
							if(!ok[i]&&u.find(e.t, i)){
								ok[i] = true; c++;
							}
						}
					}
				}
			}
			System.out.println(res);
		}
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
