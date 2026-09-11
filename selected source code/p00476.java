import java.awt.Point;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	public static class LazySegTree{
		int n;
		long[] dat;
		long[] lazy;

		public LazySegTree(int n_) {
			int n = 1;
			while(n < n_){
				n *= 2;
			}
			
			this.n = n;
			dat = new long[this.n * 2 - 1];
			lazy = new long[this.n * 2 - 1];
			for(int i = 0; i < this.n * 2 - 1 ; i++){
				dat[i] = 0;
				lazy[i] = 0;
			}
		}
		
		
		public void update_node(int k){
			dat[k] = dat[2 * k + 1] + dat[2 * k + 2];
		}
		
		long max(int a, int b, int k, int l, int r)
		{
			if (b <= l || r <= a){
				return 0;
			}else if (a <= l && r <= b){
				return dat[k] + lazy[k];
			}
			
			long lch = max(a, b, k * 2 + 1, l, (l + r) / 2);
			long rch = max(a, b, k * 2 + 2, (l + r) / 2, r);
			
			return (Math.max(lch, rch) + lazy[k]);
		}

		void add(long x, int a, int b, int k, int l, int r)
		{
			if (b <= l || r <= a) return;
			
			if (a <= l && r <= b){
				lazy[k] += x;
				while (k > 0){
					k = (k - 1) / 2;
					dat[k] = Math.max(dat[k * 2 + 1] + lazy[k * 2 + 1], dat[k * 2 + 2] + lazy[k * 2 + 2]);
				}
			}
			else {
				add(x, a, b, k * 2 + 1, l, (l + r) / 2);
				add(x, a, b, k * 2 + 2, (l + r) / 2, r);
			}
		}
		
		public int size(){
			return this.n;
		}
		
		public String toString(){
			return Arrays.toString(dat) + "\n" + Arrays.toString(lazy);
		}
	}
	
	public static class Floor implements Comparable<Floor>{
		long rest_value;
		int pos;
		
		public Floor(long rest_value, int pos) {
			super();
			this.rest_value = rest_value;
			this.pos = pos;
		}
		
		public Floor(Floor o){
			super();
			this.rest_value = o.rest_value;
			this.pos = o.pos;
		}

		@Override
		public int compareTo(Floor o) {
			if(this.rest_value == o.rest_value){
				return this.pos - o.pos;
			}else{
				return o.rest_value > this.rest_value ? 1 : o.rest_value < this.rest_value ? -1 : 0;
			}
		}

		@Override
		public String toString() {
			return "Floor [rest_value=" + rest_value + ", pos=" + pos + "]";
		}
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		final int N  = sc.nextInt();
		final long H = sc.nextLong();
		
		LazySegTree seg = new LazySegTree(N);
		PriorityQueue<Floor> queue = new PriorityQueue<Floor>();
		
		long count = 0;
		long life = H;
		for(int i = 0; i < N - 1; i++){
			final int d = sc.nextInt();
			final int h = sc.nextInt();
			
			queue.add(new Floor(h, i));
			seg.add(life, i, i + 1, 0, 0, seg.size());
			
			life -= d;
			queue.add(new Floor(h, i));
			
			while(life <= 0){
				Floor floor = queue.poll();
				
				//if(heal.rest_value == 0){
				//	continue;
				//}
				
				//System.out.println(heal + " " +  queue);
				
				long max = seg.max(floor.pos, i + 1, 0, 0, seg.size());
				
				//System.out.println(i + " " + max + " " + life);
				
				if(floor.rest_value + max > H){
					//heal.rest_value = H - max;
					queue.add(new Floor(H - max, floor.pos));
					continue;
				}
				
				long many = Math.min((-life) / floor.rest_value + 1, (H - max) / floor.rest_value);
				count += many;
				life += many * floor.rest_value;
				
				seg.add(many * floor.rest_value, floor.pos, i + 1, 0, 0, seg.size());
				queue.add(new Floor(floor));
			}
		}
		
		
		//System.out.println(
		System.out.println(count);
	}
}
