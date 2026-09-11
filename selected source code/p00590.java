import java.util.*;
public class Main {
	static Scanner sc = new Scanner(System.in);
	static boolean[] prime = new boolean[10001];
	static int n;
	public static void main(String[] args) {
		make_prime();
		while(read()){
			solve();
		}
	}
	
	static boolean read(){
		if(!sc.hasNext())return false;
		n = sc.nextInt();
		return true;
	}
	
	static void solve(){
		int res = 0;
		for(int i = 2; i <= n; i++){
			if(prime[i] && prime[n - i + 1])res++;
		}
		System.out.println(res);
	}
	
	static void make_prime(){
		Arrays.fill(prime, true);
		prime[0] = false;
		prime[1] = false;
		for(int i = 2; i < prime.length; i++){
			if(prime[i]){
				for(int j = 2*i; j < prime.length; j += i){
					prime[j] = false;
				}
			}
		}
	}

}
