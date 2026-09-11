import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
class Main {
	static int MAX_N = 131072 * 2;
	static int[] dat = new int[MAX_N];
	static int n;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		int d = sc.nextInt();
		Data[] data = new Data[n];
		for(int i = 0; i < n; i++) {
			data[i] = new Data(sc.nextInt(),sc.nextInt(),sc.nextInt());
		}
		Arrays.sort(data);
		
		SegTree xmin = new SegTree(n);
		SegTreeM xmax = new SegTreeM(n);
		SegTree ymin = new SegTree(n);
		SegTreeM ymax = new SegTreeM(n);
		
		for(int i = 0; i < n; i++) {
			xmin.update(i, data[i].x);
			xmax.update(i, data[i].x);
			ymin.update(i, data[i].y);
			ymax.update(i, data[i].y);
		}
		long max = 0;
		int r = 0;
		for(int i = 0; i < n; i++) {
			while(r+1 < n && data[i].b + d >= data[r+1].b) {
				r++;
			}
			long sum = 0;
			if(i != r) {
			sum = (long)(xmax.query(i, r+1) - xmin.query(i, r+1))*(ymax.query(i, r+1) - ymin.query(i, r+1));
			}
			else {
				sum = 0;
			}
			max = Math.max(sum, max);
		}
		
		
		System.out.println(max);
	}
	
	static class SegTreeM {
		int n, n_, m;
		int[] node;
		
		public SegTreeM(int n) {
			this.n = n;
			this.n_ = Integer.highestOneBit(Math.max(n-1,1) << 1);
			this.m = 2*n_- 1;
			this.node = new int[m];
			Arrays.fill(node, Integer.MIN_VALUE);
		}
		
		void update(int x, int a) {
			x += n_ -1;
			node[x] = a;
			while(x > 0) {
				x = (x-1)/2;
				node[x] = Math.max(node[2*x+1], node[2*x+2]);
			}
		}
		
		int query(int l, int r) {
            return query(l,r,0,n_,0);
        }
 
        private int query(int l, int r, int cl, int cr, int cur) {
            if (l <= cl && cr <= r) {
                return node[cur];
            }else {
                int ret = Integer.MIN_VALUE;
                int mid = (cl+cr)/2;
                if (cl < r && l < mid)
                    ret = Math.max(ret,query(l,r,cl,mid,2*cur+1));
                if (mid < r && l < cr)
                    ret = Math.max(ret,query(l,r,mid,cr,2*cur+2));
                return ret;
            }
        }
		
		
	}
	
	
	
	static class SegTree {
		int n, n_, m;
		int[] node;
		
		public SegTree(int n) {
			this.n = n;
			this.n_ = Integer.highestOneBit(Math.max(n-1,1) << 1);
			this.m = 2*n_- 1;
			this.node = new int[m];
			Arrays.fill(node, Integer.MAX_VALUE);
		}
		
		void update(int x, int a) {
			x += n_ -1;
			node[x] = a;
			while(x > 0) {
				x = (x-1)/2;
				node[x] = Math.min(node[2*x+1], node[2*x+2]);
			}
		}
		
		int query(int l, int r) {
            return query(l,r,0,n_,0);
        }
 
        private int query(int l, int r, int cl, int cr, int cur) {
            if (l <= cl && cr <= r) {
                return node[cur];
            }else {
                int ret = Integer.MAX_VALUE;
                int mid = (cl+cr)/2;
                if (cl < r && l < mid)
                    ret = Math.min(ret,query(l,r,cl,mid,2*cur+1));
                if (mid < r && l < cr)
                    ret = Math.min(ret,query(l,r,mid,cr,2*cur+2));
                return ret;
            }
        }
		
		
	}
	
	
	
	static class Data implements Comparable<Data> {
		int x;
		int y;
		int b;
		
		Data(int a , int c, int d) {
			x = a;
			y = c;
			b = d;
		}
		@Override
		public int compareTo(Data o) {
			return this.b - o.b;
		}
	}
 	
}
