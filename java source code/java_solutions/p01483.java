import java.util.Arrays;
import java.util.Scanner;

//Elevator
public class Main{

	int[] d;
	void run(){
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt(), m = sc.nextInt(), W = sc.nextInt(), K = 0;
		int[] w = new int[15], f = new int[15], s = new int[n+1];
		for(int i=1;i<=n;i++){
			int k = sc.nextInt();
			while(k--!=0){
				w[K] = sc.nextInt(); s[i]+=1<<K; f[K++] = i;
			}
		}
		int[] sum = new int[1<<K];
		boolean[] valid = new boolean[1<<K];
		for(int S=0;S<1<<K;S++){
			for(int j=0;j<K;j++)if(((S>>j)&1)>0){
				sum[S]+=w[j];
			}
			valid[S] = sum[S]<=W;
		}
		int[] min = new int[1<<K];
		Arrays.fill(min, K);
		min[0] = 0;
		for(int S=1;S<1<<K;S++){
			int res = K, sub = S;
			do{
				if(valid[sub]){
					res = Math.min(res, min[S-sub]+1);
				}
				sub = (sub-1)&S;
			}while(sub!=S);
			min[S] = res;
		}
		int res = f[K-1]-m;
		for(int i=f[K-1];i>1;i--){
			res+=2*(min[s[i]]-1)+1;
			s[i-1]|=s[i];
		}
		System.out.println(Math.max(res, 0));
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
