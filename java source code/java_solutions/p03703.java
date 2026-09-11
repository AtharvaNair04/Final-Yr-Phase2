import java.util.*;

public class Main {
	public static class SegmentTree {
		int N;
		int[] b;
		
		public SegmentTree(int n) {
			N = 1;
			while(N<n)
				N*=2;
			
			b = new int[N*2];
		}
		
		public int rangeSum(int l, int r) {
			return rangeSum(1, 0, N, l, r);
		}
		
		public int rangeSum(int index, int l, int r, int x, int y) {
			if(y<=l || r<=x) {
				return 0;
			} else if(x<=l && r<=y) {
				return b[index];
			} else {
				int mid = (l+r)/2;
				int g1=0,g2=0;
				if(x<mid)
					g1 = rangeSum(index*2, l, mid, x, Math.min(y, mid));
				if(mid<y)
					g2 = rangeSum(index*2+1, mid, r, Math.max(x, mid), y);
				return g1+g2;
			}
		}
		
		public void add(int index, int value) {
			b[index+N]+=value;
			int i = (index+N)/2;
			while(i>0) {
				b[i] = b[i*2]+b[i*2+1];
				i/=2;
			}
		}
	}


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int K = sc.nextInt();
		int[] a = new int[N];
		for(int i=0; i<N; i++)
			a[i] = sc.nextInt()-K;
		
		long[] s = new long[N+1];
		HashSet<Long> hash = new HashSet<>();
		hash.add(0l);
		for(int i=0; i<N; i++) {
			s[i+1] = s[i] + a[i];
			hash.add(s[i+1]);
		}
		
		Long[] index = hash.toArray(new Long[0]);
		Arrays.sort(index);
		
		long ans = 0;
		SegmentTree st = new SegmentTree(index.length);
		st.add(Arrays.binarySearch(index, 0l), 1);
		for(int i=0; i<N; i++) {
			int j = Arrays.binarySearch(index, s[i+1]);
			ans += st.rangeSum(0, j+1);
			st.add(j, 1);
		}
		
		System.out.println(ans);
		sc.close();
	}
}
