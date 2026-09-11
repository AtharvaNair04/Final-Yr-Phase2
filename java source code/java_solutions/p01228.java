import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) {
		int t = sc.nextInt();
		for(int i=0;i<t;i++) {
			solve();
		}
	}
	static void solve() {
		int n = sc.nextInt();
		ArrayList<Frac> al = new ArrayList<Frac>();
		for(int i=0;i<n;i++) {
			String r = sc.next();
			int m = r.length() / 2;
			for(int j=0;j<m;j++) {
				int p = Integer.parseInt(r.substring(j*2, j*2+2), 16);
				for(int k=0;k<8;k++) {
					if ((p>>k&1) == 1) {
						al.add(new Frac(j,m,k));
					}
				}
			}
		}
		int lcm = 1;
		for(Frac f:al) {
			lcm = (int) Frac.lcm(lcm, f.b);
			if (lcm > 1024) {
				break;
			}
		}
		if (lcm > 1024) {
			System.out.println("Too complex.");
			return;
		}
//		System.out.println(lcm);
		int[] pattern = new int[lcm];
		for(Frac f:al) {
			pattern[(int) (f.a * lcm / f.b)] |= 1 << f.type;
		}
		StringBuilder sb = new StringBuilder();
		for(int p:pattern) {
			String s = Integer.toHexString(p).toUpperCase();
			if (s.length() == 1) {
				s = "0" + s;
			}
			sb.append(s);
		}
		System.out.println(sb.toString());
	}
}
class Frac {
	long a,b;
	int type;
	public Frac(long a,long b,int type) {
		long g = gcd(a,b);
		this.a = a / g;
		this.b = b / g;
		this.type = type;
	}
	public static long gcd(long a,long b) {
		while(b!=0) {
			long r = a%b;
			a = b;
			b = r;
		}
		return a;
	}
	public static long lcm(long a,long b) {
		return a / gcd(a,b) * b;
	}
}
