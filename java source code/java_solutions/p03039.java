import java.util.*;
public class Main {
	static long M = 1000000007;
	public static void main(String[] args) {
		for(int i = 0; i < 200001; i++) fac(i);
		Scanner seer = new Scanner(System.in);
		int n = seer.nextInt();
		int m = seer.nextInt();
		int k = seer.nextInt();
		//todos contra todos
		long[] calc = new long[n];
		for(int i = 1; i < m; i++){
			calc[0] = sum(calc[0], mult(i, m-i));
		}
		for(int i = 1; i < n; i++){
			calc[i] = sum(calc[i-1], mult(calc[0], 2), mult(m, m, i));
		}
		long tot = 0;
		for(long c: calc) tot = sum(tot, c);
		
		System.out.println(mult(tot, comb(n*m-2, k-2)));
	}
	static long[] fac = new long[200001];
	static long[] invfac = new long[200001];
	static long fac(int n){
		if(n == 0) return fac[0] = 1;
		else if(fac[n] > 0) return fac[n];
		else return fac[n] = mult(n, fac(n-1));
	}
	static long invfac(int n){
		if(invfac[n] > 0) return invfac[n];
		else return invfac[n] = inv(fac[n]);
	}
	static long inv(long a){
		return (gcdex(a, M).r + M) % M;
	}
	static Pair gcdex(long a, long b){
		if(b > a) return gcdex(b, a).invert();
		else if(b == 0) return new Pair(1, 0);
		else{
			Pair p = gcdex(b, a%b); //p.r * b + p.s * (a - b(a/b)) = 1
			return new Pair(p.s, p.r - p.s*(a/b));
		}
	}
	static class Pair{
		long r, s;
		public Pair(long r, long s){
			this.r = r; this.s = s;
		}
		public Pair invert(){
			return new Pair(s, r);
		}
	}
	static long comb(int n, int k){
		return mult(fac(n), invfac(k), invfac(n-k));
	}
	static long mult(long... ls){
		long res = 1;
		for(long a: ls){
			res = (res * a) % M;
		}
		return res;
	}
	static long sum(long... ls){
		long res = 0;
		for(long a: ls){
			res = (res + a) % M;
		}
		return res;
	}
}
