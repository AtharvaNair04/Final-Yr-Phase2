import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		
		int max = 1;
		int a[] = new int[n+1];
		
		for(int i=1;i<=n;i++){
			a[i] = sc.nextInt();
			if(Math.abs(a[i]) > Math.abs(a[max])){
				max = i;
			}
		}
		
		
		if(a[max] == 0){
			System.out.println(0);
		}
		else{
			int cnt = 0;
			int ans[][] = new int[2*n][2];
			
			for(int i=1;i<=n;i++){
				if(i!=max){
					ans[cnt][0] = max;
					ans[cnt][1] = i;
					a[i] += a[max];
					cnt++;
				}
			}
			
			if(a[max] > 0){
				for(int i=1;i<n;i++){
					if(a[i] > a[i+1]){
						ans[cnt][0] = i;
						ans[cnt][1] = i+1;
						a[i+1] += a[i];
						cnt++;
					}
				}
			}
			else{
				for(int i=n;i>1;i--){
					if(a[i-1] > a[i]){
						ans[cnt][0] = i;
						ans[cnt][1] = i-1;
						a[i-1] += a[i];
						cnt++;
					}
				}
			}
			
			System.out.println(cnt);
			for(int i=0;i<cnt;i++){
				System.out.println(ans[i][0] + " " +ans[i][1]);
			}
		}
	}

}
