import java.util.*;

public class Main{

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		int cnt[] = new int[n];
		for(int i=0;i<n;i++){
			int a = sc.nextInt();
			cnt[a]++;
		}
		
		
		boolean ok = true;
		for(int i=1+n%2;i<n;i+=2){
			if(cnt[i] != 2)
				ok = false;
		}
		if(n%2==1 && cnt[0] != 1)
			ok = false;
		
		if(ok){
			int ans = 1;
			for(int i=0;i<n/2;i++){
				ans *= 2;
				ans %= (int)1e9+7;
			}
			System.out.println(ans);
		}
		else{
			System.out.println(0);
		}
	}

}
