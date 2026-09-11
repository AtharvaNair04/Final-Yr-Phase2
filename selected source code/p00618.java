import java.util.Scanner;

//Course Planning for Lazy Students
public class Main{

	int n, u;
	int[] c, r;
	int ans;
	boolean[] v;
	
	void dfs(int s, int sum, int depth){
		if(u<=sum)ans = depth;
		if(ans<=depth)return;
		for(int i=0;i<n;i++){
			if((s&(1<<i))>0)continue;
			if((r[i]&s)!=r[i])continue;
			int x = s|1<<i;
			if(v[x])continue;
			v[x] = true;
			dfs(x, sum+c[i], depth+1);
		}
	}
	
	void run(){
		Scanner sc = new Scanner(System.in);
		while(true){
			n = sc.nextInt();
			u = sc.nextInt();
			if((n|u)==0)break;
			c = new int[n];
			r = new int[n];
			for(int i=0;i<n;i++){
				c[i] = sc.nextInt();
				int x = 0;
				int k = sc.nextInt();
				for(int j=0;j<k;j++)x|=1<<sc.nextInt();
				r[i] = x;
			}
			ans = n;
			v = new boolean[1<<n];
			for(int i=0;i<n;i++)if(r[i]==0){v[1<<i]=true;dfs(1<<i,c[i],1);}
			System.out.println(ans);
		}
	}
	
	public static void main(String[] args) {
		new Main().run();
	}
}
