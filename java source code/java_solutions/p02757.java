import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int N = sc.nextInt();
		int P = sc.nextInt();
		char[] c = sc.next().toCharArray();
		int[] v = new int[N];
		for(int i=0; i<N; i++)
			v[i] = c[i]-'0';
		
		if(P==2 || P==5) {
			long ans = 0;
			for(int i=0; i<N; i++) {
				if(v[i]%P==0) {
					ans += i+1;
				}
			}
			System.out.println(ans);
			sc.close();
			return;
		}
		
		int[] mod = new int[P];
		long ans = 0;
		long a = 1; // a = 10000000...
		long b = 0;
		
		mod[v[0]%P]++;
		a *= 10;
		ans += mod[0];
		mod[0]++;

		for(int i=1; i<N; i++) {
			b = (b*10+v[i])%P;
			int index = (int)(((P-(b%P)) * rev(a, P))%P);
			ans += mod[index];
			mod[index]++;
			a = (a * 10)%P;
		}
		
		System.out.println(ans);
		sc.close();
	}
	
	static long rev(long a, long MOD) {
		return pow(a, MOD-2, MOD);
	}
	
	static long pow(long a, long b, long MOD) {
		long ans = 1;
		while(b>0) {
			if((b&1)!=0) {
				ans = (ans * a)%MOD;
			}
			b>>=1;
			a = (a*a)%MOD;
		}
		return ans;
	}

}
