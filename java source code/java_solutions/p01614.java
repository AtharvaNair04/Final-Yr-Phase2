import java.util.*;

public class Main {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int n = sc.nextInt();
		int[] a = new int[n];
		int[] b = new int[n];
		int[] c = new int[n];
		for(int i=0;i<n;i++){
			a[i] = sc.nextInt();
			b[i] = sc.nextInt();
			c[i] = sc.nextInt();
		}
		
		int[] ans = new int[394];
		Arrays.fill(ans, -1);
		ans[0] = 0;
		
		for(int i=0;i<=393;i++){
			for(int j=0;j<n;j++){
				if(ans[i]>=0){
					for(int k=a[j];k<=b[j];k++){
						if(i+k<=393) ans[i+k] = Math.max(ans[i+k], ans[i]+c[j]);
					}
				}
			}
		}
		
		int m = sc.nextInt();
		int[] d = new int[m];
		boolean flag = false;
		for(int i=0;i<m;i++){
			d[i] = sc.nextInt();
			if(ans[d[i]]==-1) flag = true;
		}
		
		if(flag==true){
			System.out.println("-1");
		}else{
			for(int i=0;i<m;i++) System.out.println(ans[d[i]]);
		}
	}	
}
