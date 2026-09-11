import java.util.Arrays;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		try {
			//solve(new Scanner(new File("src\\h\\10-maximum00.in")));
			solve(new Scanner(System.in));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void solve(Scanner sc) {
		int n = sc.nextInt();
		int[] x = new int[n];
		for(int i=0;i<n;i++) {
			x[i] = sc.nextInt();
		}
		RMQ dp = new RMQ(n+1);
		for(int i=0;i<n;i++) {
			long max = x[i];
			max = Math.max(max,max+dp.query(0,x[i]));
			dp.update(x[i], max);
			//System.out.println(dp);
		}
		long ans = n*(n+1L)/2;
		ans-=dp.query(0, n+1);
		System.out.println(ans);
		sc.close();
	}
}
class RMQ {
	private int size;
	private int size_ = 1;
	private long[] data;
	public RMQ(int size) {
		this.size = size;
		while(this.size_<this.size) {
			this.size_*=2;
		}
		data = new long[size_*2-1];
		this.init();
	}

	private void init() {
		Arrays.fill(data, -1);
	}

	public void update(int k,long a) {
		k+=size_-1;
		data[k] = a;
		while(k>0) {
			k=(k-1)/2;
			data[k] = Math.max(data[k*2+1],data[k*2+2]);
		}
	}

	public long query(int a,int b) {
		return query(a,b,0,0,size_);
	}

	//[a,b)の区間について処理
	//kは接点の番号,[l,r)がkに対応する節点
	public long query(int a,int b,int k,int l,int r) {
		if (r<=a || b<=l) {
			return Integer.MIN_VALUE;
		}
		if (a<=l && r<=b) {
			return data[k];
		}else{
			return Math.max(query(a,b,k*2+1,l,(l+r)/2)
					,query(a,b,k*2+2,(l+r)/2,r));
		}
	}

	public String toString() {
		String s = "";
		for(int i=0;i<size;i++) {
			s += query(i,i+1) + ",";
		}
		return s;
	}
}
